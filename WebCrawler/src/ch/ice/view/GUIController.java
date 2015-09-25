package ch.ice.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import ch.ice.controller.MainController;

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
	private MenuItem quitMenuItem;

	@FXML
	private Button cancelMetaButton;
	@FXML
	private Button okMetaButton;
	@FXML
	private Label metaTagsList;
	@FXML
	private TextField pathTextField;
	@FXML
	private Button changeDirectory;

	public static ObservableValue<? extends String> test;

	public static PropertiesConfiguration config;

	public static List<String> metaTagElements;

	public static String path;

	public static void getProperties(Label label) {
		try {
			config = new PropertiesConfiguration("conf/app.properties");
			metaTagElements = new CopyOnWriteArrayList<String>(
					Arrays.asList(config
							.getStringArray("crawler.searchForMetaTags")));
			label.setText(metaTagElements.toString());
		} catch (ConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static String getSaveProperties() {
		try {
			config = new PropertiesConfiguration("conf/app.properties");
			path = config.getString(("writer.file.path"));

		} catch (ConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return path;

	}

	public static void setSaveProperties(String chosenPath) {
		try {
			config = new PropertiesConfiguration("conf/app.properties");
			config.setProperty("writer.file.path", chosenPath);

		} catch (ConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		metaTagsList.setWrapText(true);
		metaTagsList.setMaxWidth(550);
		metaTagsList.setMaxHeight(80);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				selectFileButton.requestFocus();
			}
		});

		getProperties(metaTagsList);
		FileChooser filechooser = new FileChooser();
		DirectoryChooser directoryChooser = new DirectoryChooser();
		getSaveProperties();
		pathTextField.setText(path);

		selectFileButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Stage stage = new Stage();
				try {
					filechooser.getExtensionFilters().addAll(
							new FileChooser.ExtensionFilter(
									"Excel-File (*.xlsx)", "*.xlsx"),
							new FileChooser.ExtensionFilter(
									"Excel-File (*.xls)", "*.xls"));
					MainController.file = filechooser.showOpenDialog(stage);
					if (MainController.file != null) {
						fileTextField.setText(MainController.file
								.getAbsolutePath());
					}
				} catch (NullPointerException e) {

					System.out.println("No File selected");
					fileTextField.setText("");
				}

			}
		});

		changeDirectory.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Stage stage = new Stage();
				try {

					File pathFile = directoryChooser.showDialog(stage);
					if (pathFile != null) {
						setSaveProperties(pathFile.getAbsolutePath());
						config.save();
						getSaveProperties();
						pathTextField.setText(path);

					}
				} catch (NullPointerException | ConfigurationException e) {

					System.out.println("No Path selected");
					pathTextField.setText("");
				}
			}

		});

		startSearchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
						"SaveFile.fxml"));
				Parent root1;
				try {
					root1 = (Parent) fxmlLoader.load();

					Stage stage = new Stage();
					stage.setTitle("File processed");
					stage.setScene(new Scene(root1));
					stage.initStyle(StageStyle.UNDECORATED);
					stage.show();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Thread t1 = new Thread() {
					public void run() {
						MainController main = new MainController();

						main.startMainController();
					}
				};

				t1.start();

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
					stage.showAndWait();

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							getProperties(metaTagsList);
						}
					});

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		quitMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				System.exit(0);

			}
		});

	}
}
