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

package com.secretparty.app.api;

import com.secretparty.app.models.Party;
import com.secretparty.app.models.Thematic;
import com.secretparty.app.models.User;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by david on 28/01/2014.
 */
public interface APIService {
    @GET("/thematics")
    void listThematics(Callback<ArrayList<Thematic>> thematicCallback);

    @GET("/party/{id}")
    void getParty(@Path("id") int id,Callback<Party> partyCallback);

    @FormUrlEncoded
    @POST("/party")
    void createParty(@Field("party_user[party][name]") String partyName,
                      @Field("party_user[party][length]") Integer length,
                      @Field("party_user[party][thematic]") Integer idThematic,
                      @Field("party_user[user][name]") String userName,
                      @Field("party_user[user][secret]") Integer idSecret, Callback<Party> partyCallback);

    @FormUrlEncoded
    @POST("/user")
    void joinParty(@Field("user[name]") String name,
                   @Field("user[secret]") Integer idSecret,
                   @Field("user[party]") Integer idParty, Callback<User> userCallback);
}
