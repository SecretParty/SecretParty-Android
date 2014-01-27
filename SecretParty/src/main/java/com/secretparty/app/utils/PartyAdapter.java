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

import com.secretparty.app.models.Party;
import com.secretparty.app.models.Thematic;
import com.secretparty.app.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by david on 26/01/2014.
 */
public class PartyAdapter {

    public static Party parseParty(JSONObject jsonParty, Thematic t) throws JSONException {
        Party s = new Party();
        s.setId(jsonParty.getInt("id"));
        s.setName(jsonParty.getString("name"));
        s.setLength(jsonParty.getInt("length"));
        s.setThematic(t);

        JSONArray jsonUser = jsonParty.getJSONArray("users");
        ArrayList<User> listUser = new ArrayList<User>();
        for(int i=0 ; i<jsonUser.length() ; i++){
            listUser.add(UserAdpater.parseUser((JSONObject) jsonUser.get(i)));
        }
        return s;
    }

    public static Party parseParty(JSONObject jsonParty) throws JSONException {
        JSONObject jsonThematic = jsonParty.getJSONObject("thematic");
        Thematic t = new Thematic();
        t.setId(jsonThematic.getInt("id"));
        t.setName(jsonThematic.getString("name"));
        return parseParty(jsonParty, t);
    }
}
