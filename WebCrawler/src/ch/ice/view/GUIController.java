package ch.ice.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUIController implements Initializable {

	@FXML
	private Button selectFileButton;
	@FXML
	private Button startSearchButton;
	@FXML
	private TextField fileTextField;
	@FXML
	private ProgressBar searchProgressBar;
	@FXML
	private MenuItem MetaTags;
	@FXML
	private Button cancelMetaButton;
	@FXML
	private Button okMetaButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// TODO Auto-generated method stub

		selectFileButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				System.out.println("Select File Clicked");

			}
		});

		startSearchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				System.out.println("Start Search Clicked");

			}
		});

		MetaTags.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
						"metaChoice.fxml"));
				Parent root1;
				try {
					root1 = (Parent) fxmlLoader.load();

					Stage stage = new Stage();
					stage.setTitle("Choose your Meta Tags");
					stage.setScene(new Scene(root1));
					stage.initStyle(StageStyle.UNDECORATED);
					stage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

}
