package fr.ensma.a3.ia.be.meteo;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import fr.ensma.a3.ia.be.utils.DateUtil;



/**
 * Classe permettant de recuperer le contenu json fournie par API meteo pour une position donnée
 * 
 */
public class RecuperationMeteo {

	private Position po;
	private String[] jsonData;


	public RecuperationMeteo() {

	}

	public RecuperationMeteo(Position po) {
		this.po = po;
		jsonData = new String[2];// 0 releve meteo actuel ;1 prevision meteo
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


	/**
	 * Utilizer ajoutJsonData, connecter à URL
	 */
	public void remplirForecastMeteo() {

		ajoutJsonData(1);
		List<Weather> listmeteofore = new ArrayList<Weather>();
		for(int n=0;n<40;n++) {
			Weather meteofore = getContenuMeteoForecast(n);
			if(meteofore!=null) {
				listmeteofore.add(meteofore);
			}
		}
		po.setListmeteoForecast(listmeteofore);
	}

	/**
	 * Utilizer ajoutJsonData, connecter à URL
	 */
	public void remplirReleveMeteoActuel() {
		ajoutJsonData(0);
		Weather meteonow = getContenuMeteoActuel();
		if(meteonow!=null) {
			po.setMeteoNow(meteonow);
		}
	}





	private String genereAdresse(int i) {
		String adresse = null;
		switch (i) {
		case 0:// 0 releve meteo
			adresse ="http://api.openweathermap.org/data/2.5/weather?lat="+ po.getLat() + "&lon=" + po.getLon()
			//			+ "&APPID=504f811685be1b6cb9c2787452991b43";
			+ "&APPID=efd271640f4b66ea5359340383b9a4f7";

			break;
		case 1://1 prevision meteo
			adresse ="http://api.openweathermap.org/data/2.5/forecast?lat="+ po.getLat() + "&lon=" + po.getLon()
			//			+ "&APPID=504f811685be1b6cb9c2787452991b43";
			+ "&APPID=efd271640f4b66ea5359340383b9a4f7";
		}
		return adresse;
	}


	private void ajoutJsonData(int i) { 


		String adresse = genereAdresse(i);



		try { 
			URL url = new URL(adresse); 

			URLConnection uc = url.openConnection(); 

			//On y crée un flux de lecture 
			InputStream in = uc.getInputStream(); 

			//On lit le premier bit 
			int c = in.read(); 

			//On crée un StringBuilder pour par la suite y ajouter tout les bits lus 
			StringBuilder build = new StringBuilder(); 

			//Tant que c n'est pas égal au bit indiquant la fin d'un flux... 
			while (c != -1) { 
				//...on l'ajoute dans le StringBuilder... 
				build.append((char) c); 
				//...on lit le suivant 
				c = in.read(); 
			} 

			//On retourne le code de la page 
			this.jsonData[i] = build.toString(); 

		} catch (MalformedURLException e) { 	 
			System.out.println(e.getMessage()+" URL ne répond pas ");
			System.out.println(DateUtil.setDate(new Date())); 
		} catch (IOException e) { 
			System.out.println(e.getMessage()+" StringBuilder ne peut pas lire ");
			//e.printStackTrace();
			System.out.println(DateUtil.setDate(new Date())); 
		}

	}
	private Weather getContenuFromJsonObj(Weather meteo, JsonObject JsonObj) {

		try {

			//Unix Timestamp to Java Timestamp
			long unixSeconds = (long)JsonObj.getJsonNumber("dt").doubleValue();
			Timestamp date = new Timestamp(unixSeconds*1000L);
			//			String formattedDate = DateUtil.setDate(date);

			meteo.setDate(date);

		} catch (Exception e) {
			System.out.println("erreur pour récupérer le temp de météo");
			System.out.println(DateUtil.setDate(new Date())); 
			return null;//date - primary key non null
		}

		try {

			JsonArray weather = JsonObj.getJsonArray("weather");
			JsonObject weatherObj = (JsonObject)weather.get(0);
			meteo.setWeatherdescription(weatherObj.getString("description"));
		} catch (Exception e) {
			meteo.setWeatherdescription(null);
		}
		try {
			meteo.setTemperature((float) JsonObj.getJsonObject("main").getJsonNumber("temp").doubleValue());
		} catch (Exception e) {
			meteo.setTemperature(null);
		}
		try {
			meteo.setPressure((float) JsonObj.getJsonObject("main").getJsonNumber("pressure").doubleValue());
		} catch (Exception e) {
			meteo.setPressure(null);
		}
		try {
			meteo.setHumidity((float) JsonObj.getJsonObject("main").getJsonNumber("humidity").doubleValue());
		} catch (Exception e) {
			meteo.setHumidity(null);
		}
		try {
			meteo.setClouds((float) JsonObj.getJsonObject("clouds").getJsonNumber("all").doubleValue());
		} catch (Exception e) {
			meteo.setClouds(null);
		}
		try {
			meteo.setWind_speed((float)JsonObj.getJsonObject("wind").getJsonNumber("speed").doubleValue());
		} catch (Exception e) {
			meteo.setWind_speed(null);
		}


		try {
			meteo.setRain((float) JsonObj.getJsonObject("rain").getJsonNumber("3h").doubleValue());
		} catch (Exception e) {
			meteo.setRain(null);
		}
		try {
			meteo.setSnow((float) JsonObj.getJsonObject("snow").getJsonNumber("3h").doubleValue());
		} catch (Exception e) {
			meteo.setSnow(null);	
		}
		try {
			// ! récupération direction car n'est pas toujours présente
			meteo.setWind_direction((float)JsonObj.getJsonObject("wind").getJsonNumber("deg").doubleValue());
		} catch (Exception e) {
			//					e.printStackTrace();
			meteo.setWind_direction(null);
		}
		return meteo; 
	}


	private Weather getContenuMeteoActuel() {

		Weather meteo = new Weather();
		meteo.setLat(po.getLat());
		meteo.setLon(po.getLon());

		String contenu = this.jsonData[0];
		if (contenu != null) {

			//Reader qui parcourt la trame Json
			JsonReader reader = Json.createReader(new StringReader(contenu));
			JsonObject meteoObject = reader.readObject();
			reader.close();

			return getContenuFromJsonObj(meteo, meteoObject);
		} 
		return null;

	}


	private Weather getContenuMeteoForecast(int n_3h) {

		Weather meteo = new Weather();
		meteo.setLat(po.getLat());
		meteo.setLon(po.getLon());

		String contenu = this.jsonData[1];
		//Reader qui parcourt la trame Json
		if (contenu!=null) {
			JsonReader reader = Json.createReader(new StringReader(contenu));
			JsonObject meteoObject = reader.readObject();
			reader.close();


			JsonArray list = meteoObject.getJsonArray("list");
			if ((n_3h) < list.size()) {

				JsonObject listObj = (JsonObject)list.get(n_3h);
				//		System.out.println(listObj);

				return getContenuFromJsonObj(meteo,listObj);
			}
		} 
		return null;


	}


}
