package ui;

import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

public class PreLoginClient extends Client {
//    private State state = State.SIGNEDOUT;
    private ServerFacade server;

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

    public String help() {

        return """
                    Possible Commands (case insensitive)
                    - help
                    - quit
                    - login <username> <password>
                    - register <username> <password>
                    """;
    }

//    public String signIn(String... params)  {
//        if (params.length >= 1) {
//            state = State.SIGNEDIN;
//            visitorName = String.join("-", params);
//            ws.enterPetShop(visitorName);
//            return String.format("You signed in as %s.", visitorName);
//        }
//        throw new ResponseException(400, "Expected: <yourname>");
//    }
    public String register(Repl repl, String... params) throws Exception {
        if (params.length >= 2) {
            var userData = new UserData(params[0],params[1],params[2]);
             userData = server.register(userData);
             repl.setClient(new PostLoginClient(server));
            return String.format("You logged in as %s.", userData.username());
        }
        throw new Exception();
    }
}
