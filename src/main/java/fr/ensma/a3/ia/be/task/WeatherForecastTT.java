package fr.ensma.a3.ia.be.task;

import java.util.List;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.ensma.a3.ia.be.dao.WeatherForecastDAO;
import fr.ensma.a3.ia.be.dto.Position;
import fr.ensma.a3.ia.be.dto.RecuperationWeather;
import fr.ensma.a3.ia.be.dto.Weather;

public class WeatherForecastTT extends TimerTask{
	
	private static final Logger logger = LogManager.getLogger(WeatherForecastTT.class);
	private final RecuperationWeather service;
	private final WeatherForecastDAO dao;

	public WeatherForecastTT(final Position pos) {
		service = new RecuperationWeather(pos);
		dao = new WeatherForecastDAO(pos);
	}


	public void run() {
		// should be run periodically
		// retrieve current weather report

		List<Weather> forecast = null;

		logger.debug("Getting weather forecast...");
		forecast = service.getWeatherForecast();

		if (forecast == null || forecast.isEmpty()) {

			logger.error("Weather forecast is null or empty, retry to get weather forecast in 1 min...");
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				//ignore error
			}
			forecast = service.getWeatherForecast();

			if (forecast == null || forecast.isEmpty()) {
				logger.error("Weather forecast is null or empty");
				return;
			}
		}

		logger.debug("Adding new weather forecast...");
		dao.addWeatherForecast(forecast);

	}

}
