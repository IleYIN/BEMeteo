package fr.ensma.a3.ia.be.meteo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.aeonbits.owner.ConfigCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.ensma.a3.ia.be.dto.Position;
import fr.ensma.a3.ia.be.dto.Weather;
import fr.ensma.a3.ia.be.utils.JDBCUtil;
import fr.ensma.a3.ia.be.utils.ServerConfig;

public class WeatherForecastDAO {
	
	private static final Logger logger = LogManager.getLogger(WeatherReportDAO.class);
	private static fr.ensma.a3.ia.be.utils.ServerConfig cfg = ConfigCache.getOrCreate(ServerConfig.class);

	private final String tableName;
	private final Position pos;
	
	private Connection conn = null;
	private PreparedStatement ps = null;
	private Statement st = null;
	private ResultSet rs = null;

	public WeatherForecastDAO(final Position pos) {
		// get config
		this.pos = pos;
		tableName = cfg.pgTableWeatherForecast();
	}

	public void addWeatherForecast(List<Weather> listWeather) {

		Timestamp currentTime = new Timestamp(new Date().getTime());
		Timestamp lastInsertedTime = null;

		conn = JDBCUtil.getPostgreConn();

		try {
			st = conn.createStatement();
			String query = "select max(insert_time) from "+ tableName +" where latitude="+ pos.getLat()+" and longitude="+pos.getLon()+";";
			rs = st.executeQuery(query);

			while (rs.next()) {
				lastInsertedTime = rs.getTimestamp("max");
			}
		} catch (SQLException e) {
			logger.error("Could not get lastInsertedDate",e);
			
		} finally {
			JDBCUtil.close( rs, st, conn);
		}
		
		
		//si la différence entres judgeinserttime et time est moins que difftime
		//il est possible que judgeinserttime est null si la table est vide quand on lance le programm

		//24h update
		if(lastInsertedTime==null || lastInsertedTime.getTime() <= (currentTime.getTime() - (1000*3600*24)) ) {

			conn = JDBCUtil.getPostgreConn();

//		if (!(result==null) && !result.isEmpty() && (judgeinserttime==null||time.getTime()-judgeinserttime.getTime()>=difftime)) {

			try {
				for(Weather weatherfore : listWeather) {

					ps = conn.prepareStatement("insert into "+ tableName +" (weather_time,latitude,longitude,temperature,pressure,humidity,weather_description,clouds,wind_speed,wind_direction,rain,snow,insert_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
					ps.setObject(1, weatherfore.getDate(),Types.TIMESTAMP);
					ps.setObject(2, weatherfore.getLat());
					ps.setObject(3, weatherfore.getLon());
					ps.setObject(4, weatherfore.getTemperature());
					ps.setObject(5, weatherfore.getPressure());
					ps.setObject(6, weatherfore.getHumidity());
					ps.setObject(7, weatherfore.getWeatherdescription());
					ps.setObject(8, weatherfore.getClouds());
					ps.setObject(9, weatherfore.getWind_speed());
					ps.setObject(10, weatherfore.getWind_direction());
					ps.setObject(11, weatherfore.getRain());
					ps.setObject(12, weatherfore.getSnow());
					ps.setObject(13, currentTime, Types.TIMESTAMP);
					ps.execute();

				}
				
				logger.debug("insert into "+  tableName + " finished");
				
			} catch (Exception e) {
				logger.error("could not insert into "+tableName, e);
//				System.out.println(e.getMessage());
//				System.out.println(DateUtil.setDate(new Date())); 
			} finally {

				JDBCUtil.close(ps, conn);
			}
		} else {
			logger.debug("There's no new information for "+ tableName);
//			System.out.println("pas de nouvelle information sur le tableau " + TABLE_NAME +" pour la position "+ po.toString()+" "+DateUtil.setDate(new Date()));

		} 

	}


}
