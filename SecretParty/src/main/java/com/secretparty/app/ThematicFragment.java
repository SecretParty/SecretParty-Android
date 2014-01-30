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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.secretparty.app.models.Party;
import com.secretparty.app.models.Thematic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MagicMicky on 23/01/14.
 */
public class ThematicFragment extends Fragment {
    private FragmentEvent.ThematicSelectedListener mCallback;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (FragmentEvent.ThematicSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listview_fragment, container, false);
        ListView thematic_list = (ListView) rootView.findViewById(R.id.list);
        ListAdapter mAdapter = new ThematicAdapter(this.getActivity(), mCallback.getThematics());
        thematic_list.setAdapter(mAdapter);
        thematic_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                mCallback.onThematicSelected(pos);
            }
        });
        return rootView;
    }


        private static class ThematicAdapter extends BaseAdapter {
        private final List<Thematic> mThematics;
        private final Context mContext;

        public ThematicAdapter(Context context) {
            this(context, new ArrayList<Thematic>());
        }
        public ThematicAdapter(Context context, List<Thematic> data) {
            this.mThematics = data;
            this.mContext = context;
        }
        @Override
        public int getCount() {
            return mThematics.size();
        }

        @Override
        public Thematic getItem(int pos) {
            return mThematics.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return getItem(pos).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView initial, thematicText, thematicNb;
            if(convertView==null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.single_thematic, parent, false);
            }
            initial = (TextView) convertView.findViewById(R.id.thematic_initial);
            thematicText = (TextView) convertView.findViewById(R.id.thematic_name);
            thematicNb = (TextView) convertView.findViewById(R.id.thematic_party_number);
            Thematic t = this.getItem(position);
            convertView.setId(t.getId());
            initial.setText(""+t.getName().charAt(0));
            initial.setBackgroundColor(mContext.getResources().getIntArray(R.array.pic_color)[t.getColor()]);
            thematicText.setText(t.getName());
            thematicNb.setText(""+t.getParties().size());
            return convertView;
        }
    }

}
