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
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.secretparty.app.models.Secret;

import java.util.List;

/**
 * Created by MagicMicky on 23/01/14.
 */
public class PartyDetailsDialog extends DialogFragment {
    private static final String TAG = "PartyDetailsDialog";
    private FragmentEvent.PartySelectedListener mCallback;
    private int secretChosen = 0;

    static PartyDetailsDialog newInstance(int thematicPos, int partyPos) {
        PartyDetailsDialog f = new PartyDetailsDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt(PartyFragment.PARTY_POS, partyPos);
        args.putInt(ThematicDetailedFragment.THEMATIC_ID, thematicPos);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (FragmentEvent.PartySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ThematicManager");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int thematicId = getArguments().getInt(ThematicDetailedFragment.THEMATIC_ID,-1);
        final int partyId = getArguments().getInt(MainActivity.PARTY_ID, -1);
        if(thematicId==-1 || partyId==-1) {
            Log.e(TAG, "Error while giving the thematic or Party position");
        }
        //TODO : stop using freaking positions
        final List<Secret> secrets = mCallback.getThematicRepository().get(thematicId).getSecrets();

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
                        dismiss();
                        mCallback.onPartyJoined(partyId, secrets.get(secretChosen).getId());
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
