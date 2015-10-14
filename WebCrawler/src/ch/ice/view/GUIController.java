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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ice.controller.MainController;
import ch.ice.controller.web.SearchEngineFactory;

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
	@FXML
	private Label internetLabel;
	@FXML
	private Label bingLabel;
	@FXML
	private AnchorPane anchorLow;
	@FXML
	private ImageView searchImage;

	SwitchButton switchToggle;

	public static ObservableValue<? extends String> test;

	public static PropertiesConfiguration config;

	public static List<String> metaTagElements;

	public static String path;

	public static String searchGlobal;

	public static Image googleImage = new Image(
			MetaController.class.getResourceAsStream("/Google.png"));
	public static Image bingImage = new Image(
			MetaController.class.getResourceAsStream("/Bing.png"));

	public static final Logger logger = LogManager
			.getLogger(GUIController.class.getName());

	public static void getProperties(Label label) {
		try {
			config = new PropertiesConfiguration("conf/app.properties");
			metaTagElements = new CopyOnWriteArrayList<String>(
					Arrays.asList(config
							.getStringArray("crawler.searchForMetaTags")));
			label.setText(metaTagElements.toString().replace("[", "")
					.replace("]", ""));
		} catch (ConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.error(e1);
		}

	}

	public static String getSaveProperties() {
		try {
			config = new PropertiesConfiguration("conf/app.properties");
			path = config.getString(("writer.file.path"));

		} catch (ConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.error(e1);
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
			logger.error(e1);
		}

	}

	public static void getSearchEngine() {
		try {
			config = new PropertiesConfiguration("conf/app.properties");
			searchGlobal = config.getString(("searchEngine.global"));
			if (searchGlobal.equals("GOOGLE")) {
				MainController.searchEngineIdentifier = SearchEngineFactory.GOOGLE;
			}
			if (searchGlobal.equals("BING")) {
				MainController.searchEngineIdentifier = SearchEngineFactory.BING;
			}

		} catch (ConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			logger.error(e1);
		}

	}

	public static void setSearchEngineImage(ImageView imageView) {
		try {
			config = new PropertiesConfiguration("conf/app.properties");
			searchGlobal = config.getString(("searchEngine.global"));
			if (searchGlobal.equals("GOOGLE")) {
				imageView.setImage(googleImage);
			}
			if (searchGlobal.equals("BING")) {
				imageView.setImage(bingImage);
			}

		} catch (ConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public static void getWriterFactoryProperties() {
		try {
			config = new PropertiesConfiguration("conf/app.properties");
			String tester = config.getString(("writer.factory"));
			if (tester.equals("FileWriterFactory.EXCEL")) {
				MainController.fileWriterFactory = "FileWriterFactory.EXCEL";
			}
			if (tester.equals("FileWriterFactory.CSV")) {
				MainController.fileWriterFactory = "FileWriterFactory.CSV";
			}

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

		getSearchEngine();
		setSearchEngineImage(searchImage);
		getWriterFactoryProperties();

		// Task task = new Task<Void>() {
		// @Override
		// public Void call() throws Exception {
		// int i = 0;
		// while (true) {
		// Platform.runLater(new Runnable() {
		// @Override
		// public void run() {

		// }
		// });
		// i++;
		// Thread.sleep(1000);
		//
		// }
		// }
		// };
		// Thread th = new Thread(task);
		// th.setDaemon(true);
		// th.start();

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

		GUIMain.externalNetCheck();

		if (GUIMain.internetCheck == true) {
			internetLabel.setText("Internet Connection Established");
			internetLabel.setTextFill(Color.GREEN);
			internetLabel.setVisible(false);

		} else {
			internetLabel.setText("No Internet Connection");
			internetLabel.setTextFill(Color.RED);
			startSearchButton.setDisable(true);

		}

		if (GUIMain.bingCheck == true) {
			bingLabel.setText("Bing is reachable");
			bingLabel.setTextFill(Color.GREEN);
			bingLabel.setVisible(false);

		} else {
			bingLabel.setText("Bing is unreachable");
			bingLabel.setTextFill(Color.RED);
			startSearchButton.setDisable(true);
		}

		selectFileButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Stage stage = new Stage();
				try {
					filechooser.getExtensionFilters().addAll(
							new FileChooser.ExtensionFilter(
									"Excel-File (*.xlsx)", "*.xlsx"),
							new FileChooser.ExtensionFilter(
									"Excel-File (*.xls)", "*.xls"),
							new FileChooser.ExtensionFilter("CSV-File (*.csv)",
									"*.csv"));
					MainController.uploadedFileContainingCustomers = filechooser
							.showOpenDialog(stage);
					if (MainController.uploadedFileContainingCustomers != null) {
						fileTextField
								.setText(MainController.uploadedFileContainingCustomers
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
					// Thread t1 = new Thread() {
					// public void run() {
					// MainController main = new MainController();
					//
					// try {
					// main.startMainController();
					// } catch (InternalFormatException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					//
					// }
					// }
					// };

					// t1.start();
					root1 = (Parent) fxmlLoader.load();

					Stage stage = new Stage();
					stage.setTitle("File processed");
					stage.setScene(new Scene(root1));
					stage.initStyle(StageStyle.UNDECORATED);
					stage.showAndWait();

				} catch (IOException | NullPointerException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// System.out.println("HAAAALLLOOOO");
					// Node source = (Node) event.getSource();
					// Stage stage = (Stage) source.getScene().getWindow();
					// stage.close();
					//
				}

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

					// Update Selected Meta Tags
					getProperties(metaTagsList);
					// Update SearchEngine Image
					setSearchEngineImage(searchImage);

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

		switchToggle = new SwitchButton();
		// switchToggle.setId("switchToggle");
		anchorLow.getChildren().add(switchToggle);
		// anchorLow.setRightAnchor(switchToggle, 60.0);
		// anchorLow.setTopAnchor(switchToggle, 30.0);
		switchToggle.setLayoutX(250.0);
		switchToggle.setLayoutY(45.0);

	}
}
