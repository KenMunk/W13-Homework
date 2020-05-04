import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * @author Kenneth Munk
 * @purpose An assignment to test out both json accessibility and git
 */

public class WxModel {
	
	private String zipCodeString, location, weather, time, temp, windSpeed, windDirection, windDirectionCardinal, pressure, humidity;
	private boolean validZip;
	
	public WxModel() {
		this.clear();
	}
	
	public WxModel(String zipCode) {
		
		this.clear();
		this.setZIP(zipCode);
		this.getWeatherData();
		
	}
	
	public void clear() {
		this.zipCodeString = "";
		this.location = "";
		this.weather = "";
		this.time = "";
		this.temp = "";
		this.windSpeed = "";
		this.windDirection = "";
		this.windDirectionCardinal = "";
		this.pressure = "";
		this.humidity = "";
		this.validZip = true;
	}
	
	public void setZIP(String zipCodeString) {
		
		String tempZIP;
		tempZIP = zipCodeString;
		
		this.zipCodeString = tempZIP;
		
	}
	
	public void setLocation(JsonElement apiReport) {
		String tempData;
		
		tempData = apiReport.getAsJsonObject().get("name").getAsString();
		
		this.location = tempData;
		
	}
	
	@SuppressWarnings("deprecation")
	public void setTime(JsonElement apiReport) {
		String tempData;
		long timeOffset;
		Date currentTime;
		int year, month, day, hours, minutes, seconds;
		
		//Getting the time offset
		timeOffset = apiReport.getAsJsonObject().get("dt").getAsLong();
		
		//getting the raw UTC time and then offsetting the time
		currentTime = new Date(timeOffset*1000);
		
		year = currentTime.getYear()+1900;
		month = currentTime.getMonth()+1;
		day = currentTime.getDate();
		hours = currentTime.getHours();
		minutes = currentTime.getMinutes();
		seconds = currentTime.getSeconds();
		
		//assemble time
		tempData = String.format("%4d-%02d-%02d %02d:%02d:%02d", year, month, day, hours, minutes, seconds);
		
		//update the time
		this.time = tempData;
	}
	
	public void setWeather(JsonElement apiReport) {
		String tempData;
		JsonArray category;
		
		category = apiReport.getAsJsonObject().get("weather").getAsJsonArray();
		tempData = category.get(0).getAsJsonObject().get("main").getAsString();
		
		this.weather = tempData;
		
	}
	
	public void setTemp(JsonElement apiReport) {
		String tempData;
		//JsonArray category;
		
		//category = apiReport.getAsJsonObject().get("main").getAsJsonArray();
		
		tempData = String.format("%01.1f",apiReport.getAsJsonObject().get("main").getAsJsonObject().get("temp").getAsFloat());
		
		this.temp = tempData;
		
	}
	
	public void setWindSpeed(JsonElement apiReport) {
		String tempData;
		JsonElement wind;
		JsonElement windSpeedNum;
		double wSpeed;
		
		wind = apiReport.getAsJsonObject().get("wind");
		windSpeedNum = wind.getAsJsonObject().get("speed");
		wSpeed = windSpeedNum.getAsDouble();
		
		tempData = String.format("%01.1f MPH",wSpeed);
		
		//System.out.printf("WindSpeed = %f", wSpeed);
		
		this.windSpeed = tempData;
		
	}
	
	private void setWindDirection(JsonElement apiReport) {
		String tempData, tempCardinal;
		//JsonArray category;
		tempData = "0";
		JsonElement wind;
		JsonElement windDirNum;
		int windDir;
		
		//category = apiReport.getAsJsonObject().get("wind").getAsJsonArray();
		wind = apiReport.getAsJsonObject().get("wind");
		windDirNum = wind.getAsJsonObject().get("deg");
		windDir = windDirNum.getAsInt();
		tempData = String.format("%d",windDir);
		
		if(!((windDir % 360 > 90)&&(windDir % 360 < 270))){
			
			tempCardinal = "N";
			
		}
		else {
			
			tempCardinal = "S";
			
		}

		if(!((windDir % 360 > 0)&&(windDir % 360 < 180))){
			
			tempCardinal = tempCardinal + "W";
			
		}
		else {
			
			tempCardinal = tempCardinal + "E";
			
		}
		
		this.windDirectionCardinal = tempCardinal;
		
		this.windDirection = tempData + " degrees";
		
	}
	
	private void setPressure(JsonElement apiReport) {
		String tempData;
		JsonElement category;
		double hPa;
		double inPa;
		
		category = apiReport.getAsJsonObject().get("main");
		
		hPa = category.getAsJsonObject().get("pressure").getAsDouble();
		inPa = hPa*0.02952998751f;
		
		tempData = String.format("%02.2f",inPa);
		
		this.pressure = tempData;
		
	}
	
