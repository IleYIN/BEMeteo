package fr.ensma.a3.ia.be.dto;


import java.util.List;


@Deprecated
public class Station {

	private final Position pos;
	private final List<Weather> listWeatherForecast;
	private final Weather currentWeather;

	public Station(final Position pos) {
		this.pos = pos;
		RecuperationWeather rw = new RecuperationWeather(pos);
		listWeatherForecast = rw.getWeatherForecast();
		currentWeather = rw.getCurrentWeather();
		
	}

	public Position getPos() {
		return pos;
	}


	public List<Weather> getListWeatherForecast() {
		return listWeatherForecast;
	}

	public Weather getCurrentWeather() {
		return currentWeather;
	}

}



