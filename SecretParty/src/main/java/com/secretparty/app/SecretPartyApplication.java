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

import android.app.Application;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.secretparty.app.api.APIService;
import com.secretparty.app.api.adapter.PartyDeserializer;
import com.secretparty.app.db.ThematicRepository;
import com.secretparty.app.models.Party;

import java.util.Date;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by david on 26/01/2014.
 */
public class SecretPartyApplication extends Application {

    private APIService apiService;
    private ThematicRepository thematicRepository;

    @Override
    public void onCreate(){
        super.onCreate();
        this.thematicRepository = new ThematicRepository(this);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Party.class, new PartyDeserializer(thematicRepository))
                .create();


        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setServer(getString(R.string.server))
                .build();

        apiService = restAdapter.create(APIService.class);
    }

    public APIService getApiService() {
        return apiService;
    }

    public ThematicRepository getThematicRepository() {
        return thematicRepository;
    }
}
