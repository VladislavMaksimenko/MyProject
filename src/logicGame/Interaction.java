package logicGame;

import mainGame.Feed;
import mainGame.MainGame;
import dataBase.DataBase;
import mainGame.Game;
import settings.GameResources;
import settings.GameSettings;
import socket.WebSocket;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/** Класс отвечающий за поведение и взаимодействие объектов на поле */
public class Interaction implements Runnable {

    /** Контейнер Агентов */
    private ArrayList<logicGame.Agent> agents;

    /** Контейнер корма */
    private ArrayList<Feed> feeds;

    /** Внутренний класс, отвечающий за производство корма */
    private FeedController feedController;

    /** Объект отвечающий за получение агентов в соответствии с заданными настройками */
    private GameSettings gs;

    /** Базовый конструктор */
    public Interaction() {
        feedController = new FeedController();
        agents = new ArrayList<>();
        gs = new GameSettings();
    }

    /** Создание и запуск Агентов по данным из файла ресурсов
     * Старт контролера корма
     * Старт основного потока */
    public void start() {

        try{
            gs.load(new FileInputStream("C:\\Users\\Владислав\\IdeaProjects\\CW\\config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        agents.add(gs.getAgent(0));
        agents.add(gs.getAgent(0));
        agents.add(gs.getAgent(1));
        agents.add(gs.getAgent(1));
        startAgents();

        feedController.start();

        /* Поток в котором работает класс */
        Thread thread = new Thread(this);
        thread.start();
    }

    /** Запуск основного цикла игры */
    @Override
    public void run() {
        while (Game.getExample().isGame) {
            try {
                for (int i = 0; i < agents.size(); i++) {
                    if (!agents.get(i).alive())
                        agents.remove(agents.get(i));
                    else {
                        for (int j = 0; j < feeds.size(); j++)
                            interactionFeed(agents.get(i), feeds.get(j));

                        for (int m = 0; m < agents.size(); m++)
                            if (!agents.get(i).equals(agents.get(m)))
                                interactionAgent(agents.get(i), agents.get(m));
                    }
                }
                WebSocket.updateGame(agents, feeds);
                if(agents.size() == 1) {
                    stopGame();
                }
                Thread.sleep(GameResources.timeToMove);
            } catch (InterruptedException | IOException | SQLException e){
                e.printStackTrace();
            }
        }
    }

    /** Остановка игры
     * Запись выживших Агентов в базу данных */
    public void stopGame() throws SQLException {
        stopAgents();
        DataBase.addAgents(agents);
        feedController.stop();
        Game.getExample().isGame = false;
    }

    /** Проверка на соприкоснование двух Агентов
     * Взаимодействие двух Агентов, после получения урона Агенты меняют траекторию */
    private void interactionAgent(logicGame.Agent first, logicGame.Agent second) {
        if (distance(first, second) <= (first.getSize() + second.getSize()) / 2) {
            first.giveDmg(second.getDamage());
            second.giveDmg(first.getDamage());
            first.changeDir();
            second.changeDir();
        }
    }

    /** Проверка на соприкоснование Агента и корма
     * Взаимодейстиве Агента с кормом, пополнение энергии от корма */
    private void interactionFeed(logicGame.Agent agent, Feed feed) {
        if (distance(agent, feed) <= (feed.getSize() + agent.getSize()) / 2) {
            agent.eat(feed);
            feeds.remove(feed);
        }
    }

    /** Метод возвращает расстояние между двумя объектами */
    private double distance(MainGame object1, MainGame object2) {
        return Math.sqrt(Math.pow(object2.getX() - object1.getX(), 2) + Math.pow(object2.getY() - object1.getY(), 2));
    }

    /** Метод останавливает жизненный цикл всех агентов */
    private void stopAgents() {
        for (Agent agent : agents) {
            agent.stop();
        }
    }

    /** Метод запускает жизненный цикл всех агентов */
    private void startAgents() {
        for (Agent agent : agents) {
            agent.start();
        }
    }

    /** Внутриний класс отвечающий за распределение пирогов на поле */
    private class FeedController implements Runnable {

        /** Поток в котором работает котролер */
        Thread feedControllerThread;

        /** Спавн корма
         *  true - продолжать
         *  false - прекратить */
        private boolean spawn;

        /** Базовый конструктов создающий массив корма */
        private FeedController() {
            feeds = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                feeds.add(new Feed());
            }
        }

        /** Метод запускает жизненный цикл контроллера */
        private void start() {
            spawn = true;
            feedControllerThread = new Thread(this);
            feedControllerThread.start();
        }

        /** Метод останавливает жизненный цикл контроллера */
        private void stop() {
            spawn = false;
        }

        /** Метод отвечающий за распространение корма */
        @Override
        public void run() {
            while (spawn) {
                feeds.add(new Feed());
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
