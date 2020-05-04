
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WxController implements Initializable{
	
	@FXML
	private TextField zipField;
	
	@FXML
	private Button checkWeather;
	
	@FXML
	private ImageView weatherIcon;
	
	@FXML
	private Label cityName;
	
	@FXML
	private Label weather;
	
	@FXML
	private Label temperature;
	
	@FXML
	private Label windSpeedNDirection;
	
	@FXML
	private Label time;
	
	@FXML
	private Label pressure;
	
	@FXML
	private Label humidity;
	
	@FXML
	private void handleButtonAction(ActionEvent e) {
		
		WxModel weatherReport;
		Image representation;
		String[] weatherTokens;
		
		if(e.getSource() == checkWeather) {
			
			//This is there I create a new Wxhw object with the input zip code and 
			//populate all the outputs
			cityName.setText(formatReport("City", zipField.getText()));
			weatherReport = new WxModel(zipField.getText());
						
			
			cityName.setText(formatReport("City", weatherReport.get("Location")));
			weather.setText(formatReport("Weather", weatherReport.get("weather")));
			temperature.setText(formatReport("Temperature (F)", weatherReport.get("temp")));
			windSpeedNDirection.setText(formatReport("Wind (MPH)", weatherReport.get("wind")));
			time.setText(formatReport("Last Observed", weatherReport.get("time")));
			pressure.setText(formatReport("Pressure (inHG)", weatherReport.get("pressure")));
			humidity.setText(formatReport("Humidity", weatherReport.get("Humidity")));			

			
			if(!weatherReport.checkValidity()) {
				cityName.setText("City :: Invalid ZIP");
				representation = new Image("resources/badzipcode.png");
				representation = new Image("resources/RainyIcon.png");
				representation = new Image("resources/badzipcode.png");
			}
			else {
				
				weatherTokens = weatherReport.get("weather").split(" ");
				
				if(weatherTokens[0].equals("Partly") || weatherTokens[0].equals("Partially")) {
					
					switch(weatherTokens[weatherTokens.length -1]) {
					case "Rainy":
						representation = new Image("resources/RainyIcon.png");
						break;
					case "Sunny":
					case "Cloudy":
					default:
						representation = new Image("resources/PartlySunnyCloudy.png");
						break;
					}
					
				}
				else{
					
					switch((weatherTokens[weatherTokens.length - 1]).toLowerCase()) {
					case "rainy":
					case "rain":
					case "showers":
					case "storms":
					case "mist":
					case "storm":
						representation = new Image("resources/RainyIcon.png");
						break;
					case "thunderstorm":
					case "thunderstorms":
						representation = new Image("resources/ThunderStormIcon.png");
						break;
					case "sunny":
					case "clear":
						representation = new Image("resources/SunIcon.png");
						break;
					case "overcast":
					case "cloudy":
					case "clouds":
					case "foggy":
						representation = new Image("resources/CloudyIcon.png");
						break;
					case "blizzard":
					case "sleet":
					case "snow":
						representation = new Image("resources/SnowyIcon.png");
						break;
					case "meteor":
					default:
						representation = new Image("resources/badzipcode.png");
						break;
					}
					
				}
				

				if(!weatherReport.checkValidity()) {
					cityName.setText("City :: Invalid ZIP");
					representation = new Image("resources/badzipcode.png");
					representation = new Image("resources/RainyIcon.png");
					representation = new Image("resources/badzipcode.png");
				}
				
				
			}

			while(representation.getProgress() != 1) {
				
			}

			weatherIcon.setImage(representation);
			weatherIcon.setImage(representation);
			weatherIcon.setImage(representation);
		}
		
	}
	
	private String formatReport(String category, String report) {
		
		return String.format("%s :: %s", category, report);
		
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		WxModel weatherReport;
		
		//This is there I create a new Wxhw object with the input zip code and 
		//populate all the outputs
		weatherReport = new WxModel();
		
		cityName.setText(formatReport("City", weatherReport.get("Location")));
		weather.setText(formatReport("Weather", weatherReport.get("weather")));
		temperature.setText(formatReport("Temperature (F)", weatherReport.get("temp")));
		windSpeedNDirection.setText(formatReport("Wind (MPH)", weatherReport.get("wind")));
		time.setText(formatReport("Last Observed", weatherReport.get("time")));
		pressure.setText(formatReport("Pressure (inHG)", weatherReport.get("pressure")));
		humidity.setText(formatReport("Humidity", weatherReport.get("Humidity")));
		
	}
	
	
}
