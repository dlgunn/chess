package dataaccess;

public class DataAccess {
    public GameDAO gameDAO;
    public AuthDAO authDAO;
    public UserDAO userDAO;

    public void clear() {
        gameDAO.clear();
        authDAO.clear();
        userDAO.clear();
    }
}
