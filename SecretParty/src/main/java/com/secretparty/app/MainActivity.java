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

import com.secretparty.app.api.APIService;
import com.secretparty.app.models.Party;
import com.secretparty.app.models.Thematic;
import com.secretparty.app.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity implements FragmentEvent.ThematicSelectedListener,
        FragmentEvent.PartyCreatedListener,
        FragmentEvent.PartySelectedListener, FragmentEvent.BuzzListener {

    private ArrayList<Thematic> thematics = new ArrayList<Thematic>();
    private Party mCurrentParty = null;
    private APIService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = ((SecretPartyApplication) getApplication()).getApiService();

        if (savedInstanceState == null) {
            SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
           // int userId = prefs.getInt(getString(R.string.SP_user_id), -1);
            int partyId = prefs.getInt(getString(R.string.SP_party_id), -1);
            long partyEnd = prefs.getLong(getString(R.string.SP_date_party_end), -1);

            if (new Date().compareTo(new Date(partyEnd)) < 0) {
                // Send request getParty
                api.getParty(partyId, new OnReceivedParty());
            } else {
                // Send request getThematics
                api.listThematics(new OnReceivedThematics());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_party, menu);
        return true;
    }


    private void joinParty(Party p) {
        SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
        prefs.edit()//.putInt(getString(R.string.SP_user_id), user.getId())
                .putInt(getString(R.string.SP_user_id),p.getId())
                .putLong(getString(R.string.SP_date_party_end),p.getDate() + p.getLength()*100)//calcul of the party end timestamp.
                .commit();
        this.mCurrentParty = p;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
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
    public void onBuzzPlayer(User buzer, User buzee) {
        //TODO:
    }

    @Override
    public Party getParty() {
        return mCurrentParty;
    }


    @Override
    public void onThematicSelected(int pos) {
        ThematicDetailedFragment newFragment = new ThematicDetailedFragment();
        Bundle args = new Bundle();
        args.putInt(ThematicDetailedFragment.THEMATIC_POS, pos);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public List<Thematic> getThematics() {
        return thematics;
    }

    @Override
    public void onPartyJoined(final int thematicPos, final int partyPos, final String username, final int secretId) {
        // Send request for join party
        api.joinParty(username, secretId, getThematics().get(thematicPos).getParties().get(partyPos).getId(), new onPartyJoin());
    }

    @Override
    public void onPartyCreated(final int thematicId, final int secretId, final String partyName, final int duration, final String username) {
        // Send request create party
        api.createParty(partyName,duration,thematicId,username,secretId,new OnCreatedParty());
    }

    private void loadPartyFragment() {
        loadPartyFragment(-1);
    }

    private void loadPartyFragment(int pos) {
        PartyFragment newFragment = new PartyFragment();
        if (pos != -1) {
            Bundle args = new Bundle();
            args.putInt(ThematicDetailedFragment.THEMATIC_POS, pos);
            newFragment.setArguments(args);

        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private class OnReceivedThematics implements Callback<ArrayList<Thematic>> {

        @Override
        public void success(ArrayList<Thematic> thematics, Response response) {
            Log.i("API", "OK request getThematics");
            MainActivity.this.thematics = thematics;
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

    private class OnReceivedParty implements Callback<Party> {
        @Override
        public void success(Party party, Response response) {
            Log.i("API", "OK request getParty");
            MainActivity.this.mCurrentParty = party;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ThematicFragment())
                    .commit();
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.i("API", "FAIL request getParty : " + retrofitError.getMessage());
            Toast.makeText(MainActivity.this,R.string.PartyLoadingFailed, Toast.LENGTH_LONG).show();
        }
    }

    private class onPartyJoin implements Callback<User> {
        @Override
        public void success(User user, Response response) {
            Log.i("API", "OK request joinParty");
            MainActivity.this.joinParty(user.getParty());
            //TODO action post join party
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.i("API", "FAIL request joinParty : " + retrofitError.getMessage());
            Toast.makeText(MainActivity.this,R.string.PartyJoiningFail, Toast.LENGTH_LONG).show();
        }
    }

    private class OnCreatedParty implements Callback<Party> {
        @Override
        public void success(Party party, Response response) {
            Log.i("API", "OK request createdParty");
           // MainActivity.this.joinParty(party);
            //TODO action post created party
            if(MainActivity.this.getThematics().contains(party.getThematic())) {
                Log.d("API", "doexists");
            }
            else {
                Log.d("API", "do not exists");
            }
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.i("API", "FAIL request createdParty : " + retrofitError.getMessage());
            Toast.makeText(MainActivity.this,R.string.PartyCreationFail, Toast.LENGTH_LONG).show();
        }
    }
}
