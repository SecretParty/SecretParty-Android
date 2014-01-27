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

package com.secretparty.app.utils;

import android.util.Log;

import com.secretparty.app.models.Party;
import com.secretparty.app.models.Secret;
import com.secretparty.app.models.Thematic;
import com.secretparty.app.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ThematicAdpater {


    public static ArrayList<Thematic> getThematics(JSONArray json) throws JSONException {

        ArrayList<Thematic> list = new ArrayList<Thematic>();
        for(int i = 0; i< json.length();i++){
            list.add(parseThematic((JSONObject) json.get(i)));
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
            listSecret.add(SecretAdapter.parseSecret((JSONObject) jsonSecret.get(i),t));
        }
        t.setSecrets(listSecret);

        // Add Parties
        ArrayList<Party> listParty = new ArrayList<Party>();
        if(json.has("parties")) {
            JSONArray jsonParty = json.getJSONArray("parties");
            for(int i = 0; i<jsonParty.length(); i++){
                listParty.add(PartyAdapter.parseParty((JSONObject) jsonParty.get(i),t));
            }
        }
        t.setCurrentParties(listParty);
        return t;
    }


}