	private void setHumidity(JsonElement apiReport) {
		String tempData;
		//JsonArray category;
		
		//category = apiReport.getAsJsonObject().get("main").getAsJsonArray();
		
		tempData = String.format("%01.0f%%",apiReport.getAsJsonObject().get("main").getAsJsonObject().get("humidity").getAsFloat());
		
		this.humidity = tempData;
		
	}
	
	public String get(String reference) {
		
		String tempString;
		tempString = null;
		
		switch(reference.toLowerCase()) {
		case "location":
			tempString = this.location;
			break;
		case "weather":
			tempString = this.weather;
			break;
		case "time":
			tempString = this.time;
			break;
		case "temp":
			tempString = this.temp;
			break;
		case "windSpeed":
			tempString = this.windSpeed;
			break;
		case "windDirection":
			tempString = this.windDirection;
			break;
		case "windCardinal":
			tempString = this.windDirectionCardinal;
			break;
		case "wind":
			tempString = String.format("%s %s", this.windSpeed, this.windDirectionCardinal);
			break;
		case "pressure":
			tempString = this.pressure;
			break;
		case "humidity":
			tempString = this.humidity;
			break;
		default:
			//tempString = null;
			break;
		}
		return tempString;
	}
	
	public boolean checkValidity() {
		boolean tempData;
		tempData = this.validZip;
		return tempData;
	}
	
	public void invalidQueryResponse() {
		
		String responseOut;
		
		responseOut = String.format("ERROR: No cities match your query");
		
		System.out.print(responseOut);
		
	}
	
	public String getWeatherData() {
		
		JsonElement apiReport = null;
		String weatherReport = null;

		String apiKey;
		apiKey = "ee344ce83dd7b5e66288b1c6ebaf95c7";
		
		try {
			
			//Build the URL
			URL weatherURL = new URL("http://api.openweathermap.org/data/2.5/weather?zip="
		 				   + this.zipCodeString
		 				   + "&units=imperial&appid=" 
		 				   + apiKey);
			
			//Open the URL
			InputStream weatherDataStream = weatherURL.openStream();
			BufferedReader weatherReaderBuffer = new BufferedReader(new InputStreamReader(weatherDataStream));
			
			//Reading the results as a json object
			apiReport = new JsonParser().parse(weatherReaderBuffer);
			
			//end the connection
			weatherDataStream.close();
			weatherReaderBuffer.close();
		
		} 
		catch (java.net.MalformedURLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		catch(java.io.UnsupportedEncodingException unsupportedencoding) {
			//unsupportedencoding.printStackTrace();
		}
		catch(java.io.IOException badIO) {
			//badIO.printStackTrace();
		}
		catch(Exception everythingElse) {
			//everythingElse.printStackTrace();
		}
		
		if(apiReport != null) {
			
			this.setLocation(apiReport);
			this.setWeather(apiReport);
			this.setTemp(apiReport);
			this.setWindDirection(apiReport);
			this.setWindSpeed(apiReport);
			this.setPressure(apiReport);
			this.setTime(apiReport);
			this.setHumidity(apiReport);
			this.validZip = true;
			
			weatherReport = String.format("%-15s %-20s\n", "Location:", this.location)
										 + String.format("%-15s %-20s\n", "Time:", this.time)
										 + String.format("%-15s %-20s\n", "Weather:", this.weather)
										 + String.format("%-15s %-20s\n", "Temperature F:", this.temp)
										 + String.format("%-15s %-20s\n", "Wind:", this.windSpeed)
										 + String.format("%-15s %-20s\n", "Wind direction:", this.windDirection)
										 + String.format("%-15s %-20s\n", "Pressure inHG:", this.pressure);
			
		}
		else {
			this.validZip = false;
			String invalid = "--";
			this.location = invalid;
			this.weather = invalid;
			this.time = "NEVER";
			this.temp = invalid;
			this.windSpeed = invalid;
			this.windDirection = invalid;
			this.pressure = invalid;
			this.humidity = invalid;
			
		}
		
		return weatherReport;
	}
	
	public static void main(String[] args) {
		
		WxModel report;
		String outputString;
		
		report = new WxModel();
		report.setZIP(args[0]);
		//report.setZIP("95678");
		outputString = report.getWeatherData();
		
		if(outputString == null) {
			
			report.invalidQueryResponse();
			
		}
		else {
			
			System.out.print(outputString);
			
		}
		
	}
	
}
