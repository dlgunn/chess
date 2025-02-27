package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.UserData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.Objects;


public class UserServiceTest {
    UserService service = new UserService(new MemoryDataAccess());

    @Test
    void registerUser() {
        UserData userData = new UserData("me", "1234","123@gmail.com");
        RegisterResult registerResult = service.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        assert(Objects.equals(registerResult.username(), "me"));

    }

    @Test
    void clear() {
        service.register(new RegisterRequest("me", "1234", "12@gmail.com"));
        service.register(new RegisterRequest("you", "4321", "13@gmail.com"));
        service.register(new RegisterRequest("cat", "124342342", "23@gmail.com"));

        service.clear();
        assertEquals(new MemoryDataAccess(), service.dataAccess);

    }


}
