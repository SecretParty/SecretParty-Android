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

import com.secretparty.app.models.Secret;
import com.secretparty.app.models.Thematic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class APISecretParty {


    public static ArrayList<Thematic> getThematics(String server){
        JSONArray json = APIRestServer.doGet(server+"thematics.json");
        ArrayList<Thematic> list = new ArrayList<Thematic>();
        for(int i = 0; i< json.length();i++){
            Thematic t = new Thematic();
            try {
                t.setId( ((JSONObject)json.get(i)).getInt("id") );
                t.setName(((JSONObject)json.get(i)).getString("name"));
                JSONArray jsonSecret = ((JSONObject) json.get(i)).getJSONArray("secrets");
                ArrayList<Secret> listSecret = new ArrayList<Secret>();
                for(int j = 0; j<jsonSecret.length(); j++){
                    Secret s = new Secret();
                    s.setId(((JSONObject)jsonSecret.get(i)).getInt("id"));
                    s.setName(((JSONObject) jsonSecret.get(i)).getString("name"));
                    s.setHint(((JSONObject) jsonSecret.get(i)).getString("indication"));
                    s.setThematic(t);
                    listSecret.add(s);
                }
                t.setSecrets(listSecret);
                list.add(t);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
