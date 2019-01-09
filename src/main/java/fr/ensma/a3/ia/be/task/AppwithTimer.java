package fr.ensma.a3.ia.be.task;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Map.Entry;

import org.aeonbits.owner.Config.Key;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.loaders.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.ensma.a3.ia.be.dto.Position;
import fr.ensma.a3.ia.be.utils.ServerConfig;

public class AppwithTimer {
    private static final Logger logger = LogManager.getLogger(AppwithTimer.class);

	public static void main(String[] args) throws Exception {
		
		validateConfig();
		
//		Position po = new Position(117.2f, 39.13f);
		Position po2 =  Position.getCurrentPosition();
		
//		new WeatherTimer (po);
		new WeatherTimer (po2);
	}
	
    private static void validateConfig() {
        final String CLASSPATH_PROTOCOL = "classpath:";

        Sources sources = ServerConfig.class.getAnnotation(Sources.class);
        for (String source : sources.value()) {
            Properties props = new Properties();
            URI uri = null;
            if (source.startsWith(CLASSPATH_PROTOCOL)) {
                String path = source.substring(CLASSPATH_PROTOCOL.length());
                try {
                    uri = ClassLoader.getSystemResource(path).toURI();
                } catch (URISyntaxException | NullPointerException e) {
                    logger.error("Could not find the configuration file " + source);
                    System.exit(-1);
                }
            } else {
                try {
                    uri = new URI(source);
                } catch (URISyntaxException e) {
                    logger.error("Could not find the configuration file " + source);
                    System.exit(-1);
                }
            }

            try {
                new PropertiesLoader().load(props, uri);
            } catch (IOException e) {
                logger.error("Could not read configuration file " + source);
                System.exit(-1);
            }

            logger.info("Listing configuration properties");
            for (Entry<Object, Object> e : props.entrySet()) {
                logger.info(e.getKey() + "=" + e.getValue());
            }

            for (Method m : ServerConfig.class.getMethods()) {
                Key key = m.getAnnotation(Key.class);
                if (!props.containsKey(key.value())) {
                    logger.error("Missing required configuration property " + key.value());
                    System.exit(-1);
                }
            }
        }
    }
	

}
