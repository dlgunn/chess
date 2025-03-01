package dataaccess;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.RegisterRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataAccessTest {
    DataAccess dataAccess = new MemoryDataAccess();

    @Test
    void createUser() throws DataAccessException {
        UserData userData = new UserData("me", "12345","email");
        assertEquals(userData, dataAccess.userDAO.createUser(userData));
    }

    @Test
    void createUserThrowsException() {
        Exception ex = assertThrows(DataAccessException.class, () -> {
            dataAccess.userDAO.createUser(new UserData(null, null, null));
        });
        assertEquals("Error: bad request", ex.getMessage());
    }


}
