package calculator;

import java.util.Scanner;

public class UserInterface {
    private final Scanner scanner;
    private final Calculator calculator;

    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
        this.calculator = new Calculator();
    }

    public void boot() {
        while (true) {
            String userInput = this.scanner.nextLine();
            if (userInput.equalsIgnoreCase("/exit")) {
                System.out.println("Bye!");
                break;
            }

            if (userInput.equalsIgnoreCase("/help")) {
                System.out.println("The program calculates the addition, subtraction, multiplication, division, power of numbers and it also can calculate based on operators priority (including parenthesis)");
                continue;
            }

            if (userInput.isEmpty()) {
                continue;
            }

            if (userInput.charAt(0) == '/') {
                System.out.println("Unknown command");
                continue;
            }

            String result = this.calculator.performOperation(userInput);
            if (result.isEmpty()) {
                continue;
            }

            System.out.println(result);
        }
    }
}
