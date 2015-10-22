package ch.ice.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ice.controller.MainController;
import ch.ice.controller.web.SearchEngineFactory;
import ch.ice.exceptions.InternalFormatException;
import ch.ice.exceptions.MissingCustomerRowsException;
import ch.ice.model.Customer;
import ch.ice.utils.Config;

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
	private MenuItem manualMenuItem;
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
	private Label infoLabel;
	@FXML
	private AnchorPane anchorLow;
	@FXML
	private ImageView searchImage;
	@FXML
	private Button closeWindowButton;
	@FXML
	private Button lowerWindowButton;

	@FXML
	private VBox vBox;

	private double xOffset = 0;
	private double yOffset = 0;

	/**
	 * SwitchButton for CSV or EXCEL selection
	 */
	private SwitchButton switchToggle;

	/**
	 * Check variables for maximum search requests according to the selected
	 * Search Engine
	 */
	private int maxGoogle;
	private int maxBing;
	private int listSize = 0;

	/**
	 * Text for InfoLabel including search exceeds errors and status report
	 */
	private String statusOk = "File Status OK";
	private String googleExceeds = "The file exceeds the allowed Google searches of ";
	private String bingExceeds = "The file exceeds the allowed Bing searches of ";

	/**
	 * List for loaded Metatags out of XML. Is referenced by other Classes
	 */
	public static List<String> metaTagElements;

	/**
	 * path for storage of file
	 */
	public static String path;
	/**
	 * path for selected file
	 */
	public static String chosenPath;

	private File pathFile;

	/**
	 * Check variable for selected Search Engine
	 */
	private String searchGlobal;

	/**
	 * Images for Search Engine
	 */
	private Image googleImage = new Image(
			GUIController.class.getResourceAsStream("/Google.png"));
	private Image bingImage = new Image(
			GUIController.class.getResourceAsStream("/Bing.png"));

	public static PropertiesConfiguration config = Config.PROPERTIES;

	public static final Logger logger = LogManager
			.getLogger(GUIController.class.getName());

	/**
	 * Gets the Metatags out of the Properties and Updates the Label in the
	 * Parameter. Is referenced by other Classes
	 * 
	 * @param label
	 */
	public static void getProperties(Label label) {
		metaTagElements = new CopyOnWriteArrayList<String>(Arrays.asList(config
				.getStringArray("crawler.searchForMetaTags")));
		label.setText(metaTagElements.toString().replace("[", "")
				.replace("]", ""));

	}

	/**
	 * Gets the paths for the selected file and the saveFile directory
	 * 
	 * @param startButton
	 */
	private boolean getSaveProperties(Button startButton) {
		path = config.getString(("writer.file.path"));
		chosenPath = config.getString(("writer.file.chosenPath"));
		if (MainController.uploadedFileContainingCustomers == null) {
			return false;

		} else

			return true;

	}

	/**
	 * sets the Properties of the file path and the saveFile path
	 * 
	 * @param path
	 *            for the saveFile action
	 * @param chosenPath
	 *            for initial directory for the file selection
	 */
	private void setSaveProperties(String path, String chosenPath) {
		config.setProperty("writer.file.path", path);
		config.setProperty("writer.file.chosenPath", chosenPath);

	}

	/**
	 * Sets searchEngineIdentifier for MainController to use the selected search
	 * engine
	 */
	private void getSearchEngine() {
		searchGlobal = config.getString(("searchEngine.global"));
		if (searchGlobal.equals("GOOGLE")) {
			MainController.searchEngineIdentifier = SearchEngineFactory.GOOGLE;
		}
		if (searchGlobal.equals("BING")) {
			MainController.searchEngineIdentifier = SearchEngineFactory.BING;
		}

	}

	/**
	 * Sets the Image of the selected search engine
	 * 
	 * @param imageView
	 */
	private void setSearchEngineImage(ImageView imageView) {
		searchGlobal = config.getString(("searchEngine.global"));
		if (searchGlobal.equals("GOOGLE")) {
			imageView.setImage(googleImage);

		}
		if (searchGlobal.equals("BING")) {
			imageView.setImage(bingImage);
		}

	}

	/**
	 * sets the selected Output File extension CSV or EXCEL
	 */
	private void getWriterFactoryProperties() {
		String tester = config.getString(("writer.factory"));
		if (tester.equals("EXCEL")) {
			MainController.fileWriterFactory = true;
		} else if (tester.equals("CSV")) {
			MainController.fileWriterFactory = false;
		}

	}

	/**
	 * sets the maximum amount of search requests according to the config file
	 */
	private void getMaxGoogleSearches() {
		maxGoogle = Integer.parseInt(config
				.getString(("searchEngine.maxGoogleSearches")));
		maxBing = Integer.parseInt(config
				.getString(("searchEngine.maxBingSearches")));

	}

	/**
	 * Checks the size of the file and disables the search Button. Further it
	 * shows an accurate message
	 * 
	 * @param startSearchButton
	 * @param infoLabel
	 */
	private boolean getCheckStatus(Button startSearchButton, Label infoLabel) {

		while (listSize > 0) {
			if (listSize > maxGoogle && searchGlobal.equals("GOOGLE")) {
				System.out.println(maxGoogle);
				infoLabel.setText(googleExceeds + maxGoogle);
				infoLabel.setTextFill(Color.RED);
				return false;
			} else if (listSize > maxBing && searchGlobal.equals("BING")) {
				System.out.println("Second");
				System.out.println(listSize);
				infoLabel.setText(bingExceeds + maxBing);
				infoLabel.setTextFill(Color.RED);
				return false;
			} else {
				infoLabel.setText(statusOk);
				infoLabel.setTextFill(Color.GREEN);
				return true;
			}

		}
		return false;

	}

	private boolean checkAll() {
		try {
			if (getSaveProperties(startSearchButton) == true
					&& getCheckStatus(startSearchButton, infoLabel) == true
					&& pathFile.exists() == true) {

				startSearchButton.setDisable(false);
				return true;
			} else {
				startSearchButton.setDisable(true);

				return false;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.out.println("Save Path not set yet or wrong Directory");
			logger.info("Save Path not set yet or wrong Directory");
		}
		startSearchButton.setDisable(true);
		return false;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// Set Initials
		infoLabel.setText("No file selected");
		infoLabel.setTextFill(Color.ORANGE);
		metaTagsList.setWrapText(true);
		metaTagsList.setMaxWidth(550);
		metaTagsList.setMaxHeight(80);

		// Get and set Properties
		getSearchEngine();
		setSearchEngineImage(searchImage);
		getWriterFactoryProperties();
		getProperties(metaTagsList);
		getSaveProperties(startSearchButton);
		getMaxGoogleSearches();
		getCheckStatus(startSearchButton, infoLabel);
		pathFile = new File(path);
		checkAll();
		pathTextField.setText(path);
		fileTextField.setText(chosenPath);

		// GUIMain.externalNetCheck();

		/**
		 * AcionListener for Select File Button
		 */
		selectFileButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				// Stage stage = new Stage();
				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				FileChooser filechooser = new FileChooser();
				try {
					filechooser.getExtensionFilters().addAll(
							new FileChooser.ExtensionFilter(
									"Excel-File (*.xlsx)", "*.xlsx"),
							new FileChooser.ExtensionFilter(
									"Excel-File (*.xls)", "*.xls"),
							new FileChooser.ExtensionFilter("CSV-File (*.csv)",
									"*.csv"));

					// set Initital Directory according to config
					if (!chosenPath.isEmpty()) {
						File initial = new File(chosenPath);
						filechooser.setInitialDirectory(initial);
					}
					// get File
					MainController.uploadedFileContainingCustomers = filechooser
							.showOpenDialog(stage);
					if (MainController.uploadedFileContainingCustomers != null) {
						fileTextField
								.setText(MainController.uploadedFileContainingCustomers
										.getAbsolutePath());
						fileTextField.setStyle("-fx-text-inner-color: black;");

						// Save path to config
						setSaveProperties(
								path,
								MainController.uploadedFileContainingCustomers
										.getAbsolutePath()
										.replaceAll(
												MainController.uploadedFileContainingCustomers
														.getName(), ""));
						config.save();
						// update configs for GUI
						getSaveProperties(startSearchButton);
						List<Customer> testList = MainController
								.retrieveCustomerFromFile(MainController.uploadedFileContainingCustomers);
						listSize = testList.size();
						getCheckStatus(startSearchButton, infoLabel);
						checkAll();

					} else if (MainController.uploadedFileContainingCustomers == null) {
						MainController.uploadedFileContainingCustomers = new File(
								fileTextField.getText());
						fileTextField.setStyle("-fx-text-inner-color: black;");
						checkAll();

					} else {
						fileTextField.setText("Illegal Directory for File");
						fileTextField.setStyle("-fx-text-inner-color: red;");
						checkAll();
					}
				} catch (NullPointerException | InternalFormatException
						| ConfigurationException e) {
					e.printStackTrace();
					logger.error(e);
					System.out.println("No File selected");
					fileTextField.setText("Wrong File Format");
					fileTextField.setStyle("-fx-text-inner-color: red;");
					checkAll();
					startSearchButton.setDisable(true);
				} catch (MissingCustomerRowsException e) {
					e.printStackTrace();
					logger.error(e);
					System.out.println("No Customers in File");
					fileTextField
							.setText("There seem to be no Customers in the file");
					fileTextField.setStyle("-fx-text-inner-color: red;");
					checkAll();
					startSearchButton.setDisable(true);

				} catch (IllegalArgumentException e) {
					logger.error(e);
					e.printStackTrace();
					fileTextField.setText("Illegal Directory for File");
					fileTextField.setStyle("-fx-text-inner-color: red;");
					checkAll();
					setSaveProperties(path, "");
					checkAll();
					try {
						config.save();
					} catch (ConfigurationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						logger.error(e1);
					}
					getSaveProperties(startSearchButton);
					checkAll();

				}

			}
		});

		/**
		 * ActionListener for Change Directory Button
		 */
		changeDirectory.setOnAction(new EventHandler<ActionEvent>() {
			DirectoryChooser directoryChooser = new DirectoryChooser();

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				// Stage stage = new Stage();
				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				try {
					if (!path.isEmpty() && pathFile.exists() == true) {
						File initial = new File(path);
						directoryChooser.setInitialDirectory(initial);

					}
					pathFile = directoryChooser.showDialog(stage);

					if (pathFile != null && pathFile.exists() == true) {
						setSaveProperties(pathFile.getAbsolutePath(),
								chosenPath);
						config.save();
						getSaveProperties(startSearchButton);
						pathTextField.setText(path);
						pathTextField.setStyle("-fx-text-inner-color: black;");
						checkAll();

					} else if (pathFile == null) {
						pathTextField.setText(path);
						pathTextField.setStyle("-fx-text-inner-color: black;");
						pathFile = new File(path);
						checkAll();

					} else {

						pathTextField.setText("Illegal Directory for Saving");
						pathTextField.setStyle("-fx-text-inner-color: red;");
						checkAll();
					}
					// if there is no path selected
				} catch (NullPointerException | ConfigurationException e) {
					logger.error(e);
					e.printStackTrace();
					System.out.println("No Path selected");
					pathTextField.setText("No Directory selected");
					pathTextField.setStyle("-fx-text-inner-color: red;");
					setSaveProperties("", chosenPath);
					checkAll();
					try {
						config.save();
					} catch (ConfigurationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						logger.error(e1);
					}
					getSaveProperties(startSearchButton);
					checkAll();

					// if Directory is invalid
				} catch (IllegalArgumentException e) {
					logger.error(e);
					e.printStackTrace();
					pathTextField.setText("Illegal Directory for Saving");
					pathTextField.setStyle("-fx-text-inner-color: red;");
					setSaveProperties("", chosenPath);
					checkAll();
					try {
						config.save();
					} catch (ConfigurationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						logger.error(e1);
					}
					getSaveProperties(startSearchButton);
					checkAll();

				}
			}

		});

		/**
		 * ActionListener for StartSearchButton. If Paths are emppty the Button
		 * is disabled
		 */
		startSearchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (path.equals("") || chosenPath.equals("")) {
					return;
				}

				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
						"SaveFile.fxml"));
				Parent root1;
				try {
					root1 = (Parent) fxmlLoader.load();
					Stage stage = new Stage();
					stage.setTitle("File processed");
					stage.setScene(new Scene(root1));
					stage.initStyle(StageStyle.UNDECORATED);
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.showAndWait();

				} catch (IOException | NullPointerException e) {
					e.printStackTrace();
					logger.error(e);
				}

			}

		});

		/**
		 * ActionListerner for Properties Button
		 */
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
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.showAndWait();

					// Update Selected Meta Tags
					getProperties(metaTagsList);
					// Update SearchEngine Image
					setSearchEngineImage(searchImage);
					getMaxGoogleSearches();
					getCheckStatus(startSearchButton, infoLabel);
					checkAll();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e);
				}

			}
		});

		/**
		 * ActionListener for Quit Option
		 */
		quitMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				System.exit(0);

			}
		});

		// Add SwitchButton to View
		switchToggle = new SwitchButton();
		anchorLow.getChildren().add(switchToggle);
		switchToggle.setLayoutX(250.0);
		switchToggle.setLayoutY(45.0);

		/**
		 * ActionListener for Decoration Button Close
		 */
		closeWindowButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				System.exit(0);

			}
		});

		/**
		 * ActionListener for Decoration Button Lower
		 */
		lowerWindowButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				stage.setIconified(true);

			}
		});

		manualMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				Desktop dt = Desktop.getDesktop();
				try {

					dt.open(new File("conf/SPOSDES_manual.pdf"));
				} catch (IOException e) {
					// TODO
					// Auto-generated
					// catch block
					e.printStackTrace();
				}

			}
		});

		/**
		 * Make Window Moveable
		 */
		vBox.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		vBox.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Node source = (Node) event.getSource();
				Stage stage = (Stage) source.getScene().getWindow();
				stage.setX(event.getScreenX() - xOffset);
				stage.setY(event.getScreenY() - yOffset);
			}
		});

	}
}
