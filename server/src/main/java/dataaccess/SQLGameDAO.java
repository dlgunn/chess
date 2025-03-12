package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, json)";
        var json = new Gson().toJson(gameData);
        var id = SqlDataAccess.executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), json);
        return new GameData(id,gameData.whiteUsername(),gameData.blackUsername(), gameData.gameName(), gameData.game());
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(GameData gameData) {

    }

    @Override
    public void clear() {

    }
}
