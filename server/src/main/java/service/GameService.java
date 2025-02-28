package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.GameData;

public class GameService {
    public final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        if (createGameRequest.gameName() == null) {
            throw new DataAccessException(401, "Error: bad request");
        }
        GameData gameData = this.dataAccess.gameDAO.createGame(new GameData(0, null, null, createGameRequest.gameName(), new ChessGame()));
        return new CreateGameResult(gameData.gameID());
    }

    public ListGamesResult listGames() {
        return new ListGamesResult(this.dataAccess.gameDAO.listGames());
    }

    public void joinGame(JoinGameRequest joinGameRequest, String username) throws DataAccessException {
        GameData gameData = dataAccess.gameDAO.getGame(joinGameRequest.gameID());
        ChessGame.TeamColor color = joinGameRequest.playerColor();

        if (gameData == null || joinGameRequest.playerColor() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        String whiteUsername = username;
        String blackUsername = gameData.blackUsername();
        String existingUsername = gameData.whiteUsername();
        if (color == ChessGame.TeamColor.BLACK) {
            whiteUsername = gameData.whiteUsername();
            blackUsername = username;
            existingUsername = gameData.blackUsername();
        }
        if (existingUsername != null) {
            throw new DataAccessException(403, "Error: already taken");
        }
        dataAccess.gameDAO.updateGame(new GameData(gameData.gameID(), whiteUsername, blackUsername, gameData.gameName(), gameData.game()));
    }
}
