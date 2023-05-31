/**
 * ChatBot
 *
 * @author Arsentyeva N. V.
 */

package app.chatbot;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.text.DateFormat;

// ChatBot наследуется от Bot
public class ChatBot extends Bot {
    private static String filename = "users/names.txt";

    // Конструктор от производного класса
    public ChatBot() {      // Конструктор
        // todo вызов конструктора родительского класса, используется для инициализации родительского класса,
        // чтобы объекты, созданные из наследующего класса, имели доступ к полям и методам родительского класса
        super();
    }

    // Работа с регулярными выражениями в любой Java-программе начинается с создания объекта класса Pattern.
    private Pattern pattern;
    private boolean saveFlag;

    // Вопросы и ответы пользователя
    final private Map<String, String> keys = new HashMap<String, String>() {{
        // hello
        put("здравствуйте", "hello");
        put("хай", "hello");
        put("привет", "hello");
        // who
        //todo Экранирование. Для обычного использования метасимволов в качестве обычных.
        put("кто\\s.*ты", "who");
        put("ты\\s.*кто", "who");
        // name
        put("как\\s.*зовут", "name");
        put("как\\s.*имя", "name");
        put("есть\\s.*имя", "name");
        put("какое\\s.*имя", "name");
        // howareyou
        put("как\\s.*дела", "howareyou");
        put("как\\s.*жизнь", "howareyou");
        // whatdoyoudoing
        put("зачем\\s.*тут", "whatdoyoudoing");
        put("зачем\\s.*здесь", "whatdoyoudoing");
        put("что\\s.*делаешь", "whatdoyoudoing");
        put("чем\\s.*занимаешься", "whatdoyoudoing");
        // whatdoyoulike
        put("что\\s.*нравится", "whatdoyoulike");
        put("что\\s.*любишь", "whatdoyoulike");
        // iamfeelling
        put("кажется", "iamfeelling");
        put("чувствую", "iamfeelling");
        put("испытываю", "iamfeelling");
        // yes
        put("^да", "yes");
        put("согласен", "yes");
        // whattime
        put("который\\s.*час", "whattime");
        put("сколько\\s.*время", "whattime");
        // bye
        put("пока", "bye");
        put("прощай", "bye");
        put("увидимся", "bye");
        put("до\\s.*свидания", "bye");
        // info
        put("/info", "information");
        put("что\\s.*умеешь", "information");
        put("/saveon", "saveon");
        put("/saveoff", "saveoff");
        put("/fileclean", "fileclean");
        put("/dollar", "coursedollar");
        put("/euro", "courseeuro");
        put("/weather", "weather");
        put("/clear", "clear");
        // math
        put("вычисли\\s?\\d+\\s?", "math");
    }};


