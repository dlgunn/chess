package server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerTest {
    Server server = new Server();

    @AfterEach
    public void reset() {
        server.stop();
    }


    @Test
    public void run() {
        assertEquals(8080, server.run(8080));
    }

    @Test
    public void stop() {
        server.run(8080);
        server.stop();
        assertEquals(8080, server.run(8080));
    }
}
