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

import com.secretparty.app.models.Party;
import com.secretparty.app.models.Thematic;
import com.secretparty.app.models.User;

import java.util.List;

/**
 * Created by MagicMicky on 30/01/14.
 */
public interface FragmentEvent {

    List<Thematic> getThematics();

    public interface ThematicSelectedListener extends FragmentEvent{
        /**
         * When a thematic is selected
         * @param pos the position of the Thematic selected. TODO : no more pos
         */
        public void onThematicSelected(int pos);
    }

    public interface PartySelectedListener extends FragmentEvent {
        /**
         * When a party is joined
         * @param thematicPos The position of the Thematic in the main getThematics TODO : no more pos
         * @param partyPos The position of the Party in the thematic TODO no more pos
         * @param username the name of the player
         * @param secretId The secret if of the player
         */
        public void onPartyJoined(final int thematicPos, final int partyPos, final String username, final int secretId);
    }

    public interface PartyCreatedListener extends FragmentEvent {
        /**
         * When a party is created
         * @param thematicId The ID of the thematic of the party
         * @param secretId The ID of the secret of the player
         * @param partyName The name of the party
         * @param duration The duration of the party
         * @param username The name of the first player
         */
        public void onPartyCreated(final int thematicId, final int secretId, final String partyName, final int duration, final String username);
    }

    public interface BuzzListener extends FragmentEvent {
        /**
         * Buzz a user
         * @param buzer the user who launches the buzz process
         * @param buzee the user buzzed by the other one.
         */
        public void onBuzzPlayer(User buzer, User buzee);

        /**
         * Should return the current party
         * @return the current party or null if there is no current party
         */
        public Party getParty();
        }

}
