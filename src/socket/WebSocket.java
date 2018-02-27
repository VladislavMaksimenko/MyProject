package socket;

import mainGame.Game;
import mainGame.Feed;
import logicGame.Agent;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/** Класс отвечающий за соединение веб клиента с серверной частью */
@ServerEndpoint("/control")
public class WebSocket {

    /** Текущая сессия */
    private static Session curSession;

    /** Метод обрабатывающий поступающие сообщение */
    @OnMessage
    public void onMessage(String message) throws SQLException, InterruptedException {
        if(message.equals("Start")){
            if (Game.getExample().getGameSession() > 0)
                Game.getExample().restart();
            Game.getExample().start();
        }else if (message.equals("Stop")){
            Game.getExample().stop();
        }
    }

    /** Метод срабатывает при закрытии сессии */
    @OnClose
    public void close(Session session) {
        System.out.println("close");
    }

    /** Метод задаёт текущую сессию */
    @OnOpen
    public void onOpen(Session s) {
        curSession = s;
    }

    /** Метод отвечающий за обновление изображения объектов на карте */
    public static void updateGame(ArrayList<Agent> agents, ArrayList<Feed> feeds) throws IOException {
        synchronized(curSession){
        String json = gameToJson(agents,feeds);
        curSession.getBasicRemote().sendText(json);}
    }

    /** Преобразует контейнер Агентов и корма в тект JSON */
    private static String gameToJson(ArrayList<Agent> agents, ArrayList<Feed> feeds){
        String json = "{\"agents\": [";
        try {
            ArrayList<String> agentsStr = new ArrayList <>();
            for (Agent agent : agents) {
                if (agent.alive()) {
                    agentsStr.add("{\"x\": " + agent.getX()
                            + ", \"y\": " + agent.getY()
                            + ", \"group\": " + agent.getGroup()
                            + ", \"energy\": " + agent.getEnergy()
                            + ", \"size\": " + agent.getSize()
                            + "}");
                }
            }
            json += String.join(",", agentsStr);
            json += "],\"foods\": [";

            ArrayList<String> foodStr = new ArrayList<>();
            for (Feed feed : feeds) {
                foodStr.add("{\"x\": " + feed.getX() +
                        ", \"y\": " + feed.getY() +
                        ", \"size\": " + feed.getEnergy() +
                        ", \"energy\": " + feed.getEnergy() + "}");
            }
            json += String.join(",", foodStr);
            json += "], \"isGame\": " + Game.getExample().isGame + "}";

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }

        return json;
    }

}
