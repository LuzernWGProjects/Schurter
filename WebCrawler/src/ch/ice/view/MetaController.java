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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.commons.configuration.ConfigurationException;

public class MetaController implements Initializable {

	@FXML
	Button cancelMetaButton;
	@FXML
	Button okMetaButton;
	@FXML
	Label metaTagsLabel;
	@FXML
	VBox vBoxMeta;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		cancelMetaButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				stage.close();
			}
		});

		okMetaButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println(GUIController.metaTagElements.toString());

				GUIController.metaTagElements.add("Test");
				System.out.println(GUIController.metaTagElements.toString());

				GUIController.config.setProperty(
						"crawler.searchForMetaTags",
						GUIController.metaTagElements.toString()
								.replace("[", "").replace("]", ""));
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

		metaTagsLabel.setText("testtest");

		MetaTag.getMetaList();

	}

}
