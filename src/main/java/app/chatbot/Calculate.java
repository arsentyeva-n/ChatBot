/**
 * ChatBot
 *
 * @author Arsentyeva N. V.
 */

package app.chatbot;

import java.util.Deque;
import java.util.ArrayDeque;

public class Calculate {
    public double calcExpression(String input) {
        // Извлекаем математическое выражение из строки
        String expression = input.substring(input.indexOf("вычисли") + 8);

        // Разделяем выражение на отдельные элементы с помощью регулярного выражения
        String[] tokens = expression.split("\\s+");

        // Создаем стек для операторов и стек для чисел
        Deque<Character> operators = new ArrayDeque<>();
        Deque<Double> numbers = new ArrayDeque<>();

        // Проходим по всем элементам выражения
        for (String token : tokens) {
            // Если текущий элемент является оператором, помещаем его в стек операторов
            if (token.matches("[+\\-*/]")) {
                while (!operators.isEmpty() && hasPrecedence(operators.peek(), token.charAt(0))) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    char op = operators.pop();
                    double result = applyOperator(a, b, op);

                    // Помещаем результат вычисления в стек чисел
                    numbers.push(result);
                }

                operators.push(token.charAt(0));
            }
            // Если текущий элемент является числом, помещаем его в стек чисел
            else if (token.matches("\\d+(\\.\\d+)?")) {
                numbers.push(Double.parseDouble(token));
            }
            // Если текущий элемент не является ни оператором, ни числом, выбрасываем исключение
            else {
                throw new IllegalArgumentException("Неверное выражение: " + expression);
            }
        }

        // Выполняем оставшиеся операции в порядке их приоритета
        while (!operators.isEmpty()) {
            double b = numbers.pop();
            double a = numbers.pop();
            char op = operators.pop();
            double result = applyOperator(a, b, op);

            // Помещаем результат вычисления в стек чисел
            numbers.push(result);
        }

        // В стеке чисел должен остаться только один элемент, который представляет собой результат вычисления
        if (numbers.size() != 1) {
            throw new IllegalArgumentException("Неверное выражение: " + expression);
        }

        return numbers.pop();
    }

    // Метод, который определяет приоритет операторов
    private boolean hasPrecedence(char op1, char op2) {
        if (op1 == '*' || op1 == '/') {
            return true;
        } else if (op1 == '+' || op1 == '-') {
            return op2 == '+' || op2 == '-';
        }
        return false;
    }

    // Метод, который выполняет операцию между двумя числами с использованием оператора
    private double applyOperator(double a, double b, char op) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new IllegalArgumentException("Деление на ноль");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("Неверный оператор: " + op);
        }
    }

}