/*
 * Copyright (C) 2014 Hugo DJEMAA / Jérémie BOUTOILLE / Mickael GOUBIN /
 *     David LIVET - http://github.com/SecretParty/SecretParty-Android
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

package com.secretparty.app;

import com.secretparty.app.models.Party;
import com.secretparty.app.models.Secret;
import com.secretparty.app.models.Thematic;
import com.secretparty.app.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class APISecretParty {


    public static ArrayList<Thematic> getThematics(String server){

        JSONArray json = APIRestServer.doGet(server+"thematics.json");

        ArrayList<Thematic> list = new ArrayList<Thematic>();
        for(int i = 0; i< json.length();i++){
            try {
                list.add(parseThematic((JSONObject) json.get(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    private static Thematic parseThematic(JSONObject json) throws JSONException {
        Thematic t = new Thematic();
        t.setId(json.getInt("id"));
        t.setName(json.getString("name"));

        // Add Secret
        JSONArray jsonSecret = json.getJSONArray("secrets");
        ArrayList<Secret> listSecret = new ArrayList<Secret>();
        for(int i = 0; i<jsonSecret.length(); i++){
            listSecret.add(parseSecret((JSONObject) jsonSecret.get(i),t));
        }
        t.setSecrets(listSecret);

        // Add Parties
        JSONArray jsonParty = json.getJSONArray("party");
        ArrayList<Party> listParty = new ArrayList<Party>();
        for(int i = 0; i<jsonParty.length(); i++){
            listParty.add(parseParty((JSONObject) jsonParty.get(i),t));
        }
        t.setCurrentParties(listParty);
        return t;
    }

    private static Party parseParty(JSONObject jsonParty, Thematic t) throws JSONException {
        Party s = new Party();
        s.setId(jsonParty.getInt("id"));
        s.setName(jsonParty.getString("name"));
        s.setLength(jsonParty.getInt("length"));
        s.setThematic(t);

        JSONArray jsonUser = jsonParty.getJSONArray("users");
        ArrayList<User> listUser = new ArrayList<User>();
        for(int i=0 ; i<jsonUser.length() ; i++){
            listUser.add(parseUser((JSONObject) jsonUser.get(i),s));
        }
        return s;
    }

    private static Secret parseSecret(JSONObject jsonSecret, Thematic t) throws JSONException {
        Secret s = new Secret();
        s.setId(jsonSecret.getInt("id"));
        s.setName(jsonSecret.getString("name"));
        s.setHint(jsonSecret.getString("hint"));
        s.setThematic(t);
        return s;
    }

    private static User parseUser(JSONObject jsonUser, Party p) throws JSONException{
        User u = new User();
        u.setId(jsonUser.getInt("id"));
        u.setName(jsonUser.getString("name"));
        return u;
    }

    public static User joinParty(int partyid, int secret, String name)  {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", partyid);
            obj.put("party", secret);
            obj.put("secret", name);
            //TODO: call API and parse the response as a JSON user.
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
