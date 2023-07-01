package calculator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static Deque<String> getPostfixExpression(String infixExpression) {
        if (!isPostfixValid(infixExpression)) {
            return new ArrayDeque<>();
        }

        infixExpression = curateInfixExpression(infixExpression);

        Deque<String> postfixExpression = new ArrayDeque<>();
        Deque<Character> operatorStack = new ArrayDeque<>();

        String[] infixExpressionParts = infixExpression.split(" ");
        for (String currentElement : infixExpressionParts) {
            if (currentElement.matches("\\d+")) {
                postfixExpression.add(currentElement);
                continue;
            }

            if (currentElement.matches("[a-zA-Z]+")) {
                postfixExpression.add(currentElement);
                continue;
            }

            if (currentElement.equalsIgnoreCase("(")) {
                operatorStack.push(currentElement.charAt(0));
                continue;
            }

            if (currentElement.equalsIgnoreCase(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals('(')) {
                    postfixExpression.add(Character.toString(operatorStack.pop()));
                }

                if (!operatorStack.isEmpty() && operatorStack.peek().equals('(')) {
                    operatorStack.pop();
                }
                continue;
            }

            char op = currentElement.charAt(0);
            if (op == '^') {
                while (!operatorStack.isEmpty() && precedence(op) < precedence(operatorStack.peek())) {
                    postfixExpression.add(Character.toString(operatorStack.pop()));
                }
            } else {
                while (!operatorStack.isEmpty() && precedence(op) <= precedence(operatorStack.peek())) {
                    postfixExpression.add(Character.toString(operatorStack.pop()));
                }
            }

            operatorStack.push(op);
        }

        while (!operatorStack.isEmpty()) {
            postfixExpression.add(Character.toString(operatorStack.pop()));
        }

        return postfixExpression;
    }

    public static boolean isPostfixValid(String infixExpression) {
        if (infixExpression.isEmpty()) {
            return false;
        }

        String[] infixExpressionParts = infixExpression.split("");
        int countOpenedParenthesis = 0;
        int countClosedParenthesis = 0;
        for (String infixExpressionPart : infixExpressionParts) {
            if (infixExpressionPart.equals("(")) {
                countOpenedParenthesis++;
            }

            if (infixExpressionPart.equals(")")) {
                countClosedParenthesis++;
            }
        }

        if (countOpenedParenthesis != countClosedParenthesis) {
            return false;
        }

        String prev = infixExpressionParts[0];

        for (int i = 1; i < infixExpressionParts.length; i++) {
            String current = infixExpressionParts[i];
            if (isOperator(prev) && isOperator(current) && (prev.equals("*") || prev.equals("/") || prev.equals("^"))) {
                return false;
            }

            prev = current;
        }

        return true;
    }

    private static boolean isOperator(String token) {
        return token.equals("*") || token.equals("/") || token.equals("^");
    }

    private static String curateInfixExpression(String infixExpression) {
        infixExpression = infixExpression.replaceAll("\\++", "+");

        Pattern pattern = Pattern.compile("(-{2})+");
        Matcher matcher = pattern.matcher(infixExpression);

        StringBuilder output = new StringBuilder();
        while (matcher.find()) {
            int count = matcher.group().length();
            matcher.appendReplacement(output, count % 2 == 0 ? "+" : "-");
        }

        matcher.appendTail(output);
        infixExpression = output.toString();

        return infixExpression.replaceAll("\\+-+", "-")
                .replaceAll("([+\\-*/^()])", " $1 ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static int precedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> -1;
        };
    }
}
