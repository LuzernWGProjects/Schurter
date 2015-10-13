package ch.ice.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import ch.ice.controller.MainController;
import ch.ice.controller.file.ExcelWriter;
import ch.ice.exceptions.InternalFormatException;
import ch.ice.exceptions.MissingCustomerRowsException;

public class SaveWindowController extends Thread implements Initializable {

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
	@FXML
	public static Button hobbyButton;

	public static boolean myBoo = false;
	public static boolean myBooWriting = false;
	public static boolean myBooChecking = false;
	Thread one;
	String points;
	Thread t1;
	Thread th;
	Task task;
	Task task1;
	private static Boolean pauseFlag = false;

	// public static void resumeThread() {
	// pauseFlag = false;
	// myBooChecking = false;
	// synchronized (pauseFlag) {
	// pauseFlag.notifyAll();
	//
	// }
	// }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		closeButton.setDisable(true);
		openFileButton.setDisable(true);
		cancelButton.setDisable(false);

		cancelButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				myBooChecking = true;

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Your Canceling the Process");
				alert.setContentText("No file will be saved");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					// ... user chose OK
					System.exit(0);
				} else {
					// ... user chose CANCEL or closed the dialog
					// resumeThread();
					// Node source = (Node) event.getSource();
					// Stage stage = (Stage) source.getScene().getWindow();
					// stage.show();

				}
				// Node source = (Node) event.getSource();
				// Stage stage = (Stage) source.getScene().getWindow();
				// stage.close();
			}

		});

		task = new Task<Void>() {
			@Override
			public Void call() throws Exception {

				int i = 0;
				while (myBooChecking == false) {

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

							// if (myBooChecking == true) {
							// synchronized (pauseFlag) {
							// try {
							// pauseFlag.wait();
							// } catch (InterruptedException e) {
							// // TODO Auto-generated catch block
							// e.printStackTrace();
							// }
							// }
							// }

							endMessageLabel.setWrapText(true);
							endMessageLabel.setMaxWidth(400);
							endMessageLabel.setMaxHeight(80);
							endMessageLabel.setText("Please wait " + points);

							if (MainController.customerList != null) {
								System.out.println(MainController.customerList
										.size()
										+ " : "
										+ MainController.customersEnhanced);

								double d = (double) MainController.customersEnhanced
										/ (double) MainController.customerList
												.size();
								progressBar.setProgress(d);
								progressLabel
										.setText(MainController.progressText);
								System.out.println(d);

								if (myBooWriting == true) {
									progressLabel.setText("Writing File");
								}

								if (myBoo == true) {

									endMessageLabel
											.setText("Your file has been processed and saved to: "
													+ GUIController.path);
									progressLabel
											.setText("Gathering Process ended.");

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
														// TODO
														// Auto-generated
														// catch block
														e.printStackTrace();
													}

												}
											});

									cancel(true);
								}

							}

						}
					});
					i++;
					Thread.sleep(250);

				}
				return null;
			}
		};

		th = new Thread(task);
		th.setDaemon(true);
		th.setName("THREAD GUI");
		th.start();

		task1 = new Task<Void>() {
			@Override
			public Void call() throws Exception {

				MainController main = new MainController();

				// if (myBooChecking == true) {
				// synchronized (pauseFlag) {
				// pauseFlag.wait();
				// }
				// }
				try {
					main.startMainController();

				} catch (InternalFormatException | MissingCustomerRowsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				cancel(true);
				return null;

			}

		};
		t1 = new Thread(task1);
		t1.setDaemon(true);
		t1.setName("THREAD MainController");
		t1.start();

	}
}
