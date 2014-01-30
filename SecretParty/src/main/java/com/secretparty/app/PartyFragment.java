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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.secretparty.app.models.Party;
import com.secretparty.app.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by MagicMicky on 24/01/14.
 */
public class PartyFragment extends Fragment {
    public static final String PARTY_POS = "PARTY_POS";

    private FragmentEvent.BuzzListener mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (FragmentEvent.BuzzListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.party_layout, container, false);
        TextView partyName, partyTimeLeft, partyThematic;
        View divider;
        ListView thematic_list;
        Party currentParty = mCallback.getParty();

        partyName = (TextView) rootView.findViewById(R.id.TV_party_name);
        partyTimeLeft = (TextView) rootView.findViewById(R.id.TV_time_left);
        partyThematic = (TextView) rootView.findViewById(R.id.TV_party_thema);
        divider = rootView.findViewById(R.id.V_divider);
        thematic_list = (ListView) rootView.findViewById(R.id.list);

        partyName.setText(currentParty.getName());
        partyThematic.setText(currentParty.getThematic().getName());
        partyTimeLeft.setText(getRemainingTimeString(new Date(currentParty.getDate())));

        divider.setBackgroundColor(getResources().getIntArray(R.array.pic_color)[currentParty.getThematic().getColor()]);

        ListAdapter mAdapter = new UserAdapter(this.getActivity(), currentParty.getUsers());
        thematic_list.setAdapter(mAdapter);
        thematic_list.setOnItemClickListener(null);
        return rootView;
    }

    private String getRemainingTimeString(Date end) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date());
        Calendar c2 = Calendar.getInstance();
        c2.setTime(end);
        long mil1 = c1.getTimeInMillis();
        long mil2 = c2.getTimeInMillis();
        long diff = mil1 - mil2;
        long sec = diff / 1000;
        long min = diff / (60*1000);
        return getString(R.string.remainingTime, sec, min);
    }



    private class UserAdapter extends BaseAdapter {
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
                convertView = inflater.inflate(R.layout.single_user_with_buzz,parent,false);
            }
            TextView username = (TextView) convertView.findViewById(R.id.TV_user_name);
            User u = getItem(i);
            username.setText(u.getName());


            return convertView;
        }
    }
}
