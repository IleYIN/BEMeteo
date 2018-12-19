package fr.ensma.a3.ia.be.meteo.timertask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import fr.ensma.a3.ia.be.meteo.Position;
import fr.ensma.a3.ia.be.meteo.RecuperationMeteo;
import fr.ensma.a3.ia.be.meteo.Weather;
import fr.ensma.a3.ia.be.utils.DateUtil;
import fr.ensma.a3.ia.be.utils.JDBCUtil;

public class InsertMeteoForecastPeriodique extends TimerTask{

	private static final String TABLE_NAME = "weather_forecast";
	private String nomtache;
	private Position po;
	private Timestamp time;
	private Timestamp judgeinserttime; 
	private long difftime;

	public InsertMeteoForecastPeriodique() {

	}

	public InsertMeteoForecastPeriodique(Position po, String nomtache, long difftime) {
		this.po = po;
		this.nomtache = nomtache;
		this.difftime = difftime;
	}


	public void run() {
		System.out.println(Thread.currentThread().getName()+ " -> " + nomtache +" started");
		insertForecastMeteo(po, difftime);

	}





	private void insertForecastMeteo(Position po, long difftime) {

		RecuperationMeteo rm = new RecuperationMeteo(po);

		rm.remplirForecastMeteo();

		time = new Timestamp(new Date().getTime());
		//		String inserttime = DateUtil.setDate(time) ;

		List<Weather> result = po.getListmeteoForecast();


		Connection conn = null;
		PreparedStatement ps = null;
		//		ResultSet rs = null;

		if(judgeinserttime==null) {

			conn = JDBCUtil.getPostgreConn();

			Statement st = null;
			ResultSet rs = null;
			try {
				st = conn.createStatement();
				String query = "select max(insert_time) from "+ TABLE_NAME + ";";
				rs = st.executeQuery(query);

				while (rs.next()) {
					Timestamp date = rs.getTimestamp("max");
					judgeinserttime = date;
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				System.out.println(DateUtil.setDate(new Date())); 
			} finally {
				JDBCUtil.close( rs, st, conn);
			}
		}

		//si la différence entres judgeinserttime et time est moins que difftime
//		System.out.println(time.getTime());
//		System.out.println(time);
//		System.out.println(judgeinserttime.getTime());
//		System.out.println(judgeinserttime);
//		System.out.println(time.getTime()-judgeinserttime.getTime());
		//il est possible que judgeinserttime est null si la table est vide quand on lance le programm
		if (!(result==null) && !result.isEmpty() && (judgeinserttime==null||time.getTime()-judgeinserttime.getTime()>=difftime)) {





			conn = JDBCUtil.getPostgreConn();
			try {
				for(Weather meteo : result) {

					//				System.out.println(meteo);
					ps = conn.prepareStatement("insert into "+ TABLE_NAME +" (weather_time,latitude,longitude,temperature,pressure,humidity,weather_description,clouds,wind_speed,wind_direction,rain,snow,insert_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
					ps.setObject(1, meteo.getDate(),Types.TIMESTAMP);
					ps.setObject(2, meteo.getLat());
					ps.setObject(3, meteo.getLon());
					ps.setObject(4, meteo.getTemperature());
					ps.setObject(5, meteo.getPressure());
					ps.setObject(6, meteo.getHumidity());
					ps.setObject(7, meteo.getWeatherdescription());
					ps.setObject(8, meteo.getClouds());
					ps.setObject(9, meteo.getWind_speed());
					ps.setObject(10, meteo.getWind_direction());
					ps.setObject(11, meteo.getRain());
					ps.setObject(12, meteo.getSnow());
					ps.setObject(13, time, Types.TIMESTAMP);
					ps.execute();

				}
				System.out.println("insert into "+  TABLE_NAME + " finished at "+DateUtil.setDate(new Date()));
				
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println(DateUtil.setDate(new Date())); 
			} finally {

				JDBCUtil.close(ps, conn);
			}
		} else {
			System.out.println("pas de nouvelle information sur le tableau " + TABLE_NAME +" pour la position "+ po.toString()+" "+DateUtil.setDate(new Date()));

		} 

	}


}
