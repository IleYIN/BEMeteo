package fr.ensma.a3.ia.be.meteo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import fr.ensma.a3.ia.be.utils.DateUtil;
import fr.ensma.a3.ia.be.utils.JDBCUtil;
@Deprecated
public class InsertMeteoNow implements Runnable{
	
	
	private Position po;
	private long periode;
	private String tablename;
	private Date time;

	public InsertMeteoNow() {
	}
	public InsertMeteoNow(Position po, String tab, long per) {
		this.po = po;
		this.tablename = tab;
		this.periode = per;
	}
	
	
	public void run() {
		while(true) {
			try {
	
				System.out.println(Thread.currentThread().getName()+ " started");
				insertMeteo(po, tablename);
				System.out.println("insert into "+ tablename+ " finished at "+DateUtil.setDate(new Date()));
				Thread.sleep(periode);
				

			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println(DateUtil.setDate(new Date())); 
			}
		}
	}



	//	public static long utcToTimestamp(Date dataTime) throws ParseException {
	//
	//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	//		Date date = sdf.parse(dataTime.toString());
	//		return date.getTime();
	//	}



	private void insertMeteo(Position po, String tablename) {
		
		RecuperationMeteo rm = new RecuperationMeteo(po);
		
		rm.remplirReleveMeteoActuel();

		Weather meteo = po.getMeteoNow();
		
		time = new Date();
		
		if(meteo!=null) {
			
			Connection conn = null;
			PreparedStatement ps = null;
			conn = JDBCUtil.getPostgreConn();
			try {
				ps = conn.prepareStatement("insert into "+ tablename+" (weather_time,latitude,longitude,temperature,pressure,humidity,weather_description,clouds,wind_speed,wind_direction,rain,snow,insert_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
				ps.setObject(1, meteo.getDate(), Types.TIMESTAMP);
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
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				System.out.println(DateUtil.setDate(new Date())); 
			} finally {
				
				JDBCUtil.close(ps, conn);
			}
			
		} else {
			System.out.println("pas de nouvelle information sur le tableau " + tablename+" pour la position "+ po.toString());
			System.out.println(DateUtil.setDate(new Date())); 
		}
		

	}



}
