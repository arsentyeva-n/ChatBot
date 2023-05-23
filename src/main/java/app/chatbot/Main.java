/**
 * ChatBot
 *
 * @author Arsentyeva N. V.
 */
package app.chatbot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class Main extends Application {
    private static int width = 343;
    private static int height = 485;


    @Override
    public void start(Stage primaryStage) throws IOException {
        // Стартовое окно
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("log_in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        primaryStage.initStyle(StageStyle.TRANSPARENT); // Убираем кнопки (Закрыть,свернуть и т.п)
        primaryStage.setScene(scene);
        // Установка иконки
        Image applicationIcon = new Image(getClass().getResourceAsStream("/icon/icon.png"));
        primaryStage.getIcons().add(applicationIcon);
        primaryStage.setTitle("Виртуальный помощник Ева");
        primaryStage.show();
    }

    // Запуск главного цикла приложения
    // launch запускает методы init(), start() класса Application
    public static void main(String[] args) {
        launch(args);
    }
}