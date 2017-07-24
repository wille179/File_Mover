package main.java.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main engine of the program. Launches the application.
 * @author William Rosser.
 * @version 2.1.1
 */
public class Engine extends Application {
    private Stage stage;

    /**
     * An empty constructor that does nothing.
     */
    public Engine() {
        System.out.print("");
    }

    /**
     * Starts the application.
     * @param s   The starting stage, provided by the Platform.
     */
    public void start(Stage s){
        stage = s;
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.setTitle("File Copier");
        Scene scene = null;
        try {
            Parent p = (Parent) FXMLLoader.load(Engine.class.getClassLoader().getResource("filemover_ui.fxml"));
            scene = new Scene(p);
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println("Could not open ui window. " + e.getCause());
            System.exit(1);
        }
        if (scene != null) {
            stage.show();
        }
    }
}
