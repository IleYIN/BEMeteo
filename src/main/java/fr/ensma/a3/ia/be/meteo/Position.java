package fr.ensma.a3.ia.be.meteo;

import java.util.List;


public class Position {


	private Float lon;
	private Float lat;
	private List<Weather> listmeteoForecast;
	private Weather meteoNow;


	public Position() {
//		listmeteoForecast = new ArrayList<Weather>();
		

	}

	public Position(Float lon, Float lat) {
		this();
		this.lon = lon;
		this.lat = lat;

	}



	public Float getLon() {
		return lon;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}


	public Float getLat() {
		return lat;
	}


	public void setLat(Float lat) {
		this.lat = lat;
	}

	public List<Weather> getListmeteoForecast() {
		return listmeteoForecast;
	}

	public void setListmeteoForecast(List<Weather> listmeteoForecast) {
		this.listmeteoForecast = listmeteoForecast;
	}

	public Weather getMeteoNow() {
		return meteoNow;
	}

	public void setMeteoNow(Weather meteoNow) {
		this.meteoNow = meteoNow;
	}

	
	@Override
	public String toString() {
		return "latitude=" +this.lat+", longitude="+this.lon;
	}
	


}