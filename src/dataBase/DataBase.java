package dataBase;

import logicGame.Agent;
import settings.GameResources;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    public static void addAgents (ArrayList<Agent> agents) throws SQLException {
        DBProcessor db = new DBProcessor();
        Connection conn = db.getConnection(GameResources.URL, GameResources.USERNAME, GameResources.PASSWORD);

        String query = "select MAX(gameId) as gameId from cwgame.agents";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        int maxID = 0;
        while (rs.next()) {
            if (rs.getString("gameId") == null) {
                maxID = 0;
            } else {
                maxID = rs.getInt("gameId");
            }
        }
        st.close();

        String insert = "insert into cwgame.agents (id, gameId, x, y, `group`, enegy) values (DEFAULT, ?, ?, ?, ?, ?)";
        PreparedStatement prepStat = conn.prepareStatement(insert);
        prepStat.setInt(1,  maxID + 1);
        for (Agent agent : agents) {
            prepStat.setDouble(2, agent.getX());
            prepStat.setDouble(3, agent.getY());
            prepStat.setInt(4, agent.getGroup());
            prepStat.setDouble(5, agent.getEnergy());
            prepStat.execute();
        }

        conn.close();
    }

}
