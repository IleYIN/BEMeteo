# Acquisition de données météorologiques

This project aimes to collect information of current weather and weather forecast through openweathermap api.
You may create a new configuration file referring to "weather.properties.sample" and "log4j2.xml.sample" in "src/main/resources" so that the weather information for a specific position could be saved in the databases.
This project will request current weather in every 15min and weather forecast in every 2h, openweathermap updates current weather information every 30min and weather forecast information every 3h for a new forecast time and every 24h at about 9am for the same forecast time.
