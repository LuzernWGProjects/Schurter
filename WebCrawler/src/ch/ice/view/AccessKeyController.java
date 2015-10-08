package ch.ice.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.apache.commons.configuration.ConfigurationException;

public class AccessKeyController implements Initializable {

	@FXML
	private TextField keyTextField;
	@FXML
	private Button cancelButton;
	@FXML
	private Button okButton;
	@FXML
	private Label keyLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// TODO Auto-generated method stub

		keyTextField.setPrefWidth(400);
		keyTextField.setMaxWidth(500);

		keyTextField.setText(GUIController.config
				.getString("searchEngine.bing.accountKey"));

		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				stage.close();
			}
		});

		okButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				GUIController.config.setProperty(
						"searchEngine.bing.accountKey", keyTextField.getText());
				try {
					GUIController.config.save();
				} catch (ConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				stage.close();

			}
		});

	}

	public static void haha() {

	}

}
