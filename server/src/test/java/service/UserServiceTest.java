package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


import java.util.Objects;


public class UserServiceTest {
    UserService service = new UserService(new MemoryDataAccess());

    @Test
    void registerUser() {
        UserData userData = new UserData("me", "1234","123@gmail.com");
        RegisterResult registerResult = service.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        assert(Objects.equals(registerResult.username(), "me"));

    }

    @Test void login() {
        UserData userData = new UserData("me", "1234","123@gmail.com");
        RegisterResult registerResult = service.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        LoginResult loginResult = service.login(new LoginRequest("me", "1234"));
        assertEquals("me", loginResult.username());

    }

    @Test void loginFail() {
        UserData userData = new UserData("me", "1234","123@gmail.com");
        RegisterResult registerResult = service.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        LoginResult loginResult = service.login(new LoginRequest("me", "123"));
        assertNull(loginResult);
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
