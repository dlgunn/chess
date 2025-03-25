package client;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String url =  "http://localhost:" + port;
        facade = new ServerFacade(url);

    }

    @BeforeEach
    public void reset() {
        try {
            facade.clear();
        } catch (Exception e) {
            System.out.print("Could not clear server");
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void register() {
        String username = null;
        try {
            username = facade.register(new UserData("name", "pass", "email"));
        } catch (Exception e) {
            System.out.print("Register Failed");
        }
        assertEquals("name", username);
    }

    @Test
    void registerAlreadyTaken(){
        try {
            var username1 = facade.register(new UserData("name", "pass", "email"));
        } catch (Exception e) {
            System.out.print("Setup failed");
        }
        assertThrows(Exception.class, () -> facade.register(new UserData("name", "pass", "email")));

    }

    @Test
    void login() {
        String username1 = null;
        String username2 = null;
        try {
            username1 = facade.register(new UserData("name", "pass", "email"));
            username2 = facade.login(new UserData("name", "pass", null));
        } catch (Exception e) {
            System.out.print("Setup failed");
        }
        assertEquals(username1, username2);
    }

    @Test
    void loginWrongInfo() {
        try {
            var username1 = facade.register(new UserData("name", "pass", "email"));
        } catch (Exception e) {
            System.out.print("Setup failed");
        }
        assertThrows(Exception.class, () -> facade.login(new UserData("name", "badPass", null)));

    }

    @Test
    void logout() {
        try {
            facade.register(new UserData("name", "pass", "email"));
            facade.createGame("newGame");
            facade.logout();
        } catch (Exception e) {
            System.out.print("Setup failed");
        }
        assertThrows(Exception.class, () -> facade.createGame("newGame"));
    }

    @Test
    void logoutFail() {
        assertThrows(Exception.class, () -> facade.logout());
    }

    @Test
    void createGame() {
        try {
            facade.register(new UserData("name", "pass", "email"));
            facade.createGame("gameName");
            assertEquals(1, facade.listGames().length);
        } catch (Exception e) {
            System.out.print("Setup failed");
        }
    }

    @Test
    void createGameFail() {
        assertThrows(Exception.class, () -> facade.createGame("newGame"));
    }

    @Test
    void listGames() {
        try {
            facade.register(new UserData("name", "pass", "email"));
            facade.createGame("gameName");
            facade.createGame("game2");
            facade.createGame("game3");
            assertEquals(3, facade.listGames().length);
        } catch (Exception e) {
            System.out.print("Setup Failed");
        }
    }

    @Test
    void listGamesUnauthorized() {
        assertThrows(Exception.class, () -> facade.listGames());
    }

    @Test
    void joinGame() {
        GameData gameData;
        try {
            facade.register(new UserData("name", "pass", "email"));
            facade.createGame("gameName");
            gameData = facade.joinGame(1, ChessGame.TeamColor.WHITE);
            assertEquals("gameName", gameData.gameName());

        } catch (Exception e) {
            System.out.print("Setup failed");
        }

    }

    @Test
    void joinGameFail() {
        assertThrows(Exception.class, () -> facade.joinGame(1, ChessGame.TeamColor.BLACK));
    }

    @Test
    void observeGame() {
        try {
            facade.register(new UserData("name", "pass", "email"));
            facade.createGame("gameName");
            GameData gameData = facade.observeGame(1);
            assertEquals("gameName", gameData.gameName());
        } catch (Exception e) {
            System.out.print("Setup failed");
        }
    }

    @Test
    void observeGameFail() {
        assertThrows(Exception.class, () -> facade.observeGame(1));
    }

    @Test
    void clear() {
        try {
            facade.register(new UserData("name", "pass", "email"));
            facade.createGame("gameName");
            facade.clear();
            assertThrows(Exception.class, () -> facade.joinGame(1, ChessGame.TeamColor.BLACK));
        } catch (Exception e) {
            System.out.print("Setup failed");
        }
    }

}
