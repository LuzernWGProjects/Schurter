package ch.ice.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import ch.ice.controller.MainController;
import ch.ice.controller.file.ExcelWriter;

public class SaveWindowController implements Initializable {

	@FXML
	private Label endMessageLabel;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private Label progressLabel;
	@FXML
	private Button closeButton;
	@FXML
	private Button openFileButton;
	@FXML
	private Button cancelButton;

	boolean myBoo = true;
	Thread one;
	String points;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		closeButton.setDisable(true);
		openFileButton.setDisable(true);
		cancelButton.setDisable(false);

		Task task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				int i = 0;
				while (true) {
					final int finalI = i;
					if (i == 4) {
						i = 0;
					}
					if (i == 0) {
						points = "";
					}
					if (i == 1) {
						points = ".";
					}
					if (i == 2) {
						points = "..";
					}
					if (i == 3) {
						points = "...";
					}

					// make static amendments in MainController and we're good
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							endMessageLabel.setWrapText(true);
							endMessageLabel.setMaxWidth(400);
							endMessageLabel.setMaxHeight(80);
							endMessageLabel.setText("Please wait " + points);

							cancelButton
									.setOnAction(new EventHandler<ActionEvent>() {

										@Override
										public void handle(ActionEvent event) {

											cancel(true);
											Node source = (Node) event
													.getSource();
											Stage stage = (Stage) source
													.getScene().getWindow();
											stage.close();

										}
									});

							if (MainController.customerList != null) {
								System.out.println(MainController.customerList
										.size() + " : " + MainController.i);

								double d = (double) MainController.i
										/ (double) MainController.customerList
												.size();
								progressBar.setProgress(d);
								progressLabel
										.setText(MainController.progressText);
								System.out.println(d);

								if (progressBar.getProgress() == 1) {

									endMessageLabel
											.setText("Your file has been processed and saved to: "
													+ GUIController.path);
									progressLabel
											.setText("Gathering Process ended. We call that AWESOME!");

									closeButton.setDisable(false);
									openFileButton.setDisable(false);
									cancelButton.setDisable(true);

									closeButton
											.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(
														ActionEvent event) {

													System.exit(0);

												}
											});
									openFileButton
											.setOnAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(
														ActionEvent event) {
													Desktop dt = Desktop
															.getDesktop();
													try {
														dt.open(new File(
																GUIController.path
																		+ "/"
																		+ ExcelWriter.fileName));
													} catch (IOException e) {
														// TODO Auto-generated
														// catch block
														e.printStackTrace();
													}

												}
											});

									cancel(true);

								}

							} else
								System.out.println("Not yet...");
						}
					});
					i++;
					Thread.sleep(250);

				}
			}
		};
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();

	}
}
