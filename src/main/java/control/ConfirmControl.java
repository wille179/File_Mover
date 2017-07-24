package main.java.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import main.java.main.Copier;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the confirmation UI.
 * @author William Rosser.
 * @version 2.1.1
 */
public class ConfirmControl implements Initializable {
    @FXML
    private Button cancelButton, okButton;
    @FXML
    private Label detailsLabel;
    @FXML
    private ProgressBar progress;
    private static double currentProgress = 0;

    private static ConfirmControl instance;

    private boolean done  = false;

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        instance = this;
        done = false;
        detailsLabel.setText("Your files will be save to:\n" + UIControl.getFileLocation() + "\\selected");
    }

    @FXML
    public void close(ActionEvent e) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    public void copy(ActionEvent e) {
        if (!done) {
            okButton.setDisable(true);
            new Thread() {
                public void run() {
                    System.out.println("Copied " + (new Copier()).copy(UIControl.getNums(), UIControl.getPrefix()) + " files.");
                }
            }.start();
            okButton.setText("Finish");
            okButton.setDisable(false);
            done = true;
        } else {
            close(e);
        }
    }

    public static void setProgress(double prog) {
        currentProgress = prog;
        instance.okButton.setDisable(currentProgress < 1 && currentProgress > 0);
        instance.okButton.setText((currentProgress > 0) ? "Finish" : "Copy");
        instance.progress.setProgress(currentProgress);
    }
}
