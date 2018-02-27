package settings;

import logicGame.Agent;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GameSettings extends Properties {

    private static GameSettings gs = new GameSettings();

    public String getProperty(String group, String key) {
        return getProperty(group + '.' + key);
    }

    /** Метод задающий параметры из файла config.properties в класс GameResources */
    public static void setGameSettings() {

        try{
            gs.load(new FileInputStream("C:\\Users\\Владислав\\IdeaProjects\\CW\\config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        GameResources.agentSize = Double.valueOf(gs.getProperty("agentSize"));
        GameResources.agentSpeed = Double.valueOf(gs.getProperty("agentSpeed"));
        GameResources.energyLost = Double.valueOf(gs.getProperty("energyLost"));
        GameResources.minSizeFeed = Integer.valueOf(gs.getProperty("minSizeFeed"));
        GameResources.maxSizeFeed = Integer.valueOf(gs.getProperty("maxSizeFeed"));
        GameResources.minEnergyFeed = Integer.valueOf(gs.getProperty("minEnergyFeed"));
        GameResources.maxEnergyFeed = Integer.valueOf(gs.getProperty("maxEnergyFeed"));
        GameResources.timeToMove = Integer.valueOf(gs.getProperty("timeToMove"));
        GameResources.mapSize = Integer.valueOf(gs.getProperty("mapSize"));
        GameResources.USERNAME = gs.getProperty("USERNAME");
        GameResources.PASSWORD = gs.getProperty("PASSWORD");
        GameResources.URL = gs.getProperty("URL");
    }

    /** Метод получающий характеристики агента */
    public Agent getAgent(int group) {
        double speed = Double.valueOf(gs.getProperty("group" + group, "agentSpeed"));
        double size = Double.valueOf(gs.getProperty("group" + group, "agentSize"));
        double damage = Double.valueOf(gs.getProperty("group" + group, "agentDmg"));

        return new Agent(group, speed, size, damage);
    }
}
