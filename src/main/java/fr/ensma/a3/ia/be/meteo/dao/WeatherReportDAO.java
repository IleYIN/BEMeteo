package fr.ensma.a3.ia.be.meteo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.ensma.a3.ia.be.dto.Position;
import fr.ensma.a3.ia.be.dto.Weather;
import fr.ensma.a3.ia.be.utils.JDBCUtil;
import fr.ensma.a3.ia.be.utils.ServerConfig;

public class WeatherReportDAO {

	private static final Logger logger = LogManager.getLogger(WeatherReportDAO.class);
	private static fr.ensma.a3.ia.be.utils.ServerConfig cfg = ConfigCache.getOrCreate(ServerConfig.class);
	private final String tableName;
	private final Position pos;
	
	private Connection conn = null;
	private PreparedStatement ps = null;
	private Statement st = null;
	private ResultSet rs = null;

	public WeatherReportDAO(final Position pos) {
		this.pos = pos;
		// get config
		tableName = cfg.pgTableWeatherReport();
	}


	public void addWeatherReport(Weather report) {

		Timestamp currentTime = new Timestamp(new Date().getTime());
		Timestamp lastWeatherTime = null;

		conn = JDBCUtil.getPostgreConn();

		try {
			st = conn.createStatement();
			String query = "select max(weather_time) from "+ tableName + " where latitude="+ pos.getLat()+" and longitude="+pos.getLon()+";";
			rs = st.executeQuery(query);

			while (rs.next()) {
				lastWeatherTime = rs.getTimestamp("max");
			}
		} catch (SQLException e) {
			logger.error("Could not get lastWeatherTimestamp",e);
		} finally {
			JDBCUtil.close( rs, st, conn);
		}

		//si la différence entres judgeinserttime et time est moins que difftime
		//il est possible que judgeinserttime est null si la table est vide quand on lance le programm
		if (lastWeatherTime == null || lastWeatherTime.before(report.getDate())) {

			conn = JDBCUtil.getPostgreConn();
			try {

				ps = conn.prepareStatement("insert into "+ tableName +" (weather_time,latitude,longitude,temperature,pressure,humidity,weather_description,clouds,wind_speed,wind_direction,rain,snow,insert_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
				ps.setObject(1, report.getDate(),Types.TIMESTAMP);
				//System.out.println("insert_weathertime_"+report.getDate());
				
				ps.setObject(2, report.getLat());
				ps.setObject(3, report.getLon());
				ps.setObject(4, report.getTemperature());
				ps.setObject(5, report.getPressure());
				ps.setObject(6, report.getHumidity());
				ps.setObject(7, report.getWeatherdescription());
				ps.setObject(8, report.getClouds());
				ps.setObject(9, report.getWind_speed());
				ps.setObject(10, report.getWind_direction());
				ps.setObject(11, report.getRain());
				ps.setObject(12, report.getSnow());
				ps.setObject(13, currentTime, Types.TIMESTAMP);
				ps.execute();

				logger.debug(report.toString()+ " has been inserted into "+  tableName);

			} catch (Exception e) {
				logger.error("could not insert into "+tableName, e);
				//System.out.println(e.getMessage());
				//System.out.println(DateUtil.setDate(new Date())); 
			} finally {

				JDBCUtil.close(ps, conn);
			}
		} else {
			logger.debug("There's no new information for the position "+ pos.toString() +"for the table "+ tableName);
			//System.out.println("pas de nouvelle information sur le tableau " + TABLE_NAME +" pour la position "+ po.toString()+" "+DateUtil.setDate(new Date()));

		} 

	}


}
