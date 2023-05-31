/**
 * ChatBot
 *
 * @author Arsentyeva N. V.
 */

package app.chatbot;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainWindow {

    private static int width = 342;

    private static int height = 485;

    private ChatBot eva;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private TextField messageField;


    @FXML
    private TextArea textArea;

    // Кнопка отправки смс
    @FXML
    private void send() {
        if (messageField.getText().equals("")) {
            return;
        }
        Date date = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm:ss");
        String userMessage = "[" + formatForDateNow.format(date) + "]" + " " + eva.getUserName() + " : " + messageField.getText() + "\n";
        textArea.setText(textArea.getText() + userMessage); // Отправка сообщения пользователя
        if (messageField.getText().equals("/saveon") || messageField.getText().equals("/saveoff") ||
                messageField.getText().equals("/fileclean") || messageField.getText().equals("/loaddialog")
                || messageField.getText().equals("/clear")){
            if (messageField.getText().equals("/clear")) {
                textArea.clear();
            } else
             if (messageField.getText().equals("/loaddialog")) {
                try {
                    textArea.setText(eva.checkFunction(messageField.getText()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                messageField.setText("");
                return;
            } else {
                try {
                    eva.checkFunction(messageField.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String botMessage = "[" + formatForDateNow.format(date) + "]" + " " + eva.getBotName() + " : " + eva.say(messageField.getText()) + "\n";
        textArea.setText(textArea.getText() + botMessage);
        messageField.setText("");
        eva.addMessage(userMessage + botMessage); // добавление в список истории сообщения
    }

    @FXML
    private void initialize() {
        eva = new ChatBot();
        eva.setBotName("Ева");
        try {
            eva.setUserName(eva.loadName()); // todo как имя передается из одного окна в другое сгл
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Обработка клавиш
        messageField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    send();
                }
            }
        });

    }

    @FXML
    public void start(Stage primaryStage) throws IOException {
        /*Создаем окно*/
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        primaryStage.initStyle(StageStyle.TRANSPARENT); // Убираем кнопки (Закрыть,свернуть и т.п)
        primaryStage.setScene(scene);
        primaryStage.setTitle("Виртуальный помощник Ева");
        primaryStage.show();
    }

    @FXML
    private void close() { // Закрытие программы
        if (eva.isSaveFlag())                        // если пользователь указал сохранение диалога
            eva.addHistory(textArea.getText());     // текст из textArea сохраняется в файл под именем пользователя

      /*  LinkedList<String> sms = new LinkedList<>(); // Список сообщений
        sms = eva.getHistory(); // добавление в список истории сообщения
        for(String i : sms){
            System.out.print(i);
        }*/
        Stage stage = (Stage) mainAnchor.getScene().getWindow();
        stage.close();
    }

}

