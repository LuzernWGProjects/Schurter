package ch.ice.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import ch.ice.controller.MainController;

public class SaveWindowController implements Initializable {

	@FXML
	private Label endMessageLabel;
	@FXML
	private ProgressBar progressBar;

	boolean myBoo = true;
	Thread one;
	String points;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

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
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							endMessageLabel.setText("Please wait " + points);
							if (MainController.customerList != null) {
								System.out.println(MainController.customerList
										.size() + " : " + MainController.i);

								double d = (double) MainController.i
										/ (double) MainController.customerList
												.size();
								progressBar.setProgress(d);
								System.out.println(d);
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
