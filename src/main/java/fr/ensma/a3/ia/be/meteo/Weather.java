package fr.ensma.a3.ia.be.meteo;

import java.sql.Timestamp;

public class Weather {

//	private Date date;
	private Timestamp date;
	private Float lat;
	private Float lon;
	
	private Float temperature;
	private Float pressure;
	private Float humidity;
	
	private String weatherdescription;
	
	public Timestamp getDate() {
		return date;
	}


	public void setDate(Timestamp date) {
		this.date = date;
	}



	private Float clouds;
	private Float wind_speed;
	private Float wind_direction;
	
	
	private Float rain;
	private Float snow;
	
	
	
	public Weather() {}
	
	
	public Float getRain() {
		return rain;
	}


	public void setRain(Float rain) {
		this.rain = rain;
	}


	public Float getSnow() {
		return snow;
	}


	public void setSnow(Float snow) {
		this.snow = snow;
	}


//	public Date getDate() {
//		return date;
//	}
//
//	public void setDate(Date date) {
//		this.date = date;
//	}

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	public Float getPressure() {
		return pressure;
	}

	public void setPressure(Float pressure) {
		this.pressure = pressure;
	}

	public Float getHumidity() {
		return humidity;
	}

	public void setHumidity(Float humidity) {
		this.humidity = humidity;
	}

	public String getWeatherdescription() {
		return weatherdescription;
	}

	public void setWeatherdescription(String weatherdescription) {
		this.weatherdescription = weatherdescription;
	}

	public Float getClouds() {
		return clouds;
	}

	public void setClouds(Float clouds) {
		this.clouds = clouds;
	}

	public Float getWind_speed() {
		return wind_speed;
	}

	public void setWind_speed(Float wind_speed) {
		this.wind_speed = wind_speed;
	}

	public Float getWind_direction() {
		return wind_direction;
	}


	public Float getLat() {
		return lat;
	}


	public void setLat(Float lat) {
		this.lat = lat;
	}


	public Float getLon() {
		return lon;
	}


	public void setLon(Float lon) {
		this.lon = lon;
	}


	

	public void setWind_direction(Float wind_direction) {
		this.wind_direction = wind_direction;
	}



	@Override
	public String toString() {
		return "Weather [ latitude=" +lat + ", longitude=" + lon+ ", date=" + date
				+  ", WeatherDescription=" + weatherdescription
				+ ", temperature=" + temperature + ", pressure=" + pressure+ ", humidity=" + humidity 
				+", wind_speed=" + wind_speed+ ", wind_direction=" + wind_direction + ", cloud=" + clouds
				+ ", rain in 3h(mm)="+ rain + ", snow in 3h(mm)="+snow + " ]";
	}
	


}
