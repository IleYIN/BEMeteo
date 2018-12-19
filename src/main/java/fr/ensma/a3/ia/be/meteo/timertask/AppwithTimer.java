package fr.ensma.a3.ia.be.meteo.timertask;

import fr.ensma.a3.ia.be.meteo.Position;

public class AppwithTimer {

	public static void main(String[] args) throws Exception {
		
		Position po = new Position(0.3614f, 46.6613f);
		
		new MonTimer (po);
	}
	

}
