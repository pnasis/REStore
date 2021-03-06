package Code;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
//import java.lang.Math.*;

public class WeatherAPI {

	private double temperature;
	private Long humidity;
	private Double windspeedDouble = null;
	private Long windspeedLong = null;
	private String weatherDescription;
	private String weatherCondition;
	private double beaufort;
	
	public WeatherAPI()
	{
		//Our request to the API.
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://community-open-weather-map.p.rapidapi.com/weather?q=Thessaloniki%2Cgr&lat=0&lon=0&callback=(&id=2172797&lang=null&units=imperial&mode=xml"))
				.header("X-RapidAPI-Host", "community-open-weather-map.p.rapidapi.com")
				.header("X-RapidAPI-Key", "")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		//Our response from the API.
		HttpResponse<String> response;
		
		try {
			
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

			String data = response.body();
			
			//We take the preffered data from the body of the response.
			data = data.substring(1, data.length() -1);
			data = data.substring(1);			
		    JSONObject jsonObject = (JSONObject) JSONValue.parse(data);
		    //--We take temperature and humidity--//
		    JSONObject takeKeyForTemp = (JSONObject) jsonObject.get("main");

		    if(takeKeyForTemp.get("temp") instanceof Double) {
		    	temperature = (Double) takeKeyForTemp.get("temp");
		    }
		    else if (takeKeyForTemp.get("temp") instanceof Long) {
		    	temperature = (Long) takeKeyForTemp.get("temp");
		    }
		    
		    temperature = (temperature - 32) * 0.5556;
		    humidity = (Long) takeKeyForTemp.get("humidity");
		    
		    
		    //--We take beaufort--//
		    JSONObject takeKeyForWind = (JSONObject) jsonObject.get("wind");
		    if(takeKeyForWind.get("speed") instanceof Double)
		    {
		    	windspeedDouble = (Double) takeKeyForWind.get("speed");
		    	beaufort = Math.cbrt(Math.pow(windspeedDouble / 1.625, 2));
		    	windspeedLong = null;
		    }
		    else if(takeKeyForWind.get("speed") instanceof Long)
		    {
		    	windspeedLong = (Long) takeKeyForWind.get("speed");
		    	
		    	beaufort = (long) Math.cbrt(Math.pow(windspeedLong / 1.625, 2));
		    	
		    	windspeedDouble = null;
		    }
		    
		    //We take Weather description and weather condition--//
		    JSONArray weatherDetailsArray = (JSONArray) jsonObject.get("weather");
		    String temp = weatherDetailsArray.get(0).toString();

		    JSONObject weatherObject = (JSONObject) JSONValue.parse(temp);
		    
		    weatherDescription = (String) weatherObject.get("description");
		    weatherCondition = (String) weatherObject.get("main");
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} 
		
	}
	
	public double getBeaufort() {
		return beaufort;
	}

	public void setBeaufort(double beaufort) {
		this.beaufort = beaufort;
	}

	public double getTemperature() {
		return temperature;
	}

	public long getHumidity() {
		return humidity;
	}

    public Double getWindspeedDouble() {
    	return windspeedDouble;
    }

    public Long getWindspeedLong() {
    	return windspeedLong;
    }
	public String getWeatherDescription() {
		return weatherDescription;
	}

	public String getWeatherCondition() {
		return weatherCondition;
	}
	
}
