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
import android.widget.Toast;

import com.secretparty.app.models.Party;
import com.secretparty.app.models.Secret;
import com.secretparty.app.models.User;

import java.util.List;

/**
 * Created by MagicMicky on 23/01/14.
 */
public class PartyDetailsDialog extends DialogFragment {
    private static final String TAG = "PartyDetailsDialog";
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
                    + " must implement ThematicManager");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int thematicPos = getArguments().getInt(ThematicDetailedFragment.THEMATIC_POS,-1);
        final int partyPos = getArguments().getInt(PartyFragment.PARTY_POS, -1);
        if(thematicPos==-1 || partyPos==-1) {
            Log.e(TAG, "Error while giving the thematic or Party position");
        }

        final List<Secret> secrets = mCallback.getThematics().get(thematicPos).getSecrets();

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.join_party_layout, null);
        Spinner secret = (Spinner) rootView.findViewById(R.id.S_secrets);
        ArrayAdapter<Secret> adapter = new ArrayAdapter<Secret>(getActivity(), android.R.layout.simple_spinner_item, secrets);

        secret.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Log.v("PartyDialog", "Item click on " + position);
                secretChosen = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                secretChosen=-1;
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
                        Toast.makeText(getActivity(),R.string.party_join_cancel,Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                });
        return builder.create();
    }
}
