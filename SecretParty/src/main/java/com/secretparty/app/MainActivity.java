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
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.secretparty.app.models.Party;
import com.secretparty.app.models.Thematic;
import com.secretparty.app.models.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends ActionBarActivity implements ThematicFragment.ThematicManager {

    private ArrayList<Thematic> thematics = new ArrayList<Thematic>();
    private Party mCurrentParty=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
            int userId = prefs.getInt(getString(R.string.SP_user_id), -1);
            int partyId = prefs.getInt(getString(R.string.SP_party_id), -1);
            long partyEnd = prefs.getLong(getString(R.string.SP_date_party_end), -1);
            if(new Date().compareTo(new Date(partyEnd)) < 0) {
                GetPartyAsync getPartyAsync = new GetPartyAsync();
                getPartyAsync.execute(partyId);
            } else {
                ThematicsAsync async = new ThematicsAsync();
                async.execute();
            }
            
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void onPartyJoined(int thematicPos, int partyPos, String username, int secretId) {
        Log.d("OMG", "party joined");

        UserJoinPartyASync join = new UserJoinPartyASync(getThematics().get(thematicPos).getCurrentParties().get(partyPos), username,secretId );
        join.execute();
       /* //Change Fragment
        PartyFragment newFragment = new PartyFragment();
        Bundle args = new Bundle();
        args.putInt(ThematicDetailedFragment.THEMATIC_POS, pos);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();*/



    }

    @Override
    public Party getParty() {
        return mCurrentParty;
    }

    private void loadPartyFragment() {
        loadPartyFragment(-1);
    }
    private void loadPartyFragment(int pos) {
        PartyFragment newFragment = new PartyFragment();
        if(pos!=-1) {
            Bundle args = new Bundle();
            args.putInt(ThematicDetailedFragment.THEMATIC_POS, pos);
            newFragment.setArguments(args);

        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private class UserJoinPartyASync extends AsyncTask<Void, Void, User> {
        private final Party mParty;
        private final String mUserName;
        private int mSecretId;

        public UserJoinPartyASync(Party party, String userName, int secretId) {
            this.mParty = party;
            this.mUserName =userName;
            this.mSecretId = secretId;
        }
        @Override
        protected User doInBackground(Void... voids) {
            APISecretParty.joinParty(mParty.getId(),mSecretId,mUserName,getString(R.string.server));
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);

            Date end = new Date(mParty.getDate().getTime() + mParty.getLength());

            //Save the info about the user and his in the SharedPreferences.
            prefs.edit()
                    .putInt(getString(R.string.SP_user_id), user.getId())
                    .putLong(getString(R.string.SP_date_party_end),end.getTime() )
                    .putInt(getString(R.string.SP_party_id), mParty.getId())
            .commit();
            MainActivity.this.mCurrentParty = mParty;
            loadPartyFragment();
        }
    }

    private class ThematicsAsync extends AsyncTask<Void,Void,ArrayList<Thematic>> {

        @Override
        protected ArrayList<Thematic> doInBackground(Void... voids) {
            return APISecretParty.getThematics(getString(R.string.server));
        }

        @Override
        protected void onPostExecute(ArrayList<Thematic> t){
            thematics = t;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ThematicFragment())
                    .commit();

        }
    }
    
    private class GetPartyAsync extends AsyncTask<Integer, Void, Party> {

        @Override
        protected Party doInBackground(Integer... integers) {
            int partyId = integers[0];
            //TODO : get party
            return null;
        }

        @Override
        protected void onPostExecute(Party party) {
            MainActivity.this.mCurrentParty = party;
            loadPartyFragment();
        }
    }

}
