package fr.ensma.a3.ia.be.meteo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import fr.ensma.a3.ia.be.utils.DateUtil;
import fr.ensma.a3.ia.be.utils.JDBCUtil;
@Deprecated
public class InsertMeteoForecast implements Runnable{
	
	
	private Position po;
	private long periode;
	private String tablename;
	private Date time;

	public InsertMeteoForecast() {
	}
	public InsertMeteoForecast(Position po, String tab, long per) {
		this.po = po;
		this.tablename = tab;
		this.periode = per;
	}
	
	
	public void run() {
		while(true) {
			try {
				
				System.out.println(Thread.currentThread().getName()+" will start in 10 seconds");
				Thread.sleep(10000);
				
				insertForecastMeteo(po, tablename);
				System.out.println("insert into "+ tablename+ " finished at "+DateUtil.setDate(new Date()));
				Thread.sleep(periode-10000);
				

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




	private void insertForecastMeteo(Position po, String tablename) {
		
		RecuperationMeteo rm = new RecuperationMeteo(po);
		
		rm.remplirForecastMeteo();
		time = new Date();
//		String inserttime = DateUtil.setDate(time) ;
		
		List<Weather> result = po.getListmeteoForecast();


		if (!result.isEmpty()) {
			
			Connection conn = null;
			PreparedStatement ps = null;
			//		ResultSet rs = null;

			try {
				for(Weather meteo : result) {

					//				System.out.println(meteo);
					conn = JDBCUtil.getPostgreConn();
					ps = conn.prepareStatement("insert into "+ tablename+" (weather_time,latitude,longitude,temperature,pressure,humidity,weather_description,clouds,wind_speed,wind_direction,rain,snow,insert_time) values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
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

				}
			} catch (Exception e) {
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
