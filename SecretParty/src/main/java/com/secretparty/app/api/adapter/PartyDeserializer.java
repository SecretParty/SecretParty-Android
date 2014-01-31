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

package com.secretparty.app.api.adapter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.secretparty.app.db.ThematicRepository;
import com.secretparty.app.models.Party;
import com.secretparty.app.models.Thematic;
import com.secretparty.app.models.User;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MagicMicky on 30/01/14.
 */
public class PartyDeserializer implements JsonDeserializer<Party> {
    private final ThematicRepository thematicRepository;

    public PartyDeserializer(ThematicRepository thematicRepository) {
        this.thematicRepository = thematicRepository;
    }
    @Override
    public Party deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        int id = obj.get("id").getAsInt();
        int length = obj.get("length").getAsInt();
        String name = obj.get("name").getAsString();
        long timestamp = obj.get("date").getAsLong();
        List<User> userList = new ArrayList<User>();
        JsonArray users = obj.get("users").getAsJsonArray();
        Gson gson = new Gson();
        for(int i =0;i<users.size();i++) {
            User u = gson.fromJson(users.get(i).getAsJsonObject().get("user").getAsJsonObject(),User.class);
            userList.add(u);
        }
        Thematic t = thematicRepository.get(obj.get("thematic_id").getAsInt());
        Log.v("Called", "called");
        return new Party(id,name,timestamp,length,userList,t);
    }
}
