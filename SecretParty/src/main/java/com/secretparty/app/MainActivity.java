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

import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.secretparty.app.api.APIService;
import com.secretparty.app.db.ThematicRepository;
import com.secretparty.app.models.Party;
import com.secretparty.app.models.Thematic;
import com.secretparty.app.models.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity implements FragmentEvent.ThematicSelectedListener,
        FragmentEvent.PartyCreatedListener, FragmentEvent.UserCreationListener,
        FragmentEvent.PartySelectedListener, FragmentEvent.BuzzListener {

    public static final String PARTY_ID = "party_id";
//    private ArrayList<Thematic> thematics = new ArrayList<Thematic>();
    private Party mCurrentParty = null;
    private APIService api;
    ThematicRepository thematicRepo;
    private ArrayList<Party> mPartiesShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);

        thematicRepo =((SecretPartyApplication) getApplication()).getThematicRepository();
        api = ((SecretPartyApplication) getApplication()).getApiService();

        //TODO : check if thematics are in the database. If so, launch a ThematicFragment. Otherwise, download them, save them to the db and launch TheamticFragment

        int userId = prefs.getInt(getString(R.string.SP_user_id), -1);
        int partyId = prefs.getInt(getString(R.string.SP_party_id), -1);
        long partyEnd = prefs.getLong(getString(R.string.SP_date_party_end), -1);
        Log.v("creation", new Date(partyEnd).toLocaleString() + "");
        api.listThematics(new OnReceivedThematics());

        if(userId == -1) {
            DialogFragment df = new UserCreationDialog();
            df.setCancelable(false);
            df.show(getSupportFragmentManager(), "dialog");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_party, menu);
        return true;
    }


    private void openParty(Party p) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Log.v("creation", "PartyDate:" + p.getFinishDate().toLocaleString());
        prefs.edit()//.putInt(getString(R.string.SP_user_id), user.getId())
                .putInt(getString(R.string.SP_party_id),p.getId())
                .putLong(getString(R.string.SP_date_party_end),p.getFinishDate().getTime())
                .commit();
        this.mCurrentParty = p;

        PartyFragment newFragment = new PartyFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_add:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("Dialog");
                if (prev != null)
                    ft.remove(prev);
                ft.addToBackStack(null);
                DialogFragment frag = PartyCreationDialog.newInstance();
                frag.show(ft, "dialog");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBuzzPlayer(int buzzerId, int buzzeeId, int secretId) {
        //TODO:
    }

    @Override
    public Party getParty() {
        return mCurrentParty;
    }

    @Override
    public List<Party> getPartiesShown() {
        return this.mPartiesShown;
    }
    @Override
    public void onThematicSelected(int thematicId) {
        api.getParties(thematicId,new PartiesCallback(thematicId));
    }

    @Override
    public ThematicRepository getThematicRepository() {
        return thematicRepo;
    }

    @Override
    public void onPartyJoined(final int partyId, final int secretId) {
        int userId = getPreferences(MODE_PRIVATE).getInt(getString(R.string.SP_user_id),-1);
        int currentPartyId = getPreferences(MODE_PRIVATE).getInt(getString(R.string.SP_party_id),-1);
        // Send request join party
        if(userId==-1) {
            Toast.makeText(this,R.string.user_creation_required, Toast.LENGTH_LONG).show();
        } else {
            if(currentPartyId == partyId) {
                api.getParty(partyId, new OnReceivedParty());
            } else {
                api.joinParty(partyId,secretId, userId, new onPartyJoin());
            }
        }
    }

    @Override
    public void onPartyCreated(final int thematicId, final int secretId, final String partyName, final int duration) {
        int userId = getPreferences(MODE_PRIVATE).getInt(getString(R.string.SP_user_id),-1);
        // Send request create party
        if(userId==-1) {
            Toast.makeText(this,R.string.user_creation_required, Toast.LENGTH_LONG).show();
        } else {
            Log.d("PartyCreated", "t"+thematicId + "s:" + secretId + "u:" + userId);
            api.createParty(partyName,duration,thematicId,secretId,userId,new OnCreatedParty());
        }
    }

    @Override
    public void onUserCreated(String name) {
        api.createUser(name, new OnReceivedUser());
    }



    private class OnReceivedThematics implements Callback<ArrayList<Thematic>> {
        @Override
        public void success(ArrayList<Thematic> thematics, Response response) {
            Log.i("API", "OK request getThematics");
            //MainActivity.this.thematics =
            thematicRepo.createAll(thematics);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ThematicFragment())
                    .commit();
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.i("API", "FAIL request getThematics : " + retrofitError.getMessage() +" | " + retrofitError.getResponse().getReason());
            Toast.makeText(MainActivity.this,R.string.thematic_loading_fail, Toast.LENGTH_LONG).show();
        }
    }
    //TODO : regroup onReceivedParty, onPartyJoin, onPartyCreated
    private class OnReceivedParty implements Callback<Party> {
        @Override
        public void success(Party party, Response response) {
            Log.i("API", "OK request getParty");
            MainActivity.this.openParty(party);
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.i("API", "FAIL request getParty : " + retrofitError.getMessage());
            Toast.makeText(MainActivity.this,R.string.PartyLoadingFailed, Toast.LENGTH_LONG).show();
        }
    }

    private class onPartyJoin implements Callback<Party> {
        @Override
        public void success(Party party, Response response) {
            Log.i("API", "OK request openParty");
            MainActivity.this.openParty(party);
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.i("API", "FAIL request openParty : " + retrofitError.getMessage());
            Toast.makeText(MainActivity.this,R.string.PartyJoiningFail, Toast.LENGTH_LONG).show();
        }
    }

    private class OnCreatedParty implements Callback<Party> {
        @Override
        public void success(Party party, Response response) {
            Log.i("API", "OK request createdParty");
           MainActivity.this.openParty(party);
           if(mPartiesShown != null && mPartiesShown.get(0).getThematic().getId() == party.getThematic().getId())
               MainActivity.this.mPartiesShown.add(party);
            //TODO add party to the current parties of the thematic.
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.e("API", ((JsonObject) retrofitError.getBodyAs(JsonObject.class)).toString());

            Log.i("API", "FAIL request createdParty : " + retrofitError.getMessage());
            Toast.makeText(MainActivity.this,R.string.PartyCreationFail, Toast.LENGTH_LONG).show();
        }
    }

    private class OnReceivedUser implements Callback<User> {
        @Override
        public void success(User user, Response response) {
            getPreferences(MODE_PRIVATE).edit().putInt(getString(R.string.SP_user_id),user.getId()).commit();
            Toast.makeText(MainActivity.this, R.string.user_created,Toast.LENGTH_LONG).show();
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.e("API", "Fail request user creation : " + retrofitError.getMessage());
            Toast.makeText(MainActivity.this, R.string.user_creation_failed,Toast.LENGTH_LONG).show();

        }
    }

    private class PartiesCallback implements Callback<ArrayList<Party>> {
        private final Thematic thematic;

        public PartiesCallback(int thematicId) {
            this.thematic= thematicRepo.get(thematicId);
        }
        @Override
        public void success(ArrayList<Party> parties, Response response) {
            MainActivity.this.mPartiesShown = parties;
            ThematicDetailedFragment newFragment = new ThematicDetailedFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.e("API", "Fail request user creation : " + retrofitError.getMessage());
            Toast.makeText(MainActivity.this, R.string.parties_listing_failes,Toast.LENGTH_LONG).show();

        }
    }
}
