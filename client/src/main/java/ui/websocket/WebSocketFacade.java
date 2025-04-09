package ui.websocket;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import ui.Client;
import ui.Repl;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    private ChessGame.TeamColor color;
//    NotificationHandler notificationHandler;


    public WebSocketFacade(String url) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
//            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    handleNotification(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void join(String authToken, int gameID, ChessGame.TeamColor color) {
        this.color = color;
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken, gameID);
        try {
            this.session.getBasicRemote().sendText(command.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void makeMove(ChessMove move, String authToken, int gameID) throws Exception {
        try {
            MakeMoveCommand command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE,authToken, gameID,move);
            this.session.getBasicRemote().sendText(command.toString());
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws Exception {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        try {
            this.session.getBasicRemote().sendText(command.toString());
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void handleNotification(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case ERROR -> {
                ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                System.out.print(errorMessage.getErrorMessage());
                Repl.printPrompt();
            }
            case LOAD_GAME -> {
                LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                System.out.print("\n");
                Client.printBoard(loadGameMessage.getGame().game().getBoard(), color);
                Repl.printPrompt();
            }
            case NOTIFICATION -> {
                NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                ChessMove move = new Gson().fromJson(notificationMessage.getMessage(), ChessMove.class);
                if (move.getStartPosition() != null) {
                    System.out.print(move.toString());
                } else {
                    System.out.print(notificationMessage.getMessage());
                }
                Repl.printPrompt();
            }
        }
    }
}
