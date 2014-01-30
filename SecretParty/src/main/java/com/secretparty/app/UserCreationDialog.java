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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.secretparty.app.models.Secret;
import com.secretparty.app.models.Thematic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MagicMicky on 31/01/14.
 */
public class UserCreationDialog extends DialogFragment {

    private FragmentEvent.UserCreationListener mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (FragmentEvent.UserCreationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ThematicManager");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View rootView = getActivity().getLayoutInflater().inflate(R.layout.user_creation_dialog, null);
        final EditText usernameEditText = (EditText) rootView.findViewById(R.id.ET_username);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(rootView)
                .setTitle(R.string.party_join_dialog)
                .setPositiveButton(R.string.join, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String username = String.valueOf(usernameEditText.getText());
                        mCallback.onUserCreated(username);
                    }
                })
                .setCancelable(false);
        return builder.create();
    }

}
