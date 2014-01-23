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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.secretparty.app.models.Thematic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MagicMicky on 23/01/14.
 */
public class ThematicAdapter extends BaseAdapter {
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
    public Thematic getItem(int i) {
        return mThematics.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView initial, thematicText, thematicNb;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.single_thematic, parent, false);
        initial = (TextView) convertView.findViewById(R.id.thematic_initial);
        thematicText = (TextView) convertView.findViewById(R.id.thematic_name);
        thematicNb = (TextView) convertView.findViewById(R.id.thematic_party_number);
        Thematic t = this.getItem(position);
        initial.setText(""+t.getName().charAt(0));
        thematicText.setText(t.getName());
        thematicNb.setText(""+t.getCurrentParties().size());

        return convertView;
    }
}
