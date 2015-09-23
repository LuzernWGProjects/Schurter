package ch.ice.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
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
	FlowPane flowPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		GUIController.getProperties(metaTagsLabel);

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

		ArrayList<MetaTag> endList = MetaTag.getMetaList();

		for (int i = 0; i < endList.size(); i++) {

			MetaTag mt = endList.get(i);
			String name = mt.getMapXML().get("name");
			CheckBox cb = new CheckBox(name);
			cb.setMinWidth(150);
			cb.setMinHeight(30);
			for (String checker : GUIController.metaTagElements) {
				if (cb.getText().equals(checker)) {
					cb.selectedProperty().set(true);
				}
				System.out.println(cb.getText() + " " + checker);
			}
			cb.selectedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(
						ObservableValue<? extends Boolean> observable,
						Boolean oldValue, Boolean newValue) {
				}
			});
			cb.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					if (cb.isSelected()) {
						System.out.println(cb.getText() + " is checked");
						GUIController.metaTagElements.add(cb.getText());
						String innerLabel = GUIController.metaTagElements
								.toString();
						metaTagsLabel.setText(innerLabel);

					}
					if (cb.isSelected() == false) {
						System.out.println("We are in!");
						for (String checker : GUIController.metaTagElements) {
							if (checker.equals(cb.getText())) {

								GUIController.metaTagElements.remove(checker);
								String innerLabel = GUIController.metaTagElements
										.toString();
								metaTagsLabel.setText(innerLabel);
							}
						}

					}

				}

			});

			flowPane.setPrefWrapLength(800);
			flowPane.getChildren().add(cb);
		}

	}
}
