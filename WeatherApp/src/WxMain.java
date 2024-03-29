
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WxMain extends Application{
	
	@Override
	public void start(Stage stage) throws Exception{
		
		Parent root = FXMLLoader.load(getClass().getResource("WxView.fxml"));
		
		Scene scene = new Scene(root,600,400);
		stage.setTitle("Wx - Kenneth Munk");
		stage.setScene(scene);
		stage.show();
		
	}
	
	public static void main(String[] args) {
		
		launch(args);
		
	}
	
}
