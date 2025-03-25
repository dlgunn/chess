package server;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;


public class ServerFacade {
    private String serverUrl;
    private String authToken;
    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void logout() throws Exception {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
        authToken = null;
    }

    public String register(UserData userData) throws Exception {
        var path = "/user";
        record registerResponse(String username, String authToken) {
        }
        var response = this.makeRequest("POST", path, userData, registerResponse.class , null);
        authToken = response.authToken;
        return response.username;
    }

    public String createGame(String gameName) throws Exception {
        var path = "/game";
        GameData gameData = new GameData(0,null,null,gameName,null);
        var response = this.makeRequest("POST", path, gameData, GameData.class , authToken);
        return response.gameName();
    }

    public String login(UserData userData) throws Exception {
        var path = "/session";
        record loginResponse(String username, String authToken) {
        }
        var response = this.makeRequest("POST", path, userData, loginResponse.class , null);
        authToken = response.authToken;
        return response.username;

    }




    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeAuthorization(http);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw ex;}
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }



    private void writeAuthorization(HttpURLConnection http) {
        if (authToken != null) {
            http.addRequestProperty("Authorization", authToken);
        }
    }


    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception{
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    String message = getMessageFromRespErr(respErr);
                    throw new Exception(message);
//                    fix this line later.
//                    throw ResponseException.fromJson(respErr);
                }
            }
            throw new Exception();
            // also fix this line
//            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    private String getMessageFromRespErr(InputStream respErr) {
        var map = new Gson().fromJson(new InputStreamReader(respErr), HashMap.class);
        return map.get("message").toString();
    }
}
