module app.chatbot {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;

    opens app.chatbot to javafx.fxml;
    exports app.chatbot;
}