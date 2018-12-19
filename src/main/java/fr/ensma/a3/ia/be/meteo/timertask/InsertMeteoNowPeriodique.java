package fr.ensma.a3.ia.be.meteo.timertask;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.TimerTask;
import fr.ensma.a3.ia.be.meteo.Position;
import fr.ensma.a3.ia.be.meteo.RecuperationMeteo;
import fr.ensma.a3.ia.be.meteo.Weather;
import fr.ensma.a3.ia.be.utils.DateUtil;
import fr.ensma.a3.ia.be.utils.JDBCUtil;

public class InsertMeteoNowPeriodique extends TimerTask{
	
	private static final String TABLE_NAME = "weather_now";

	private String nomtache;

	private Position po;
	private Timestamp time;
	private Timestamp judgeweathertime;

	public static String getTableName() {
		return TABLE_NAME;
	}

	public InsertMeteoNowPeriodique() {

	}

	public InsertMeteoNowPeriodique(Position po, String nomtache) {
		this.po = po;
		this.nomtache = nomtache;
	}


	public void run() {

		System.out.println(Thread.currentThread().getName()+ " -> " + nomtache +" started");
		insertMeteo(po);

	}





	private void insertMeteo(Position po) {

		RecuperationMeteo rm = new RecuperationMeteo(po);

		rm.remplirReleveMeteoActuel();
		Weather meteo = po.getMeteoNow();

		time = new Timestamp(new Date().getTime());

		Connection conn = null;

		//si judgeweathertime n'est pas initialisé quand on lance le programme
		if(judgeweathertime==null) {

			conn = JDBCUtil.getPostgreConn();

			Statement st = null;
			ResultSet rs = null;
			try {
				st = conn.createStatement();
				String query = "select max(weather_time) from "+  TABLE_NAME + ";";
				rs = st.executeQuery(query);

				while (rs.next()) {
					Timestamp date = rs.getTimestamp("max");
					judgeweathertime = date;
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				System.out.println(DateUtil.setDate(new Date())); 
			} finally {
				JDBCUtil.close( rs, st, conn);
			}
		}

		//si le nouveau timestamp égale à celui du judgemeteo
		//System.out.println(meteo.getDate().equals(judgeweathertime));
		//il est possible que judgeinserttime est null si la table est vide quand on lance le programme
		System.out.println("Existing timestamp: " + judgeweathertime);
		System.out.println("New timestamp: " + meteo.getDate());
		
		if(meteo!=null && ( judgeweathertime==null || meteo.getDate().after(judgeweathertime))) {

			
			conn = JDBCUtil.getPostgreConn();
			PreparedStatement ps = null;

			try {
				ps = conn.prepareStatement("insert into "+  TABLE_NAME +" (weather_time,latitude,longitude,temperature,pressure,humidity,weather_description,clouds,wind_speed,wind_direction,rain,snow,insert_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");

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
				ps.setObject(13, time,Types.TIMESTAMP);
				ps.execute();
				judgeweathertime = meteo.getDate();
				System.out.println("insert into "+  TABLE_NAME + " finished at "+DateUtil.setDate(new Date()));

			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				System.out.println(DateUtil.setDate(new Date())); 
			} finally {

				JDBCUtil.close( ps, conn);
			}
		} else {
			System.out.println("pas de nouvelle information sur le tableau " +  TABLE_NAME +" pour la position "+ po.toString()+" "+DateUtil.setDate(new Date())); 
		}


	}

}

