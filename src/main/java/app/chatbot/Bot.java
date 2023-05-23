/**
 * ChatBot
 *
 * @author Arsentyeva N. V.
 */

package app.chatbot;

import java.io.FileNotFoundException;

public abstract class Bot {
    private String botName;     // хранит имя бота

    public Bot() {
    }

    private String userName;    // хранит имя пользователя

    public abstract String loadName() throws FileNotFoundException; // загружает имя из файла

    public abstract void addHistory(String message);                // добавляет историю в текстовый файл под именем юзера

    public abstract String loadHistory();                           // загружает историю из файла

    public abstract String say(String message);                     // основной метод бота

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBotName() {
        return botName;
    }

    public String getUserName() {
        return userName;
    }


}