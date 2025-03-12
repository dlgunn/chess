package dataaccess;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTest {
    DataAccess dataAccess;

    public DataAccessTest() {
        dataAccess = new MemoryDataAccess();
    }

    private void setDataAccess(String type) throws DataAccessException {
        if (Objects.equals(type, "SQL")) {
            dataAccess = new SQLDataAccess();
            dataAccess.clear();
        }
    }

    @BeforeEach
    public void reset() throws DataAccessException {
        dataAccess.clear();
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void createUser(String type) throws DataAccessException {
        setDataAccess(type);
        UserData userData = new UserData("me", "12345","email");
        assertEquals(userData, dataAccess.userDAO.createUser(userData));
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void createAuthData(String type) throws DataAccessException {
        setDataAccess(type);
        DataAccess tempDataAccess = new SQLDataAccess();
        AuthData authData = new AuthData("random", "myself");
        AuthData returnAuthData = tempDataAccess.authDAO.createAuth(authData);
        assertEquals("myself", returnAuthData.username());
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void createUserThrowsException(String type) throws DataAccessException {
        setDataAccess(type);
        Exception ex = assertThrows(DataAccessException.class, () -> dataAccess.userDAO.createUser(new UserData(null, null, null)));
        assertEquals("Error: bad request", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void getUser(String type) throws DataAccessException {
        setDataAccess(type);
        UserData userData = new UserData("me", "12345","email");
        dataAccess.userDAO.createUser(userData);
        assertEquals(userData.email(), dataAccess.userDAO.getUser("me").email());
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void getUserFail(String type) throws DataAccessException {
        setDataAccess(type);
        UserData userData = new UserData("me", "12345","email");
        dataAccess.userDAO.createUser(userData);
        assertNull(dataAccess.userDAO.getUser("you"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void clear(String type) throws DataAccessException {
        setDataAccess(type);
        UserData userData = new UserData("me", "12345","email");
        dataAccess.userDAO.createUser(userData);
        dataAccess.clear();
        assertNull(dataAccess.userDAO.getUser("me"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void getAuth(String type) throws DataAccessException {
        setDataAccess(type);
        AuthData authData = new AuthData(null, "username");
        authData = dataAccess.authDAO.createAuth(authData);
        AuthData retrieved = dataAccess.authDAO.getAuth(authData.authToken());
        assertEquals(authData,retrieved);
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void getAuthFail(String type) throws DataAccessException {
        setDataAccess(type);
        AuthData authData = new AuthData(null, "username");
        authData = dataAccess.authDAO.createAuth(authData);
        AuthData retrieved = dataAccess.authDAO.getAuth("wrongAuthToken");
        assertNull(retrieved);
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void deleteAuth(String type) throws DataAccessException {
        setDataAccess(type);
        AuthData authData = new AuthData(null, "username");
        authData = dataAccess.authDAO.createAuth(authData);
        dataAccess.authDAO.deleteAuth(authData);
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void deleteAuthFail(String type) throws DataAccessException {
        setDataAccess(type);
        AuthData authData = new AuthData(null, "username");
        authData = dataAccess.authDAO.createAuth(authData);
        dataAccess.authDAO.deleteAuth(new AuthData("badAuth","username"));
        AuthData retrieved = dataAccess.authDAO.getAuth(authData.authToken());
        assertEquals(authData,retrieved);
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void createGame(String type) throws DataAccessException {
        setDataAccess(type);
        GameData gameData = dataAccess.gameDAO.createGame(new GameData(0,"name","name2","gameName",new ChessGame()));
        assertEquals("gameName",gameData.gameName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void createGameFail(String type) throws DataAccessException {
        setDataAccess(type);
        assertThrows(NullPointerException.class, () -> dataAccess.gameDAO.createGame(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"memory", "SQL"})
    void getGame(String type) throws DataAccessException {
        setDataAccess(type);
        GameData gameData = dataAccess.gameDAO.createGame(new GameData(0,"name","name2","gameName",new ChessGame()));
        GameData retrieved = dataAccess.gameDAO.getGame(gameData.gameID());
        assertEquals(gameData.gameName(),retrieved.gameName());
    }





}
