package ch.ice.view;

import java.io.IOException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class GUIMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

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
				"Schurter.fxml"));
		Parent root1;
		try {
			root1 = (Parent) fxmlLoader.load();

			Stage stage = new Stage();
			stage.setTitle("Schurter POS-Data-Enhancement System (SPOSDES) ");
			stage.setScene(new Scene(root1));
			stage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}
