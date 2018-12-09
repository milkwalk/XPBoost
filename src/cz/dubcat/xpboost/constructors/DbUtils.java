package cz.dubcat.xpboost.constructors;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DbUtils {
    public static void close(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }

    }

    public static void close(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }

    }

    public static void close(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }

    }

    public static void closeQuietly(Connection conn) {
        try {
            close(conn);
        } catch (SQLException arg1) {
            ;
        }

    }

    public static void closeQuietly(Connection conn, Statement stmt, ResultSet rs) {
        try {
            closeQuietly(rs);
        } finally {
            try {
                closeQuietly(stmt);
            } finally {
                closeQuietly(conn);
            }
        }

    }

    public static void closeQuietly(ResultSet rs) {
        try {
            close(rs);
        } catch (SQLException arg1) {
            ;
        }

    }

    public static void closeQuietly(Statement stmt) {
        try {
            close(stmt);
        } catch (SQLException arg1) {
            ;
        }

    }

    public static void commitAndClose(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.commit();
            } finally {
                conn.close();
            }
        }

    }

    public static void commitAndCloseQuietly(Connection conn) {
        try {
            commitAndClose(conn);
        } catch (SQLException arg1) {
            ;
        }

    }

    public static boolean loadDriver(String driverClassName) {
        try {
            Class.forName(driverClassName).newInstance();
            return true;
        } catch (ClassNotFoundException arg1) {
            return false;
        } catch (IllegalAccessException arg2) {
            return true;
        } catch (InstantiationException arg3) {
            return false;
        } catch (Throwable arg4) {
            return false;
        }
    }

    public static void printStackTrace(SQLException e) {
        printStackTrace(e, new PrintWriter(System.err));
    }

    public static void printStackTrace(SQLException e, PrintWriter pw) {
        SQLException next = e;

        while (next != null) {
            next.printStackTrace(pw);
            next = next.getNextException();
            if (next != null) {
                pw.println("Next SQLException:");
            }
        }

    }

    public static void printWarnings(Connection conn) {
        printWarnings(conn, new PrintWriter(System.err));
    }

    public static void printWarnings(Connection conn, PrintWriter pw) {
        if (conn != null) {
            try {
                printStackTrace(conn.getWarnings(), pw);
            } catch (SQLException arg2) {
                printStackTrace(arg2, pw);
            }
        }

    }

    public static void rollback(Connection conn) throws SQLException {
        if (conn != null) {
            conn.rollback();
        }

    }

    public static void rollbackAndClose(Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.rollback();
            } finally {
                conn.close();
            }
        }

    }

    public static void rollbackAndCloseQuietly(Connection conn) {
        try {
            rollbackAndClose(conn);
        } catch (SQLException arg1) {
            ;
        }

    }
}