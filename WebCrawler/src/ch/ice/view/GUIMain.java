package ch.ice.view;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GUIMain extends Application {

	public static boolean internetCheck = false;
	public static boolean bingCheck = false;
	public static final Logger logger = LogManager.getLogger(GUIMain.class
			.getName());

	public static boolean netIsAvailable(String urlString) {

		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			// conn.setConnectTimeout(1000);
			conn.connect();
			// Object objData = conn.getContent();

			conn = null;
			return true;

		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static void externalNetCheck() {
		if (netIsAvailable("http://www.google.com") == true) {
			internetCheck = true;

			if (netIsAvailable("http://www.bing.com") == true) {
				bingCheck = true;

			}

			if (netIsAvailable("http://www.bing.com") == false) {
				bingCheck = false;

			}

		}

		if (netIsAvailable("http://www.google.com") == false) {
			internetCheck = false;
			Alert alert = new Alert(AlertType.ERROR);
			alert.initStyle(StageStyle.UNDECORATED);
			DialogPane pane = alert.getDialogPane();
			pane.getStylesheets().add("ch/ice/view/WebCrawler.css");
			alert.setTitle("Internet Connection");
			alert.setHeaderText("No Internet Connection");
			alert.setContentText("Please establish Internet Connection");
			alert.showAndWait();
			System.exit(0);

		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// int initialDelay = 0; // start after 30 seconds
		// int period = 1000;
		// Timer timer = new Timer();
		// TimerTask task = new TimerTask() {
		// public void run() {
		// System.out.println(internetCheck + "   " + bingCheck);

		externalNetCheck();

		// }
		// };
		// timer.scheduleAtFixedRate(task, initialDelay, period);

		Parent root = FXMLLoader.load(getClass().getResource("Welcome.fxml"));

		Scene scene = new Scene(root);

		primaryStage
				.setTitle("Schurter POS-Data-Enhancement System (SPOSDES) ");
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.seconds(4),
						new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								primaryStage.hide();
								loadMain();
							}
						}));
		timeline.play();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void loadMain() {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
				"/ch/ice/view/Schurter.fxml"));
		Parent root1;
		try {
			root1 = (Parent) fxmlLoader.load();
			Scene scene = new Scene(root1);
			scene.setFill(Color.TRANSPARENT);
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.setTitle("Schurter POS-Data-Enhancement System (SPOSDES) ");
			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);

			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);

		}
	}
}
