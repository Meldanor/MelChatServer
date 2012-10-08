/*
 * Copyright (C) 2012 Kilian Gärtner
 * 
 * This file is part of MelChat.
 * 
 * MelChat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MelChat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MelChat.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.meldanor.melchat.core;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import de.meldanor.melchat.exception.TextToLargeException;
import de.meldanor.melchat.packets.MessagePacket;
import de.meldanor.melchat.packets.NetworkPacket;

public class MelChatCore {

    public final static Charset CHARSET = Charset.forName("UTF-8");

    public static void main(String[] args) {

        // TESTS
        try {
            NetworkPacket mPacket = new MessagePacket("Kilian", "Florian", "Hallo Florian!");
            System.out.println(mPacket);
            // "SEND PACKET"
            ByteBuffer buffer = mPacket.onGetData();

            // "RECEIVE PACKET"
            NetworkPacket mPacket2 = new MessagePacket(buffer);
            System.out.println(mPacket2);
        } catch (TextToLargeException e) {
            e.printStackTrace();
        }
    }

}
