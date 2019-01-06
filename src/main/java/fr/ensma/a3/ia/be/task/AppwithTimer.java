package fr.ensma.a3.ia.be.task;

import fr.ensma.a3.ia.be.dto.Position;

public class AppwithTimer {

	public static void main(String[] args) throws Exception {
		
		Position po = new Position(117.2f, 39.13f);
		Position po2 =  Position.getCurrentPosition();
		
		new WeatherTimer (po);
		new WeatherTimer (po2);
	}
	

}
