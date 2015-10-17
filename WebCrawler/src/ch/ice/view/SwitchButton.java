package ch.ice.view;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

import org.apache.commons.configuration.ConfigurationException;

import ch.ice.controller.MainController;

public class SwitchButton extends Label {

	private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(true);

	public SwitchButton() {

		Button switchBtn = new Button();
		switchBtn.setPrefWidth(35);
		switchBtn
				.setStyle("-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,1) , 5, 0.0 , 0 , 1 );-fx-background-insets: 0,1,2;"
						+ "-fx-background-color: linear-gradient(#f2f2f2, #d6d6d6), linear-gradient(#fcfcfc 0%, #d9d9d9 20%, #d6d6d6 100%),linear-gradient(#dddddd 0%, #f6f6f6 50%);");

		switchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				switchedOn.set(!switchedOn.get());
			}
		});

		setGraphic(switchBtn);

		switchedOn.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean t, Boolean t1) {
				if (t1) {
					MainController.fileWriterFactory = true;
					saveFactoryGlobal("EXCEL");
					setPadding(new javafx.geometry.Insets(0, 0, 0, 5));
					setText("Excel");
					setTextAlignment(TextAlignment.LEFT);
					setStyle("-fx-background-color: green;-fx-text-fill:white;-fx-background-radius: 4px;");
					setContentDisplay(ContentDisplay.RIGHT);
					setMinWidth(77.0);
				} else {
					// setMinWidth(77.0);
					MainController.fileWriterFactory = false;
					saveFactoryGlobal("CSV");
					setPadding(new javafx.geometry.Insets(0, 5, 0, 0));
					setText("CSV");
					setTextAlignment(TextAlignment.RIGHT);
					setStyle("-fx-background-color: orange;-fx-text-fill:white;-fx-background-radius: 4px;");
					setContentDisplay(ContentDisplay.LEFT);
					setMinWidth(77.0);
				}
			}
		});

		// switchedOn.set(false);
		if (MainController.fileWriterFactory == false) {
			switchedOn.set(false);
		}
		if (MainController.fileWriterFactory == true) {
			System.out.println("EXCEL");
			switchedOn.set(false);
			switchedOn.set(true);
		}

	}

	public SimpleBooleanProperty switchOnProperty() {
		return switchedOn;
	}

	public void saveFactoryGlobal(String factoryGlobal) {

		GUIController.config.setProperty("writer.factory", factoryGlobal);

		try {
			GUIController.config.save();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