    // Ответы и вопросы бота
    final private Map<String, String> answersByKeys = new HashMap<String, String>() {{
        put("hello", "Здравствуйте, рада Вас видеть.");
        put("who", "Я Ева, виртуальный помощник.");
        put("name", "Меня зовут Ева.");
        put("howareyou", "У меня всё хорошо, спасибо. ");
        put("whatdoyoudoing", "Я общаюсь с людьми.");
        put("whatdoyoulike", "Общаться с людьми.");
        put("iamfeelling", "Как давно это началось? Расскажите чуть подробнее.");
        put("yes", "Согласие есть продукт при полном непротивлении сторон.");
        put("bye", "До свидания. Надеюсь, ещё увидимся.");
        put("information", "\nСписок команд : \n" + "/info - информация о всех командах.\n" +
                "/saveon - включить сохранение диалога.\n" + "/saveoff - отключить сохранение диалога.\n" +
                "/fileclean - очищение текстового файла.\n" + "/loaddialog - загрузить диалог из файла.\n" +
                "/dollar - курс доллара.\n" + "/euro - курс евро.\n" + "/weather - погода в Чите.\n"+
                "/clear - очистка истории сообщений в окне.\n");
        put("saveon", "Сохранение диалога включено.");
        put("saveoff", "Сохранение диалога выключено.");
        put("fileclean", "Текстовый файл очищен.");
        put("clear", "История сообщений очищена.");
        try {
            put("coursedollar", readCourse(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            put("courseeuro", readCourse(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            put("weather", readWeather());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }};

    // Метод ответа бота
    @Override
    public String say(String message) {
        /* toLowerCase - опускаем все в нижний регистр для удобства работы
         * Разбивает сообщение на слова с помощью метода split("[ {,|}?]+"),
         * слова могут быть разделены любым количеством пробелов, запятых, вопросительных знаков, фигурных скобок или знаков вопроса.
         * Объединяет слова в строку с помощью метода String.join(" ", ...),
         * где первый параметр " " задает разделитель между словами, а второй параметр ...
         * представляет собой список слов, полученный на предыдущем шаге.
         */
        //todo
        String temp = String.join(" ", message.toLowerCase().split("[ {,|}?]+"));
        /* Интерфейс Map.Entry обозначает пару “ключ-значение” внутри словаря.
         * Метод entrySet() возвращает список всех пар в нашей HashMap,
         * перебираем именно пары, а не отдельно ключи или значения */

        //todo
        for (Map.Entry<String, String> i : keys.entrySet()) {
            /* создается объект Pattern, который компилируется из ключа i.getKey()
             Объект Pattern используется для поиска подстроки в других строках
             */
            pattern = Pattern.compile(i.getKey());
            /* С помощью matcher() проверяется, cодержит ли строка temp подстроку, соответствующую шаблону,
            заданному ключом i.getKey() и вовзращает объект matcher
            У объекта Matcher вызывается метод find(),
            который проверяет, есть ли совпадение подстроки в строке temp.
            Если совпадение найдено, то проверяется, равно ли значение i.getValue() строке "whattime" или "math"
             */
            if (pattern.matcher(temp).find())           // если пользователь запросил время
                if (i.getValue().equals("whattime")) {
                    DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
                    String time = formatter.format(new Date());
                    return time;
                } else if (i.getValue().equals("math")) { // если пользователь попросил вычислить мат. выражение
                    Calculate expr = new Calculate();     // объект класса для вычислений чисел из строки
                    double result = 0;
                    try {
                        result = expr.calcExpression(temp); // вызов метода вычислений и присвоение результата переменной
                    } catch (IllegalArgumentException e) {

                        return e.getMessage() + "\nЧисла и операции писать через пробел\nНапример: 1 * 2 + 3";
                    }
                    return "Результат = " + result;
                } else
                    return answersByKeys.get(i.getValue());
        }
        return "Я вас не понимаю.";
    }

    // Проверяет не запрашивает ли пользователь дополнительную ф-ю
    public String checkFunction(String message) throws IOException {
        String temp = new String();
        if (message.equals("/saveon")) {
            saveFlag = true;
        }
        if (message.equals("/saveoff")) {
            saveFlag = false;
        }
        if (message.equals("/fileclean")) {
            FileWriter fstream1 = new FileWriter("users/" + getUserName() + ".txt");   // конструктор с одним параметром - для перезаписи
            BufferedWriter out1 = new BufferedWriter(fstream1);                                // Класс BufferedWriter записывает текст в поток, предварительно буферизируя записываемые символы, тем самым снижая количество обращений к физическому носителю для записи данных.
            out1.write("");                                                                // очищаем, перезаписав поверх пустую строку
            out1.close();                                                                      // закрываем
        }
        if (message.equals("/loaddialog")) {
            return loadHistory();
        }

        return temp;
    }

    /* Метод загрузки имени из файла "names.txt".
     * Если файл "names.txt" не будет найден, то мы выбросим исключение типа FileNotFoundException.
     */
    @Override
    public String loadName() throws FileNotFoundException {
        File file = new File(filename);    // объект типа файл
        Scanner scanner = new Scanner(file);                 // считывает данные из источника, который ты для него укажешь. (Например из строки, из файла, из консоли)
        String name = null;
        while (scanner.hasNextLine()) {                      // hasNextLine() — проверяет, является ли следующая порция данных строкой
            name = scanner.nextLine();                       // Метод nextLine() обращается к источнику данных, находит там следующую строку и возвращает ее.
        }
        return name;
    }

    // Добавление истории переписки в файл
    @Override
    public void addHistory(String message) {
        try (FileWriter writer = new FileWriter("users/" + getUserName() + ".txt", true)) {
            // запись всей строки
            Date date = new Date();
            SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
            String text = "Дата переписки : " + formatForDateNow.format(date) + "\n" + message;
            writer.write(text);
            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    // Загрузка истории переписки
    @Override
    public String loadHistory() {
        String[] temp = new String[1];              // используется для временного хранения строк
        String result = new String();               // вся история переписки
        FileReader fr = null;                       // для чтения из файла
        try {
            fr = new FileReader("users/" + getUserName() + ".txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scan = new Scanner(fr);
        while (scan.hasNextLine()) {                   // используется для сканирования каждой строки файла
            temp[0] = scan.nextLine();
            result += temp[0] + "\n";
        }
        return result;
    }

    // Метод, который отслеживает хочет ли пользователь сохранить историю переписки
    public boolean isSaveFlag() {
        return saveFlag;
    }

    // Метод, который читает курс доллара и евро
    public String readCourse(int i) throws IOException {
        /* получение всех элементов на странице, которые имеют атрибут class со значением
        "mfd-master-header-left" и сохранение их в объекте Elements с именем elements */
        Document doc = Jsoup.connect("http://mfd.ru/currency/?currency=USD").get();
        Elements elements = doc.getElementsByAttributeValue("class", "mfd-master-header-left");
        /* получение первого элемента из коллекции elements и сохранение его в переменной element1*/
        Element element1 = elements.get(0);
        /*получение i-го дочернего элемента объекта element1 и извлечение
        текстового содержимого из него, сохранение результатов в переменной title*/
        String title = element1.child(i).text();
        return title;
    }

    // Метод, который читает погоду Читы
    public static String readWeather() throws IOException {
        Document doc = Jsoup.connect("https://yandex.ru/pogoda/chita").get();   // создает объект и загружает в него содержимое страницы с помощью библиотеки jsoup
        Element element = doc.selectFirst(".temp__value");  // Извлекает элемент, который имеет класс "temp__value", содержащий информацию о температуре
        String temperature = element.text();
        element = doc.selectFirst(".link__feelings");       //  Извлекает текстовое содержимое элемента с классом "link__feelings", который содержит информацию о состоянии погоды
        String description = element.text();
        return temperature + ", " + description;                    //  Возвращает строку, содержащую информацию о температуре и состоянии погоды
    }
}


