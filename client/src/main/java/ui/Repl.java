package ui;

import java.util.Scanner;
import static ui.EscapeSequences.*;


public class Repl {
    private PregameClient client;

    public Repl(String serverUrl) {
        this.client = new PregameClient();
    }

    public void run() {
        System.out.println("Welcome to chess. Type Help to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + SET_TEXT_COLOR_LIGHT_GREY + ">>> " + SET_TEXT_COLOR_YELLOW);
    }
}
