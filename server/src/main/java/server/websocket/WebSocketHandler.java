package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.SQLDataAccess;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final DataAccess dataAccess;

    public WebSocketHandler() {
        {
            try {
                dataAccess = new SQLDataAccess();
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }



    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session);
            case MAKE_MOVE -> makeMove(message, session);
            case LEAVE -> leave(command, session);
            case RESIGN -> resign(command, session);
        }
    }

    private void leave(UserGameCommand command, Session session) {
        try {
            GameData gameData = dataAccess.gameDAO.getGame(command.getGameID());
            String username = dataAccess.authDAO.getAuth(command.getAuthToken()).username();
            String whiteUsername = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();
            if (Objects.equals(whiteUsername, username)) {
                whiteUsername = null;
            }
            if (Objects.equals(blackUsername,username)) {
                blackUsername = null;
            }

            dataAccess.gameDAO.updateGame(new GameData(gameData.gameID(), whiteUsername,blackUsername,gameData.gameName(),gameData.game()));
            connections.broadcast(command.getAuthToken(), new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, username + " left the game"), gameData.gameID());
            connections.remove(command.getAuthToken(), gameData.gameID());
        } catch (DataAccessException e) {
            System.out.print("Could not update game");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resign(UserGameCommand command, Session session) {
        try {
            GameData gameData = dataAccess.gameDAO.getGame(command.getGameID());
            String username = dataAccess.authDAO.getAuth(command.getAuthToken()).username();
            if (!gameData.stillPlaying()) {
                session.getRemote().sendString(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Game is not in play").toString());
                return;
            }
            if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
                session.getRemote().sendString(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Not authorized").toString());
                return;
            }
            GameData endGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game(), false);
            dataAccess.gameDAO.updateGame(endGameData);
            connections.broadcast("", new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, username + " has resigned"), gameData.gameID());
        } catch (DataAccessException e) {
            System.out.print("Could not access game");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void makeMove(String message, Session session) throws IOException {
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);

        ChessMove move = command.getChessMove();
        GameData gameData;
        try {
            AuthData authData = dataAccess.authDAO.getAuth(command.getAuthToken());
            if (authData == null) {
                session.getRemote().sendString(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Not authorized").toString());
                return;
            }
            gameData = dataAccess.gameDAO.getGame(command.getGameID());
            if (!gameData.stillPlaying()) {
                session.getRemote().sendString(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "The game is over").toString());
                return;
            }
            String user = authData.username();
            String correctUsername;
            ChessGame.TeamColor pieceColor = gameData.game().getBoard().getPiece(move.getStartPosition()).getTeamColor();
            if (pieceColor == ChessGame.TeamColor.WHITE) {
                 correctUsername = gameData.whiteUsername();
            } else {
                correctUsername = gameData.blackUsername();
            }
            if (!Objects.equals(user, correctUsername)) {
                session.getRemote().sendString(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Not authorized").toString());
                return;
            }

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            gameData.game().makeMove(move);
            dataAccess.gameDAO.updateGame(gameData);
            connections.broadcast("", new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData), gameData.gameID());
            connections.broadcast(command.getAuthToken(), new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, new Gson().toJson(move)), gameData.gameID());
        } catch (InvalidMoveException e) {
            String error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Invalid Move").toString();
            session.getRemote().sendString(error);
            return;
        } catch (DataAccessException e) {
            String error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Couldn't update game").toString();
            session.getRemote().sendString(error);
            return;
        }
//        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "make move");
//        //probably fix this next line
//        connections.broadcast(command.getAuthToken(), notificationMessage);
        NotificationMessage notificationMessage;
        GameData endGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game(), false);
        if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)) {
            notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "White is in checkmate");
            connections.broadcast("", notificationMessage, gameData.gameID());
            try {
                dataAccess.gameDAO.updateGame(endGameData);
            } catch (DataAccessException e) {
                System.out.print("Could not update game");
            }
        } else if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE)) {
            notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "White is in check");
            connections.broadcast("", notificationMessage, gameData.gameID());
        }

        if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Black is in checkmate");
            connections.broadcast("", notificationMessage, gameData.gameID());
            try {
                dataAccess.gameDAO.updateGame(endGameData);
            } catch (DataAccessException e) {
                System.out.print("Could not update game");
            }
        } else if (gameData.game().isInCheck(ChessGame.TeamColor.BLACK)) {
            notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Black is in check");
            connections.broadcast("", notificationMessage, gameData.gameID());
        }

        if (gameData.game().isInStalemate(ChessGame.TeamColor.BLACK)) {
            notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Black is in stalemate");
            connections.broadcast("", notificationMessage, gameData.gameID());
            try {
                dataAccess.gameDAO.updateGame(endGameData);
            } catch (DataAccessException e) {
                System.out.print("Could not update game");
            }

        }

        if (gameData.game().isInStalemate(ChessGame.TeamColor.WHITE)) {
            notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "White is in stalemate");
            connections.broadcast("", notificationMessage, gameData.gameID());
            try {
                dataAccess.gameDAO.updateGame(endGameData);
            } catch (DataAccessException e) {
                System.out.print("Could not update game");
            }
        }



    }

    private void connect(UserGameCommand userGameCommand, Session session) throws IOException, DataAccessException {
        connections.add(userGameCommand.getAuthToken(), session, userGameCommand.getGameID());
        GameData gameData = dataAccess.gameDAO.getGame(userGameCommand.getGameID());
        if (gameData == null) {
            String message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "This game doesn't exist").toString();
            session.getRemote().sendString(message);
            return;
        }
        AuthData authData = dataAccess.authDAO.getAuth(userGameCommand.getAuthToken());
        if (authData == null) {
            String message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Not authorized").toString();
            session.getRemote().sendString(message);
            return;
        }
        String whoJoined;
        if (Objects.equals(authData.username(), gameData.whiteUsername())) {
            whoJoined = "white";
        } else if (Objects.equals(authData.username(), gameData.blackUsername())) {
            whoJoined = "black";
        } else {
            whoJoined = "an observer";
        }

        String msg = new Gson().toJson(new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData));
        session.getRemote().sendString(msg);
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, authData.username() + " joined as " + whoJoined);
        //probably fix this next line
        connections.broadcast(userGameCommand.getAuthToken(), notificationMessage, gameData.gameID());
    }
}
