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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.secretparty.app.models.Party;
import com.secretparty.app.models.Secret;
import com.secretparty.app.models.User;

import java.util.List;

/**
 * Created by MagicMicky on 23/01/14.
 */
public class PartyDetailsDialog extends DialogFragment {
    private ThematicFragment.ThematicManager mCallback;
    private int secretChosen = 0;

    static PartyDetailsDialog newInstance(int thematicPos, int partyPos) {
        PartyDetailsDialog f = new PartyDetailsDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(PartyFragment.PARTY_POS, partyPos);
        args.putInt(ThematicDetailedFragment.THEMATIC_POS, thematicPos);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (ThematicFragment.ThematicManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int thematicPos = getArguments().getInt(ThematicDetailedFragment.THEMATIC_POS);
        final int partyPos = getArguments().getInt(PartyFragment.PARTY_POS);
        Party p = mCallback.getThematics().get(thematicPos).getCurrentParties().get(partyPos);
        final List<Secret> secrets = mCallback.getThematics().get(thematicPos).getSecrets();

        View rootView = getDialog().getLayoutInflater().inflate(R.layout.join_party_layout, null);
        Spinner secret = (Spinner) rootView.findViewById(R.id.S_secrets);
        ArrayAdapter<Secret> adapter = new ArrayAdapter<Secret>(getActivity(), android.R.layout.simple_spinner_item, secrets);
        secret.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.v("PartyDialog", "Item click on " + position);
                secretChosen = position;
            }
        });
        secret.setAdapter(adapter);



        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView)
                .setTitle(R.string.party_join_dialog)
                .setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String username = String.valueOf(((EditText) getDialog().findViewById(R.id.ET_username)).getText());
                        dismiss();
                        mCallback.onPartyJoined(thematicPos, partyPos, username, secrets.get(secretChosen).getId());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO
                    }
                });
        return builder.create();
    }

    private class UserAdapter extends BaseAdapter{
        private final Context mContext;
        private final List<User> mUsers;

        public UserAdapter(Context context, List<User> users) {
            this.mContext = context;
            this.mUsers = users;
        }

        @Override
        public int getCount() {
            return mUsers.size();
        }

        @Override
        public User getItem(int i) {
            return mUsers.get(i);
        }

        @Override
        public long getItemId(int i) {
            return mUsers.get(i).getId();
        }
        @Override
        public boolean isEnabled(int pos) {
            return false;
        }
        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            if(convertView ==null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.single_user,parent,false);
            }
            TextView username = (TextView) convertView.findViewById(R.id.TV_user_name);
            User u = getItem(i);
            username.setText(u.getName());


            return convertView;
        }
    }
}
