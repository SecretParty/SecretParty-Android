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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.secretparty.app.models.Secret;
import com.secretparty.app.models.Thematic;
import com.secretparty.app.models.User;

import java.util.List;

/**
 * Created by MagicMicky on 05/02/14.
 */
public class BuzzDialog extends DialogFragment {
    private static final String TAG = "BuzzDialog";
    public static final String BUZZEE_ID = "buzzee_id";
    private FragmentEvent.BuzzListener mCallback;
    private int mSelectedSecretId;

    static BuzzDialog newInstance(Thematic t, User buzzee) {
        BuzzDialog f = new BuzzDialog();
        Bundle args = new Bundle();
        args.putInt(ThematicDetailedFragment.THEMATIC_ID, t.getId());
        args.putInt(BUZZEE_ID, buzzee.getId());
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (FragmentEvent.BuzzListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ThematicManager");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.buzz_player, null);
        SharedPreferences prefs = getActivity().getPreferences(Activity.MODE_PRIVATE);
        Bundle b = getArguments();
        final int buzzeeId = b.getInt(BUZZEE_ID, -1);
        final int buzzerId = prefs.getInt(getString(R.string.SP_user_id),-1);
        int thematicId = b.getInt(ThematicDetailedFragment.THEMATIC_ID,-1);
        if(thematicId == -1 || buzzeeId == -1 ||buzzerId == -1) {
            Log.e(TAG, "thematic/buzzer/buzzee id = -1");
        }
        final List<Secret> secrets = mCallback.getThematicRepository().get(thematicId).getSecrets();

        Spinner s = (Spinner) rootView.findViewById(R.id.S_secrets);
        ArrayAdapter<Secret> adapter = new ArrayAdapter<Secret>(getActivity(), android.R.layout.simple_spinner_item, secrets); 
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BuzzDialog.this.mSelectedSecretId = secrets.get(i).getId();
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView)
                .setTitle(R.string.party_join_dialog)
                .setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                        mCallback.onBuzzPlayer(buzzerId,buzzeeId,mSelectedSecretId);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), R.string.party_join_cancel, Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                });
        return builder.create();



    }
}
