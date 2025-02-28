package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {
    int id = 0;
    final private HashMap<Integer, GameData> allGameData = new HashMap<>();

    @Override
    public GameData createGame(GameData gameData) {
        gameData = new GameData(++id, gameData.whiteUsername(),gameData.blackUsername(), gameData.gameName(), gameData.game());
        allGameData.put(id, gameData);
        return gameData;
    }

    @Override
    public GameData getGame(int gameID) {
        return allGameData.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return new ArrayList<>(allGameData.values());
    }

    @Override
    public void updateGame(GameData gameData) {
        allGameData.put(gameData.GameID(),gameData);
    }

    @Override
    public void clear() {
        allGameData.clear();
    }
}
