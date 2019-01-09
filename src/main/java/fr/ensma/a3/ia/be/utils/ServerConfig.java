package fr.ensma.a3.ia.be.utils;


import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;


@Sources("classpath:weather.properties")
public interface ServerConfig extends Config {

	//@Key("postgresql.driver")
	//String pgDriver();

	@Key("postgresql.ip")
	String pgAddress();

	@Key("postgresql.port")
	Integer pgPort();

	@Key("postgresql.db")
	String pgDatabase();

	@Key("postgresql.user")
	String pgUser();

	@Key("postgresql.password")
	String pgPassword();

	@Key("postgresql.tablename.weatherReport")
	String pgTableWeatherReport();

	@Key("postgresql.tablename.weatherForecast")
	String pgTableWeatherForecast();

	@Key("position.latitude")
	Float poLatitude();

	@Key("position.longitude")
	Float poLongitude();


}
