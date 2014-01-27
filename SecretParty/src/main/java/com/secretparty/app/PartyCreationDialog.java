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
import android.widget.EditText;
import android.widget.Spinner;

import com.secretparty.app.models.Party;
import com.secretparty.app.models.Secret;
import com.secretparty.app.models.Thematic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MagicMicky on 24/01/14.
 */
public class PartyCreationDialog extends DialogFragment {
    private ThematicFragment.ThematicManager mCallback;
    private int secretChosen = 0;
    private int thematicChosen = 0;

    static PartyCreationDialog newInstance() {
        PartyCreationDialog f = new PartyCreationDialog();

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
       // final List<Secret> secrets = mCallback.getThematics().get(thematicPos).getSecrets();
        final List<Thematic> thematics = mCallback.getThematics();
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.party_creation_layout, null);
        final Spinner secret = (Spinner) rootView.findViewById(R.id.S_secrets);
        Spinner thematic = (Spinner) rootView.findViewById(R.id.S_theme);
        ArrayAdapter<Thematic> thematicArrayAdapter = new ArrayAdapter<Thematic>(getActivity(),android.R.layout.simple_spinner_dropdown_item, thematics);
       // ArrayAdapter<Secret> adapter = new ArrayAdapter<Secret>(getActivity(), android.R.layout.simple_spinner_item, secrets);
        thematic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                List<Secret> secrets = thematics.get(position).getSecrets();
                ArrayAdapter<Secret> adapter = new ArrayAdapter<Secret>(getActivity(), android.R.layout.simple_spinner_dropdown_item, secrets);
                thematicChosen = position;
                secret.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                List<Secret> secrets = new ArrayList<Secret>();
                ArrayAdapter<Secret> adapter=new ArrayAdapter<Secret>(getActivity(),android.R.layout.simple_spinner_dropdown_item,secrets);
                thematicChosen = -1;
                secret.setAdapter(adapter);
            }
        });
        thematic.setAdapter(thematicArrayAdapter);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView)
                .setTitle(R.string.party_join_dialog)
                .setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String username = String.valueOf(((EditText) getDialog().findViewById(R.id.ET_username)).getText());
                        String partyName = String.valueOf(((EditText)getDialog().findViewById(R.id.ET_party_name)).getText());
                        int duration = Integer.parseInt(String.valueOf(((EditText) getDialog().findViewById(R.id.ET_party_duration)).getText()));
                        Thematic t = mCallback.getThematics().get(thematicChosen);
                        Secret s = t.getSecrets().get(secretChosen);
                        dismiss();
                        mCallback.onPartyCreated(t.getId(),s.getId(),partyName,duration);
                       // mCallback.onPartyJoined(thematicPos, partyPos, username, secrets.get(secretChosen).getId());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO
                    }
                });
        return builder.create();
    }
}
