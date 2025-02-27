package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import model.GameData;

public class GameService {
    public final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        GameData gameData = this.dataAccess.gameDAO.createGame(new GameData(0,null,null, createGameRequest.gameName(), new ChessGame()));
        return new CreateGameResult(gameData.GameID());
    }
}
