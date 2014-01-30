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

import com.secretparty.app.db.ThematicRepository;
import com.secretparty.app.models.Party;
import com.secretparty.app.models.Thematic;
import com.secretparty.app.models.User;

import java.util.List;

/**
 * Created by MagicMicky on 30/01/14.
 */
public interface FragmentEvent {

    //List<Thematic> getThematics();
    ThematicRepository getThematicRepository();
    List<Party> getPartiesShown();

    public interface ThematicSelectedListener extends FragmentEvent{
        /**
         * When a thematic is selected
         * @param thematicId the id of the thematic selected
         */
        public void onThematicSelected(int thematicId);
    }

    public interface PartySelectedListener extends FragmentEvent {
        /**
         * When a party is joined
         * @param partyId The id of the chosen party
         * @param secretId The secret if of the player
         */
        public void onPartyJoined(final int partyId, final int secretId);
    }

    public interface PartyCreatedListener extends FragmentEvent {
        /**
         * When a party is created
         * @param thematicId The ID of the thematic of the party
         * @param secretId The ID of the secret of the player
         * @param partyName The name of the party
         * @param duration The duration of the party
         */
        public void onPartyCreated(final int thematicId, final int secretId, final String partyName, final int duration);
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

    public interface UserCreationListener  extends FragmentEvent {
        void onUserCreated(String name);
    }
}
