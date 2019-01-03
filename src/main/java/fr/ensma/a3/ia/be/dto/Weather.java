package fr.ensma.a3.ia.be.dto;

import java.sql.Timestamp;

public class Weather {

	private final Timestamp date;
	private final Float lat;
	private final Float lon;
	private final Float temperature;
	private final Float pressure;
	private final Float humidity;
	private final String weatherdescription;
	private final Float clouds;
	private final Float wind_speed;
	private final Float wind_direction;
	private final Float rain;
	private final Float snow;
	

	public Weather(Timestamp date, Float lat, Float lon, Float temperature, Float pressure, Float humidity,
			String weatherdescription, Float clouds, Float wind_speed, Float wind_direction, Float rain, Float snow) {
		super();
		this.date = date;
		this.lat = lat;
		this.lon = lon;
		this.temperature = temperature;
		this.pressure = pressure;
		this.humidity = humidity;
		this.weatherdescription = weatherdescription;
		this.clouds = clouds;
		this.wind_speed = wind_speed;
		this.wind_direction = wind_direction;
		this.rain = rain;
		this.snow = snow;
	}

	public Timestamp getDate() {
		return date;
	}

	public Float getLat() {
		return lat;
	}


	public Float getLon() {
		return lon;
	}


	public Float getTemperature() {
		return temperature;
	}


	public Float getPressure() {
		return pressure;
	}

	public Float getHumidity() {
		return humidity;
	}


	public String getWeatherdescription() {
		return weatherdescription;
	}


	public Float getClouds() {
		return clouds;
	}


	public Float getWind_speed() {
		return wind_speed;
	}


	public Float getWind_direction() {
		return wind_direction;
	}


	public Float getRain() {
		return rain;
	}

	public Float getSnow() {
		return snow;
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
