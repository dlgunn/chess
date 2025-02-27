package dataaccess;

public class MemoryDataAccess extends DataAccess {
    public MemoryDataAccess() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        userDAO = new MemoryUserDAO();
    }
}