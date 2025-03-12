package dataaccess;

import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO gameData (whiteUsername, blackUsername, gameName, json) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(gameData);
        var id = SQLDataAccess.executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), json);
        GameData newGameData = new GameData(id,gameData.whiteUsername(),gameData.blackUsername(), gameData.gameName(), gameData.game());
        updateGame(newGameData);
        return newGameData;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement =  "SELECT json FROM gameData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGameData(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(500, e.getMessage());
        }
        return null;
    }


    private GameData readGameData(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, GameData.class);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, json FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGameData(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(500,  e.getMessage());
        }
        return result;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        var statement = "UPDATE gameData " +
                "SET whiteUsername = ?, blackUsername = ?, gameName = ?, json = ? WHERE id = ?";
        var json = new Gson().toJson(gameData);
        var id = SQLDataAccess.executeUpdate(statement, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), json, gameData.gameID());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE gameData";
        SQLDataAccess.executeUpdate(statement);
    }
}
