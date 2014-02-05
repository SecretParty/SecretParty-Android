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
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.secretparty.app.models.Party;
import java.util.List;

/**
 * Created by MagicMicky on 23/01/14.
 */
public class ThematicDetailedFragment extends Fragment {
    public static final String THEMATIC_ID = "THEMATIC_ID";
    private FragmentEvent.PartySelectedListener mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (FragmentEvent.PartySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.listview_fragment, container, false);
        ListView party_list = (ListView) rootView.findViewById(R.id.list);

        ((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final PartyAdapter adapter = new PartyAdapter(this.getActivity(), mCallback.getPartiesShown());
        party_list.setAdapter(adapter);
        party_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                SharedPreferences prefs = getActivity().getPreferences(Activity.MODE_PRIVATE);
                int partyId = prefs.getInt(getString(R.string.SP_party_id),-1);
                //If the party is the user's current party
                if(partyId == id) {
                    mCallback.onPartyJoined((int)id,0);
                }
                else {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Fragment prev = getFragmentManager().findFragmentByTag("Dialog");
                    if(prev != null)
                        ft.remove(prev);
                    ft.addToBackStack(null);
                    DialogFragment frag = PartyDetailsDialog.newInstance(adapter.getItem(i).getThematic().getId(), (int)id);

                    frag.show(ft, "dialog");
                }
            }
        });


        return rootView;
    }



    private static class PartyAdapter extends BaseAdapter {

        private final Context mContext;
        private final List<Party> mParties;

        public PartyAdapter(Context context, List<Party> parties) {
            this.mParties = parties;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mParties.size();
        }

        @Override
        public Party getItem(int i) {
            return mParties.get(i);
        }

        @Override
        public long getItemId(int i) {
            return this.getItem(i).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.single_party, parent, false);
            }
            TextView partyName = (TextView) convertView.findViewById(R.id.TV_party_name);
            ImageView partyPlayingImg = (ImageView) convertView.findViewById(R.id.IMG_playing);
            Party p = getItem(position);
            partyPlayingImg.setImageDrawable(mContext.getResources().getDrawable(getDrawableIdForParty(p.isPlaying())));
            partyName.setText(p.getName());
            return convertView;
        }

        public int getDrawableIdForParty(boolean playing) {
            if(playing)
                return android.R.drawable.ic_media_play;
            return android.R.drawable.ic_media_pause;
        }
    }
}
