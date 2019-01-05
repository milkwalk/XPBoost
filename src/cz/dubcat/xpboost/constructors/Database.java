package cz.dubcat.xpboost.constructors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import cz.dubcat.xpboost.XPBoostMain;

public class Database {
    private final String table_sql = "CREATE TABLE IF NOT EXISTS `xpboost` ("
            + "`id` int(11) NOT NULL AUTO_INCREMENT,"
            + "`uuid` varchar(100) CHARACTER SET utf8 COLLATE utf8_czech_ci NOT NULL," + "`boost` double(8,2) NOT NULL,"
            + "`endtime` bigint(20) NOT NULL," + "`conditions` tinytext CHARACTER SET utf8 COLLATE utf8_czech_ci,"
            + "`advanced` text CHARACTER SET utf8 COLLATE utf8_czech_ci,"
            + "`inserted` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP," + " PRIMARY KEY (`id`),"
            + "UNIQUE KEY `uuid` (`uuid`)" + ") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;";

    public enum DType {
        MYSQL, FILE;
    }

    private static Connection conn;
    public static DType type = DType.FILE;

    public boolean loadMysql() {
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + XPBoostMain.getPlugin().getConfig().getString("database.host") + ":"
                            + XPBoostMain.getPlugin().getConfig().getString("database.port") + "/"
                            + XPBoostMain.getPlugin().getConfig().getString("database.database"),
                    XPBoostMain.getPlugin().getConfig().getString("database.user"),
                    XPBoostMain.getPlugin().getConfig().getString("database.password"));
            type = DType.MYSQL;
            conn.prepareStatement(table_sql).execute();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            XPBoostMain.getPlugin().getLogger().severe("Could not connect to the database. Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(XPBoostMain.getPlugin());
        }
        return false;
    }

    public static Connection getConnection() {
        return conn;
    }
}
