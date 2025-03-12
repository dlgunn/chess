package dataaccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTest {
    DataAccess dataAccess = new MemoryDataAccess();

    @BeforeEach
    public void reset() {
        dataAccess.clear();
    }

    @Test
    void createUser() throws DataAccessException {
        UserData userData = new UserData("me", "12345","email");
        assertEquals(userData, dataAccess.userDAO.createUser(userData));
    }

    @Test
    void createAuthData() throws DataAccessException {
        DataAccess tempDataAccess = new SqlDataAccess();
        AuthData authData = new AuthData("random", "myself");
        AuthData returnAuthData = tempDataAccess.authDAO.createAuth(authData);
        assertEquals("myself", returnAuthData.username());
    }

    @Test
    void createUserThrowsException() {
        Exception ex = assertThrows(DataAccessException.class, () -> {
            dataAccess.userDAO.createUser(new UserData(null, null, null));
        });
        assertEquals("Error: bad request", ex.getMessage());
    }

    @Test
    void getUser() throws DataAccessException {
        UserData userData = new UserData("me", "12345","email");
        dataAccess.userDAO.createUser(userData);
        assertEquals(userData, dataAccess.userDAO.getUser("me"));
    }

    @Test
    void getUserFail() throws DataAccessException {
        UserData userData = new UserData("me", "12345","email");
        dataAccess.userDAO.createUser(userData);
        assertNull(dataAccess.userDAO.getUser("you"));
    }

    @Test
    void clear() throws DataAccessException {
        UserData userData = new UserData("me", "12345","email");
        dataAccess.userDAO.createUser(userData);
        dataAccess.clear();
        assertNull(dataAccess.userDAO.getUser("me"));
    }


}
