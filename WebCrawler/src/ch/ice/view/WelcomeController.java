package ch.ice.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

public class WelcomeController implements Initializable {

	@FXML
	private Label javaVersion;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		javaVersion.setText(System.getProperty("java.version"));

		String javaVer = System.getProperty("java.version");

		if (Integer.parseInt(javaVer.substring(2, 3)) < 8) {
			showAlert();

		} else {
			if (Integer.parseInt(javaVer.substring(6)) < 8) {
				showAlert();
			}

		}

	}

	private static void showAlert() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Java Version");
		alert.setHeaderText("Incompatible Java Version");
		alert.setContentText("Please update your Java version to 1.8.0_45 or higher");
		alert.showAndWait();
		System.exit(0);
	}
}
