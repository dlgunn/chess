package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import model.UserData;
import org.junit.jupiter.api.Test;

import java.util.Objects;


public class UserServiceTest {
    UserService service = new UserService(new MemoryDataAccess());

    @Test
    void registerUser() {
        UserData userData = new UserData("me", "1234","123@gmail.com");
        RegisterResult registerResult = service.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        assert(Objects.equals(registerResult.username(), "me"));

    }


}
