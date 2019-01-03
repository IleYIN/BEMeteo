package fr.ensma.a3.ia.be.utils;

/**
 * Get or close connection with postgresql
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class JDBCUtil {

	private static ServerConfig cfg = ConfigCache.getOrCreate(ServerConfig.class);
	private static final Logger logger = LogManager.getLogger(JDBCUtil.class);

	public static Connection getPostgreConn () {

		Connection conn = null;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			logger.error("Could not find \"org.postgresql.Driver\"");
		}

		String pgUrl = "jdbc:postgresql://" + cfg.pgAddress() + ":" + cfg.pgPort() + "/" + cfg.pgDatabase();
		String pgUser = cfg.pgUser();
		String pgPassword = cfg.pgPassword();
		
		try {
			logger.debug("Connecting to "+pgUrl+"...");
			conn = DriverManager.getConnection(pgUrl, pgUser, pgPassword);
		} catch (SQLException e) {
			logger.error("Could not connect to "+pgUrl, e);
			try {
				conn = DriverManager.getConnection(pgUrl, pgUser, pgPassword);
			} catch (SQLException e1) {
				logger.error("Could not reconnect to the database", e1);
			}
		}

		return conn;
	}

	public static void close(ResultSet rs, Statement ps, Connection conn) {

		try {
			if(rs!=null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (ps!=null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn!=null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static void close(Statement ps, Connection conn) {

		try {
			if (ps!=null) {
				ps.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (conn!=null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void close(Connection conn) {

		try {
			if (conn!=null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
