package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(GameData gamedata);

    GameData getGame(int gameID);

    Collection<GameData> listGames();

    void updateGame(GameData gameData);

    void clear();

}
