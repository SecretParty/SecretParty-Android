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

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.secretparty.app.models.Party;
import com.secretparty.app.models.Secret;
import com.secretparty.app.models.Thematic;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements ThematicFragment.ThematicManager {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ThematicFragment())
                    .commit();
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
        args.putInt(ThematicDetailedFragment.ARG_POSITION, pos);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public List<Thematic> getThematics() {
        ArrayList<Party> parties = new ArrayList<Party>();
        parties.add(new Party(1,"partyxxx",null,20,null,null));
        Thematic t = new Thematic(1,"OMG",parties, new ArrayList<Secret>());
        parties.get(0).setThematic(t);
        ArrayList<Thematic> thematics = new ArrayList<Thematic>();
        thematics.add(t);

        return thematics;
    }

}
