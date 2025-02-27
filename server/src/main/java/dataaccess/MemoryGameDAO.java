package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    int id = 0;
    final private HashMap<Integer, GameData> allGameData = new HashMap<>();

    @Override
    public GameData createGame(GameData gameData) {
        gameData = new GameData(id, gameData.whiteUsername(),gameData.blackUsername(), gameData.gameName(), gameData.game());
        allGameData.put(++id, gameData);
        return gameData;
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
    public void updateGame(int gameID) {

    }

    @Override
    public void clear() {

    }
}
