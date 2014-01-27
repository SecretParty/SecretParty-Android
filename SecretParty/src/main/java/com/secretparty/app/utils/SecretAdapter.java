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

import com.secretparty.app.models.Secret;
import com.secretparty.app.models.Thematic;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by david on 26/01/2014.
 */
public class SecretAdapter {

    public static Secret parseSecret(JSONObject jsonSecret, Thematic t) throws JSONException {
        Secret s = new Secret();
        s.setId(jsonSecret.getInt("id"));
        s.setName(jsonSecret.getString("name"));
        s.setHint(jsonSecret.getString("hint"));
        s.setThematic(t);
        return s;
    }
}
