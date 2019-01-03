package fr.ensma.a3.ia.be.dto;


import org.aeonbits.owner.ConfigCache;
import fr.ensma.a3.ia.be.utils.ServerConfig;


public class Position {


	private final Float lon;
	private final Float lat;
	
	private static Position current;
	private static fr.ensma.a3.ia.be.utils.ServerConfig cfg = ConfigCache.getOrCreate(ServerConfig.class);

	public static Position getCurrentPosition() {
		if (current == null) {
			Float lon = cfg.poLongitude();
			Float lat = cfg.poLatitude();
			current = new Position(lon, lat);
		}
		return current;
	}
	

	public Position(Float lon, Float lat) {
		this.lon = lon;
		this.lat = lat;

	}

	public Float getLon() {
		return lon;
	}

	public Float getLat() {
		return lat;
	}
	
	@Override
	public String toString() {
		return "latitude=" +this.lat+", longitude="+this.lon;
	}
	


}