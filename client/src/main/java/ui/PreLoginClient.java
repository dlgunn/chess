package ui;

import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

public class PreLoginClient extends Client {
//    private State state = State.SIGNEDOUT;
    private final ServerFacade server;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public PreLoginClient(ServerFacade server) {
        this.server = server;
    }


    public String eval(String input, Repl repl) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(repl, params);
                case "login" -> login(repl, params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String login(Repl repl, String[] params) throws Exception {
        if (params.length == 2) {
            var userData = new UserData(params[0],params[1],null);
            String username = server.login(userData);
            repl.setClient(new PostLoginClient(server));
            return String.format("You logged in as %s.", username);
        }

        throw new Exception("Wrong number of arguments");
    }

    public String help() {

        return """
                    Possible Commands (case insensitive)
                    - help
                    - quit
                    - login <username> <password>
                    - register <username> <password> <email>
                    """;
    }
    public String register(Repl repl, String... params) throws Exception {
        if (params.length == 3) {
            var userData = new UserData(params[0],params[1],params[2]);
             String username = server.register(userData);
             repl.setClient(new PostLoginClient(server));
            return String.format("You logged in as %s.", username);
        }
        throw new Exception("Wrong number of arguments");
    }
}
