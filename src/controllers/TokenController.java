package controllers;

import Encrypters.*;
import com.google.gson.JsonObject;
import database.DBConnector;
import model.User;

import java.sql.SQLException;

/**
 * Opretter en instans af DBConnector og kalder alle metoder til AccesTokens.
 * Hver metode er forklaret med kommentarer i DBConnector.
 */

public class TokenController {

    DBConnector db = new DBConnector();

    public JsonObject authenticate(String username, String password) throws SQLException {
        // Authenticate the user using the credentials provided
        JsonObject jsonObject = new JsonObject();

        String token;
        User foundUser = db.authenticate(username, Digester.hashWithSalt(password));

        if (foundUser != null) {

            token = Crypter.buildToken("abcdefghijklmnopqrstuvxyz1234567890@&%!?", 25);

            db.addToken(token, foundUser.getUserID());

            jsonObject.addProperty("token", token);
            jsonObject.addProperty("usertype", foundUser.getUserType());
            jsonObject.addProperty("userid", foundUser.getUserID());
        } else {
            jsonObject = null;
        }
        //Retunerer en access token til klienten.
        return jsonObject;
    }

    public User getUserFromTokens(String token) throws SQLException {
        DBConnector db = new DBConnector();
        User user = db.getUserFromToken(token);
        db.close();
        return user;

    }

    public boolean deleteToken(String token) throws SQLException{
        DBConnector db = new DBConnector();
        boolean deleteToken = db.deleteToken(token);
        db.close();
        return deleteToken;

    }
}
