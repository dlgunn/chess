package dataaccess;
import model.AuthData;
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


}
