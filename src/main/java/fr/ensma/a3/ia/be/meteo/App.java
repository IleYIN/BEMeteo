package fr.ensma.a3.ia.be.meteo;

//import fr.ensma.a3.ia.be.meteo.versionwithTimer.MonTimer;
@Deprecated
public class App {

	public static void main(String[] args) throws Exception {
		
		Position po = new Position(0.3614f, 46.6613f);
		
		InsertMeteoNow innow = new InsertMeteoNow(po,"weather_now",1800000);
		Thread th1 = new Thread(innow,"weather_now_thread");
		

		InsertMeteoForecast infore = new InsertMeteoForecast(po,"weather_forecast",1800000);
		Thread th2 = new Thread(infore,"weather_forecast_thread");
		
		th1.start();
		th2.start();
		
		Thread.sleep(10000);
		
		//another test with Timer
//		new MonTimer(po);
	}
	

}
