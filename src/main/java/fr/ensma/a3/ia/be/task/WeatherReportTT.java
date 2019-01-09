package fr.ensma.a3.ia.be.task;

import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.ensma.a3.ia.be.dao.WeatherReportDAO;
import fr.ensma.a3.ia.be.dto.Position;
import fr.ensma.a3.ia.be.dto.RecuperationWeather;
import fr.ensma.a3.ia.be.dto.Weather;

public class WeatherReportTT extends TimerTask{

	private static final Logger logger = LogManager.getLogger(WeatherReportTT.class);
	private final RecuperationWeather service;
	private final WeatherReportDAO dao;

	public WeatherReportTT(final Position position) {
		service = new RecuperationWeather(position);
		dao = new WeatherReportDAO(position);
	}


	public void run() {
		// should be run periodically
		// retrieve current weather report

		Weather report = null;

		logger.debug("Getting current weather...");
		report = service.getCurrentWeather();

		if (report == null) {

			logger.error("Current weather data is null, retry to get current weather...");
			report = service.getCurrentWeather();

			if (report == null) {
				logger.error("Current weather data is null");
				return;
			}
		}

		logger.debug("Adding new weather report...");
		dao.addWeatherReport(report);

	}

}

