package fr.ensma.a3.ia.be.dto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe permettant de recuperer le contenu json fournie par API meteo pour une position donnée
 * 
 */
public class RecuperationWeather {



	private static final Logger logger = LogManager.getLogger(RecuperationWeather.class);
	private final Position position;
	private URL weatherReportService;
	private URL weatherForecastService;

	public RecuperationWeather(final Position position) {

		this.position = position;
		try {
			weatherReportService = new URL("http://api.openweathermap.org/data/2.5/weather?lat="+ position.getLat() + "&lon=" + position.getLon()
			//			+ "&APPID=504f811685be1b6cb9c2787452991b43";
			+ "&APPID=efd271640f4b66ea5359340383b9a4f7");
			weatherForecastService = new URL("http://api.openweathermap.org/data/2.5/forecast?lat="+ position.getLat() + "&lon=" + position.getLon()
			//			+ "&APPID=504f811685be1b6cb9c2787452991b43";
			+ "&APPID=efd271640f4b66ea5359340383b9a4f7");
		} catch (MalformedURLException e) {
			logger.error("erreur URL",e);
			System.exit(-1);
		}

	}


	/**
	 * Méthode qui permet de retourner au format string le contenu (ie la trame météo au format Json)  à l'URL adresse
	 * 
	 * @param adresse
	 * 
	 * @return toreturn
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 */



	public Weather getCurrentWeather() {

		String jsonData = getJsonDataFromURL(weatherReportService);

		if (jsonData != null) {

			//Reader qui parcourt la trame Json
			JsonReader reader = Json.createReader(new StringReader(jsonData));
			JsonObject weatherObject = reader.readObject();
			reader.close();

			return getWeatherFromJsonObj(weatherObject);
		} 
		return null;
	}


	public List<Weather> getWeatherForecast() {

		String jsonData = getJsonDataFromURL(weatherForecastService);

		if (jsonData != null) {

			List<Weather> listForecast = new ArrayList<Weather>();

			//Reader qui parcourt la trame Json
			JsonReader reader = Json.createReader(new StringReader(jsonData));
			JsonObject weatherObject = reader.readObject();
			reader.close();

			try {
				JsonArray jsonlist = weatherObject.getJsonArray("list");
				for (int n_3h = 0; n_3h < jsonlist.size(); n_3h++) {
					
					JsonObject listObj = (JsonObject)jsonlist.get(n_3h);
					listForecast.add(getWeatherFromJsonObj(listObj));
				}
			} catch (Exception e) {
				logger.error("could not get weather forecast from json data");
			}
			return listForecast;
		} 
		return null;
	}



	private String getJsonDataFromURL(URL service)  { 


		try {

			URLConnection uc = service.openConnection(); 
			InputStream in = uc.getInputStream(); 
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			
			//On crée un StringBuilder pour par la suite y ajouter tout les bits lus 
			StringBuilder build = new StringBuilder(); 

			char[] flush = new char[1024];
			int len = 0; 
			//Tant que c n'est pas égal au bit indiquant la fin d'un flux... 
			while ((len = br.read(flush)) != -1) { 
				//...on l'ajoute dans le StringBuilder... 
				build.append(flush,0,len); 
			} 

			//On retourne le code de la page 
			return build.toString(); 

		} catch (IOException e) { 
			logger.error("could not get json data from URL service",e);
		}
		return null;

	}


	private Weather getWeatherFromJsonObj(JsonObject jsonObj) {

		Timestamp date = null;
		Float lat = position.getLat();
		Float lon = position.getLon();
		Float temperature = null;
		Float pressure = null;
		Float humidity = null;
		String weatherdescription = null;
		Float clouds = null;
		Float wind_speed = null;
		Float wind_direction = null;
		Float rain = null;
		Float snow = null;

		try {
			//Unix Timestamp to Java Timestamp
			long unixSeconds = (long)jsonObj.getJsonNumber("dt").doubleValue();
			date = new Timestamp(unixSeconds*1000L);
			//System.out.println("weathertimelong_"+unixSeconds);
			//System.out.println("weathertimejson_"+date);
			
			
		} catch (Exception e) {
			logger.error("could not get weather_time from Json, weather_time can't be null");
			return null;
		}

		try {
			JsonArray weather = jsonObj.getJsonArray("weather");
			JsonObject weatherObj = (JsonObject)weather.get(0);
			weatherdescription =  weatherObj.getString("description");
		} catch (Exception e) {
			// ignore errors
		}
		try {
			temperature = (float) jsonObj.getJsonObject("main").getJsonNumber("temp").doubleValue();
		} catch (Exception e) {
			// ignore errors
		}
		try {
			pressure = (float) jsonObj.getJsonObject("main").getJsonNumber("pressure").doubleValue();
		} catch (Exception e) {
			// ignore errors
		}
		try {
			humidity = (float) jsonObj.getJsonObject("main").getJsonNumber("humidity").doubleValue();
		} catch (Exception e) {
			// ignore errors
		}
		try {
			clouds = (float) jsonObj.getJsonObject("clouds").getJsonNumber("all").doubleValue();
		} catch (Exception e) {
			// ignore errors
		}
		try {
			wind_speed = (float)jsonObj.getJsonObject("wind").getJsonNumber("speed").doubleValue();
		} catch (Exception e) {
			// ignore errors
		}

		try {
			rain = (float) jsonObj.getJsonObject("rain").getJsonNumber("3h").doubleValue();
		} catch (Exception e) {
			// ignore errors
		}
		try {
			snow = (float) jsonObj.getJsonObject("snow").getJsonNumber("3h").doubleValue();
		} catch (Exception e) {
			// ignore errors
		}
		try {
			wind_direction = ((float)jsonObj.getJsonObject("wind").getJsonNumber("deg").doubleValue());
		} catch (Exception e) {
			// ignore errors
		}

		Weather weather = new Weather(date,lat,lon,temperature,pressure,humidity,weatherdescription,clouds,wind_speed,wind_direction,rain,snow);
		return weather;

	}


}

