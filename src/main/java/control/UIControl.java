package main.java.control;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the primary UI.
 * @author William Rosser.
 * @version 2.1.1
 */
public class UIControl implements Initializable {
    @FXML
    private Button moveButton, sourceButton;
    @FXML
    private Label sourceLabel;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textField;
    @FXML
    private ChoiceBox formatType;
    @FXML
    private CheckBox checkrt, checksh;

    private static File folder;

    private static UIControl instance;

    //Folder address.

    public UIControl() {
        System.out.print("");
    }

    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        instance = this;
        setSourceLabel("No folder selected.");
        moveButton.setDisable(true);
        checkrt.setSelected(true);
        checksh.setSelected(true);
        formatType.getSelectionModel().selectedIndexProperty().addListener(
                new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        if (newValue.intValue() == 4) {
                            textField.setDisable(true);
                            textField.setText("");
                        } else {
                            textField.setDisable(false);
                        }
                    }
                }
        );
    }

    @FXML
    public void openConfirmWindow(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Confirm Copy");
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(UIControl.class.getResource("/confirm.fxml")));
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println("Could not open ui window.");
        }
        if (scene != null) {
            stage.show();
            ConfirmControl.setProgress(0);
        }
    }

    @FXML
    public void pickSource(ActionEvent e) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Pick Source Folder");
        File temp = dc.showDialog(new Stage());
        if (temp != null) {
            moveButton.setDisable(false);
            folder = temp;
        }
        setSourceLabel((folder != null) ? folder.getPath() : "No folder selected.");
    }

    private void setSourceLabel(String text) {
        sourceLabel.setText(text);
    }

    public static String getNums() {
        return instance.textArea.getText();
    }

    public static String getPrefix() {return instance.textField.getText();}

    public static File getFileLocation() {
        return folder;
    }

    public static boolean getRTSelected() {return instance.checkrt.isSelected();}

    public static boolean getSHSelected() {return instance.checksh.isSelected();}

    public static String getFormatType() {return (String) instance.formatType.getValue();}
}
