package ui;

import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

public class PregameClient {
    private State state = State.SIGNEDOUT;
    private ServerFacade server;

    public PregameClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }


    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
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
        if (state == State.SIGNEDOUT) {
            return """
                    - help
                    - quit
                    - login
                    - register
                    """;
        }
        return """
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
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
    public String register(String... params) throws Exception {
        if (params.length >= 2) {
            var name = params[0];
            var userData = new UserData(params[0],params[1],params[2]);
             userData = server.register(userData);
            return String.format("You logged in as %s.", userData.username());
        }
        throw new Exception();
    }
}
