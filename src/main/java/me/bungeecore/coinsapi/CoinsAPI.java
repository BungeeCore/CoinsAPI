package me.bungeecore.coinsapi;

import me.bungeecore.coinsapi.listener.PlayerLoginListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.UUID;

public class CoinsAPI extends JavaPlugin {

    private static CoinsAPI instance;

    @Override
    public void onEnable() {
        instance = this;
        connect();
        init();
    }

    public static CoinsAPI getInstance() {
        return instance;
    }

    private Connection connection;

    private boolean isConnected() {
        return connection != null;
    }

    private void init() {
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(), getInstance());
    }

    private void connect() {
        if (!isConnected()){
            try {
                connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Coins?autoReconnect=true", "root", "");
                createTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.print("Did you specify correct data?");
            }
        }
    }

    private void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void Update(String qry) {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(qry);
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createTable() {
        Update("CREATE TABLE IF NOT EXISTS Coins (Name VARCHAR(100), UUID VARCHAR(100), Coins VARCHAR(100))");
    }

    public boolean isPlayerExits(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Coins WHERE UUID = '" + uuid.toString() + "'");
            ResultSet set = ps.executeQuery();
            boolean get = set.next();
            set.close();
            ps.close();
            return get;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void registerPlayer(String name, UUID uuid) {
        if (isPlayerExits(uuid)) {
            return;
        }
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Coins (Name, UUID, Coins) VALUES ('" + name + "', '" + uuid.toString() + "', '100')");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setCoins(UUID uuid, int coins) {
        Update("UPDATE Coins SET Coins = '" + coins + "' WHERE UUID = '" + uuid.toString() + "'");
    }

    public int getCoins(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Coins WHERE UUID = '" + uuid.toString() + "'");
            ResultSet set = ps.executeQuery();
            set.next();
            int coins = set.getInt("Coins");
            set.close();
            ps.close();
            return coins;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public void addCoins(UUID uuid, int coins) {
        setCoins(uuid, (getCoins(uuid) + coins));
    }

    public void removeCoins(UUID uuid, int coins) {
        setCoins(uuid, (getCoins(uuid) - coins));
    }
}