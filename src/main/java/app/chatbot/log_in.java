/**
 * ChatBot
 *
 * @author Arsentyeva N. V.
 */

package app.chatbot;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class log_in {
    private static int width = 340;

    private static int height = 480;

    private static String filename = "users/names.txt";
    @FXML
    private TextField fieldName;

    @FXML
    private void start() throws IOException { // Обработка кнопки начать
        // Создание папки users
        String path = "users/";
        Files.createDirectories(Paths.get(path));
        // Проверка имени и его длины
        if (!fieldName.getText().equals("") && fieldName.getText().length() > 1) {
            // Класс FileWriter является производным от класса Writer. Он используется для записи текстовых файлов.
            FileWriter writer = new FileWriter(filename, true); // true == добавление текста в файл, если файл существует
            writer.write(fieldName.getText() + "\n");
            writer.close();
            this.close();
            MainWindow mainWindow = new MainWindow();
            mainWindow.start(new Stage());

        } else {
            // Если не проходит проверку, то создаем окно с "Error"
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.TRANSPARENT);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Длина имени должна быть больше 1-го символа");
            alert.setContentText("Введите имя корректно");
            alert.showAndWait();
        }
    }

    /* fieldName.setOnKeyPressed() устанавливает обработчик событий KeyPressed для поля ввода (fieldName).
     * Если клавиша была нажата, обработчик, который был установлен, получает KeyEvent и проверяет,
     * является ли нажатая клавиша клавишей ENTER. Если это так, то вызывается метод start().
     * В случае ошибки внутри блока try, он бросает исключение RuntimeException.
     */
    @FXML
    private void initialize() {
        fieldName.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    try {
                        start();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
        });
    }


    @FXML
    private void close() { // Закрытие программы
        // закрываем сцену
        Stage stage = (Stage) fieldName.getScene().getWindow();
        stage.close();
    }

}
