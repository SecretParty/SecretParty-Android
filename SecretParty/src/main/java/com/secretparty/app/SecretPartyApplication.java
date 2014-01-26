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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by david on 26/01/2014.
 */
public class SecretPartyApplication extends Application {

    private RequestQueue mVolleyRequestQueue;

    @Override
    public void onCreate(){
        super.onCreate();

        mVolleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mVolleyRequestQueue.start();

    }

    public RequestQueue getmVolleyRequestQueue() {
        return mVolleyRequestQueue;
    }

    @Override
    public void onTerminate(){
        mVolleyRequestQueue.stop();
        super.onTerminate();
    }
}
