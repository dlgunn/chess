package ui;

import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

public class PostLoginClient extends Client {
    private ServerFacade server;

    public PostLoginClient(ServerFacade server) {
        this.server = server;
    }


    @Override
    public String eval(String input, Repl repl) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout(repl, params);
//                case "login" -> signIn(params);
//                case "rescue" -> rescuePet(params);
//                case "list" -> listPets();
//                case "signout" -> signOut();
//                case "adopt" -> adoptPet(params);
//                case "adoptall" -> adoptAllPets();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String logout(Repl repl, String[] params) throws Exception {
        if (params.length == 0) {
            server.logout();
            repl.setClient(new PostLoginClient(server));
            return "You have been logged out";
        }
        throw new Exception();
    }

    public String help() {
        return """
                    Possible Commands (case insensitive)
                    - create <GAME NAME>
                    - list
                    - join <ID> [WHITE][BLACK]
                    - observe <ID>
                    - logout
                    - quit
                    - help
                    """;
    }
}
