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
import com.secretparty.app.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by david on 26/01/2014.
 */
public class UserAdpater {

    public static User parseUser(JSONObject jsonUser) throws JSONException {
        User u = new User();
        u.setId(jsonUser.getInt("id"));
        u.setName(jsonUser.getString("name"));
        return u;
    }
}
