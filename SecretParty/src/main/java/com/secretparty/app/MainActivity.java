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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.secretparty.app.models.Party;
import com.secretparty.app.models.Thematic;
import com.secretparty.app.models.User;
import com.secretparty.app.utils.PartyAdapter;
import com.secretparty.app.utils.ThematicAdpater;
import com.secretparty.app.utils.UserAdpater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements ThematicFragment.ThematicManager {

    private ArrayList<Thematic> thematics = new ArrayList<Thematic>();
    private Party mCurrentParty=null;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init volley queue
        SecretPartyApplication app = (SecretPartyApplication) getApplication();
        mRequestQueue = app.getmVolleyRequestQueue();

        if (savedInstanceState == null) {
            SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
            int userId = prefs.getInt(getString(R.string.SP_user_id), -1);
            int partyId = prefs.getInt(getString(R.string.SP_party_id), -1);
            long partyEnd = prefs.getLong(getString(R.string.SP_date_party_end), -1);

            if(new Date().compareTo(new Date(partyEnd)) < 0) {
                // Send request Volley getThematics
                JsonObjectRequest request = new JsonObjectRequest(getString(R.string.server)+"party/"+partyId,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    Log.d("Call API", "GET party/id");
                                    mCurrentParty = PartyAdapter.parseParty(jsonObject);
                                    getSupportFragmentManager().beginTransaction()
                                            .add(R.id.container, new ThematicFragment())
                                            .commit();
                                } catch (JSONException e) {
                                    Log.d("Call API", "ERROR GET party/id : "+e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Call API", "ERROR GET party/id : "+volleyError.getMessage());
                    }
                });
                request.setTag(this);

                // Add request in queue
                mRequestQueue.add(request);
            }
            else {
                // Send request Volley getThematics
                JsonArrayRequest request = new JsonArrayRequest(getString(R.string.server)+"thematics",
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray jsonArray) {
                                Log.d("Call API", "GET thematics");
                                try {
                                    thematics = ThematicAdpater.getThematics(jsonArray);
                                    getSupportFragmentManager().beginTransaction()
                                            .add(R.id.container, new ThematicFragment())
                                            .commit();
                                } catch (JSONException e) {
                                    Log.d("Call API", "ERROR GET thematics : "+e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Call API", "ERROR GET thematics : "+volleyError.getMessage());
                    }
                });
                request.setTag(this);

                // Add request in queue
                mRequestQueue.add(request);
            }
        }

    }

    @Override
    protected void onStop(){
        mRequestQueue.cancelAll(this);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_party, menu);
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
            case R.id.action_add:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("Dialog");
                if(prev != null)
                    ft.remove(prev);
                ft.addToBackStack(null);
                DialogFragment frag = PartyCreationDialog.newInstance();
                frag.show(ft, "dialog");
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
    public void onPartyJoined(final int thematicPos,final int partyPos, final String username,final int secretId) {
        Log.d("OMG", "party joined");
        final Party party = getThematics().get(thematicPos).getCurrentParties().get(partyPos);

        // Send request Volley join party
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,getString(R.string.server)+"user",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            Log.d("Call API", "POST user");
                            User user = UserAdpater.parseUser(jsonObject);
                            party.getUsers().add(user);
                            SharedPreferences prefs = getPreferences(MODE_PRIVATE);

                            Date end = new Date(party.getDate().getTime() + party.getLength());

                            //Save the info about the user and his in the SharedPreferences.
                            prefs.edit()
                                    .putInt(getString(R.string.SP_user_id), user.getId())
                                    .putLong(getString(R.string.SP_date_party_end),end.getTime() )
                                    .putInt(getString(R.string.SP_party_id), party.getId())
                                    .commit();
                            MainActivity.this.mCurrentParty = party;
                            loadPartyFragment();
                        } catch (JSONException e) {
                            Log.d("Call API", "ERROR POST user : "+e.getMessage());
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Call API", "ERROR POST user : "+volleyError.getMessage());
                    }
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("user[name]",username);
                params.put("user[secret]",Integer.toString(secretId));
                params.put("user[party]",Integer.toString(party.getId()));

                return params;
             }
        };
        request.setTag(this);

        // Add request in queue
        mRequestQueue.add(request);

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

    @Override
    public void onPartyCreated(final int thematicId, final int secretId, final String partyName, final int duration, final String username) {
        // Send request Volley join party
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,getString(R.string.server)+"party",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("Call API", "POST party");

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Call API", "ERROR POST party : "+volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put("party_user[party][name]",partyName);
                params.put("party_user[party][length]",Integer.toString(duration));
                params.put("party_user[party][thematic]",Integer.toString(thematicId));
                params.put("party_user[user][name]",username);
                params.put("party_user[user][secret]",Integer.toString(secretId));

                return params;
            }
        };
        request.setTag(this);

        // Add request in queue
        mRequestQueue.add(request);
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

}
