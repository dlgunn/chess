package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class ServiceTest {
    static Service service = new Service(new MemoryDataAccess());

    @BeforeEach
    void reset() {
        service.clear();
    }

    @Test
    void registerUser() throws DataAccessException {

        UserData userData = new UserData("me", "1234","123@gmail.com");
        RegisterResult registerResult = service.userService.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        assert(Objects.equals(registerResult.username(), "me"));

    }

    @Test
    void registerUserThrowsException() throws DataAccessException {
        UserData userData = new UserData("me2", "1234","123@gmail.com");
        RegisterResult registerResult = service.userService.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        assertThrows(DataAccessException.class, () -> {
            RegisterResult registerResult2 = service.userService.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        });
    }

    @Test void login() throws DataAccessException {
        UserData userData = new UserData("me3", "1234","123@gmail.com");
        RegisterResult registerResult = service.userService.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        LoginResult loginResult = service.userService.login(new LoginRequest("me3", "1234"));
        assertEquals("me3", loginResult.username());

    }

    @Test void loginThrowsException() throws DataAccessException {
        UserData userData = new UserData("me", "1234","123@gmail.com");
        RegisterResult registerResult = service.userService.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        DataAccessException ex = assertThrows(DataAccessException.class, () -> {
            LoginResult registerResult2 =  service.userService.login(new LoginRequest(userData.username(), "cat"));
        });
        assertEquals("Error: unauthorized", ex.getMessage());
    }

    @Test
    void clear() throws DataAccessException {
        service.userService.register(new RegisterRequest("me", "1234", "12@gmail.com"));
        service.userService.register(new RegisterRequest("you", "4321", "13@gmail.com"));
        service.userService.register(new RegisterRequest("cat", "124342342", "23@gmail.com"));

        service.clear();
        var actual = service.dataAccess.userDAO.getUser("me");
        assertNull(actual);

    }

    @Test
    void logout() throws DataAccessException {
        RegisterResult registerResult = service.userService.register(new RegisterRequest("me", "1234", "12@gmail.com"));
        service.userService.logout(registerResult.authToken());
        assertNull(service.dataAccess.authDAO.getAuth(registerResult.authToken()));
    }

    @Test
    void logoutThrowsException() throws DataAccessException {
        DataAccessException ex = assertThrows(DataAccessException.class, () -> {
            service.userService.logout("fakeAuthToken");
        });
        assertEquals("Error: unauthorized", ex.getMessage());
    }

    @Test
    void createGame() throws DataAccessException {
        RegisterResult registerResult = service.userService.register(new RegisterRequest("me", "1234", "12@gmail.com"));
        service.gameService.createGame(new CreateGameRequest("yooo"));
    }

    @Test
    void createGameThrowsException() throws DataAccessException {
        DataAccessException ex = assertThrows(DataAccessException.class, () -> {
            service.gameService.createGame(new CreateGameRequest(null));
        });
        assertEquals("Error: bad request", ex.getMessage());
    }

    @Test
    void joinGame() throws DataAccessException {
        RegisterResult registerResult = service.userService.register(new RegisterRequest("me", "1234", "12@gmail.com"));
        service.gameService.createGame(new CreateGameRequest("yooo"));
        service.gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE,1), "me");
        Iterator<GameData> iterator = service.gameService.listGames().games().iterator();
        GameData gameData = iterator.next();
        assertEquals("me", gameData.whiteUsername());
    }

}
