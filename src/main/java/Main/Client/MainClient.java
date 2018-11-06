package Main.Client;

import Main.DataBase.DataBaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainClient extends Application {
    static DataBaseHandler dataBaseHandler;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/Client.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1200, 400));
        primaryStage.show();

    }


    public static void main(String[] args) {
        dataBaseHandler = new DataBaseHandler();
     launch(args);
    }

    public static DataBaseHandler getDataBaseHandler() {
        return dataBaseHandler;
    }
}
