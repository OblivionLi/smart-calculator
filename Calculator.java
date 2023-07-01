package calculator;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private final Map<String, BigInteger> history;

    public Calculator() {
        this.history = new HashMap<>();
    }

    public String performOperation(String input) {
        String sanitizedInput = this.sanitizeInput(input);
        String[] inputParts = input.split(" ");

        if (this.isOperationAssignment(sanitizedInput)) {
            String handleAssignment = this.handleAssignment(sanitizedInput);
            if (!handleAssignment.isEmpty()) {
                return handleAssignment;
            }
        }

        if (inputParts.length == 1) {
            if (this.isValueAlphabetic(input)) {
                return this.getValueFromHistory(input);
            }

            if (input.matches("-?\\d+")) {
                return input;
            }
        }

        if (!input.contains("=")) {
            return this.calculate(input);
        }

        return "";
    }

    private String sanitizeInput(String input) {
        return input.replaceAll("\\s", "")
                .replaceAll("", " ").trim();
    }

    private boolean isOperationAssignment(String operation) {
        return operation.contains("=");
    }

    private String handleAssignment(String assignment) {
        assignment = assignment.replaceAll("\\s", "");
        String[] assignmentParts = assignment.split("=");

        // validate identifier
        boolean isIdentifierInvalid = this.isIdentifierInvalid(assignmentParts[0]);
        if (isIdentifierInvalid) {
            return "Invalid identifier";
        }

        boolean isAssignmentValid = this.isAssignmentValid(assignment);
        if (assignmentParts.length > 2 || !isAssignmentValid) {
            return "Invalid assignment";
        }

        return this.assignVar(assignment);
    }

    private boolean isAssignmentValid(String input) {
        Pattern pattern = Pattern.compile("=");
        Matcher matcher = pattern.matcher(input);

        int count = 0;
        while (matcher.find()) {
            count++;
        }

        if (count > 1) {
            return false;
        }

        return this.checkAssignment(input);
    }

    private boolean checkAssignment(String input) {
        String[] inputParts = input.split("=");
        Pattern pattern = Pattern.compile(".*[a-zA-Z].*\\d.*|.*\\d.*[a-zA-Z].*");
        Matcher matcher = pattern.matcher(inputParts[1]);

        return !matcher.find();
    }

    private String assignVar(String input) {
        String[] inputParts = input.split("=");
        String firstValue = inputParts[0];
        String secondValue = inputParts[1];

        // need to check if second value (value after `=` is number or letter(s))
        boolean isValueAlphabetic = this.isValueAlphabetic(secondValue);
        if (isValueAlphabetic) {
            String getValueFromHistory = this.getValueFromHistory(secondValue);
            if (getValueFromHistory.equalsIgnoreCase("unknown variable")) {
                return getValueFromHistory;
            }

            BigInteger value = new BigInteger(getValueFromHistory);
            this.history.put(firstValue, value);
        } else {
            this.history.put(firstValue, new BigInteger(secondValue));
        }

        return "";
    }

    private boolean isValueAlphabetic(String input) {
        Pattern letterPattern = Pattern.compile("[a-zA-Z]+");
        Matcher letterMatcher = letterPattern.matcher(input);

        return letterMatcher.matches();
    }

    private String getValueFromHistory(String key) {
        return this.history.containsKey(key) ? String.valueOf(this.history.get(key)) : "Unknown variable";
    }

    private boolean isIdentifierInvalid(String input) {
        Pattern pattern = Pattern.compile("\\d+|[^a-zA-Z]");
        Matcher matcher = pattern.matcher(input);

        return matcher.find();
    }

    private String calculate(String input) {
        Deque<String> postfixExpression = this.curatedPostfixExpression(input);
        if (postfixExpression.isEmpty()) {
            return "Invalid expression";
        }

        Deque<BigInteger> stack = new ArrayDeque<>();
        for (String element : postfixExpression) {
            if (this.isOperator(element)) {
                BigInteger operand2 = stack.pop();
                BigInteger operand1 = stack.pop();
                BigInteger result = this.calculateElements(operand1, operand2, element);
                stack.push(result);
            } else {
                BigInteger number = new BigInteger(element);
                stack.push(number);
            }
        }

        return stack.pop().toString();
    }

    private Deque<String> curatedPostfixExpression(String input) {
        Deque<String> postfixExpression = StringUtils.getPostfixExpression(input);
        if (postfixExpression.isEmpty()) {
            return new ArrayDeque<>();
        }

        StringBuilder curatedStackString = new StringBuilder();

        for (String element : postfixExpression) {
            if (element.matches("[*+\\-/^]")) {
                curatedStackString.append(element).append("|");
                continue;
            }

            if (element.matches("[a-zA-Z]+")) {
                String getValueFromHistory = this.getValueFromHistory(element);
                if (getValueFromHistory.equalsIgnoreCase("unknown variable")) {
                    return new ArrayDeque<>();
                }

                curatedStackString.append(getValueFromHistory).append("|");
            } else {
                curatedStackString.append(element).append("|");
            }
        }

        Deque<String> curatedStack = new ArrayDeque<>();
        String[] parts = curatedStackString.toString().split("\\|");
        for (int i = parts.length - 1; i >= 0; i--) {
            curatedStack.push(parts[i]);
        }

        return curatedStack;
    }

    private boolean isOperator(String input) {
        return input.matches("[*+\\-/^]");
    }

    private BigInteger calculateElements(BigInteger operand1, BigInteger operand2, String operator) {
        switch (operator) {
            case "+" -> {
                return operand1.add(operand2);
            }
            case "-" -> {
                return operand1.subtract(operand2);
            }
            case "*" -> {
                return operand1.multiply(operand2);
            }
            case "/" -> {
                return operand1.divide(operand2);
            }
            case "^" -> {
                return operand1.pow(operand2.intValue());
            }
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}
