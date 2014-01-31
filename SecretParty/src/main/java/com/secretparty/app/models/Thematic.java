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

package com.secretparty.app.models;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by MagicMicky on 22/01/14.
 */
@DatabaseTable()
public class Thematic {
    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private String name;
    private List<Party> parties = new ArrayList<Party>();
    @ForeignCollectionField(eager = true)
    private Collection<Secret> secrets= new ArrayList<Secret>();

    public Thematic(int id, String name, List<Party> parties, List<Secret> secrets) {
        this.setId(id);
        this.setName(name);
        this.setParties(parties);
        this.setSecrets(secrets);
    }
    public Thematic(int id) {
        this(id,null,null,null);
    }
    public Thematic() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Party> getParties() {
        return parties;
    }

    public void setParties(List<Party> parties) {
        this.parties = parties;
    }

    public List<Secret> getSecrets() {
        return new ArrayList<Secret>(secrets);
    }

    public void setSecrets(List<Secret> secrets) {
        this.secrets = secrets;
    }

    public int getColor() {
        char c1, c2;
        c1 = getName().charAt(0);
        c2 = getName().charAt(1);
        return (c1 + c2<<1) * getId() % 5;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public void addParty(Party party) {
        this.getParties().add(party);
    }
}
