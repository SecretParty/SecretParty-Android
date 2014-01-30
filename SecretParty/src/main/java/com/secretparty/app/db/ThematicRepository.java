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

package com.secretparty.app.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.secretparty.app.models.Secret;
import com.secretparty.app.models.Thematic;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by MagicMicky on 30/01/14.
 */
public class ThematicRepository {
    private DatabaseHelper db;
    Dao<Thematic, Integer> thematicDao;
    Dao<Secret,Integer> secretDao;
    public ThematicRepository(Context ctx)
    {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            db = dbManager.getDbHelper(ctx);
            thematicDao = db.getThematicDao();
            secretDao = db.getSecretDao();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }

    }

    public int create(Thematic thematic)
    {
        try {
            return thematicDao.create(thematic);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    public int update(Thematic thematic)
    {
        try {
            return thematicDao.update(thematic);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    public int delete(Thematic thematic)
    {
        try {
            return thematicDao.delete(thematic);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public List<Thematic> getAll()
    {
        try {
            return thematicDao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

    public void createAll(List<Thematic> thematics) {
        try {
            for(Thematic t : thematics) {
                for(Secret s : t.getSecrets()) {
                    s.setThematic(t);
                    secretDao.createOrUpdate(s);
                }
                thematicDao.createOrUpdate(t);
            }
        } catch(SQLException e) {
            //TODO: Exception Handling
            e.printStackTrace();
        }
    }

    public Thematic get(int id) {
        try {
            return thematicDao.queryForId(id);
        } catch (SQLException e) {
            //TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }

}

