package fr.ensma.a3.ia.be.meteo.timertask;

import java.util.Timer;

import fr.ensma.a3.ia.be.meteo.Position;

public class MonTimer {
	private Timer monTimer;
	
	public MonTimer(Position po){
		
		monTimer = new Timer();
		
		InsertMeteoNowPeriodique  tch1 = new InsertMeteoNowPeriodique (po,"weather_now_tache");
		
		
		
		InsertMeteoForecastPeriodique  tch2 = new InsertMeteoForecastPeriodique (po,"weather_forecast","weather_forecast_tache",60000l);
		monTimer.scheduleAtFixedRate(tch1, 0l, 5000l);
		monTimer.scheduleAtFixedRate(tch2, 10000l, 12000l);
		
		
		
		
//		InsertMeteoForecastPeriodique  tch2 = new InsertMeteoForecastPeriodique (po,"weather_forecast","weather_forecast_tache",86400000l);
//		monTimer.scheduleAtFixedRate(tch1, 0l, 900000l);
//		monTimer.scheduleAtFixedRate(tch2, 10000l, 86400000l);
		
		
		
//		try {
//			Thread.sleep(20000);
//			tch2.cancel();
//			Thread.sleep(10000);
//			tch1.cancel();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("===========Prog FINI============");
//		monTimer.cancel();
	}
}
