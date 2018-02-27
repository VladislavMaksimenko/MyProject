package mainGame;

import logicGame.Interaction;
import settings.GameSettings;

import java.sql.SQLException;

public class Game {

    private static Game game = new Game();

    public static Game getExample() {
        if (game == null)
            game = new Game();
        return game;
    }

    /** Идентификатор текущей игры */
    private int gameSession;

    /** Объект контролирующий взаимодействие объектов на карте */
    private Interaction interaction;

    /** Значание текущего состояния игры */
    public boolean isGame;

    /** Базовый конструктор класса, в нем происходит считывание файла config.properties,
     * создается экземпляр класса Interaction и увеличивается счетчик игры на единицу. */
    private Game() {
        GameSettings.setGameSettings();
        interaction = new Interaction();
        gameSession++;
    }

    /** Запускает игровой процесс, ставит состояние игры в значение true */
    public void start() throws SQLException, InterruptedException {
        interaction.stopGame();
        Thread.sleep(150);
        isGame = true;
        interaction.start();
    }

    /** Останавливает процесс игры */
    public void stop() throws SQLException {
        isGame = false;
        interaction.stopGame();
    }

    public int getGameSession() {
        return gameSession;
    }

    /** Запускает процесс игры заново */
    public void restart() {
        game = new Game();
    }
}
