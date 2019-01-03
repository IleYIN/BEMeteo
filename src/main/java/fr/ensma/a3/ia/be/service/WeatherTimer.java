package fr.ensma.a3.ia.be.service;

import java.util.Timer;

import fr.ensma.a3.ia.be.dto.Position;

public class WeatherTimer {
	private Timer monTimer;
	
	public WeatherTimer(Position po){
		
		monTimer = new Timer();
		
		WeatherReportTT  tch1 = new WeatherReportTT(po);
		WeatherForecastTT  tch2 = new WeatherForecastTT (po);
		
//		monTimer.scheduleAtFixedRate(tch1, 0l, 5000l);
//		monTimer.scheduleAtFixedRate(tch2, 10000l, 12000l);
		
		monTimer.scheduleAtFixedRate(tch1, 0l, 900000l);
		monTimer.scheduleAtFixedRate(tch2, 10000l,(1000*3600*24));
	}
}
