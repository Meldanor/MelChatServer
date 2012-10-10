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

package de.meldanor.melchat.packet;

import java.nio.ByteBuffer;

import junit.framework.TestCase;

import org.junit.Test;

import de.meldanor.melchat.network.PacketHandler;
import de.meldanor.melchat.network.packets.LoginPacket;
import de.meldanor.melchat.network.packets.NetworkPacket;

public class LoginPacketTest extends TestCase {

    @Test
    public void testNormalPacket() {

        PacketHandler handler = PacketHandler.getInstance();
        NetworkPacket mPacket = new LoginPacket("Kilian");

        // "SEND PACKET"
        ByteBuffer buffer = handler.preparePacket(mPacket);

        // "RECEIVE PACKET"
        NetworkPacket mPacket2 = handler.createPacket(buffer);
        assertEquals(mPacket2.toString(), mPacket.toString());

    }

    @Test
    public void testMaxPacket() {

        PacketHandler handler = PacketHandler.getInstance();
        NetworkPacket mPacket = new LoginPacket("Lorem ipsum dolor sit amet, ");

        // "SEND PACKET"
        ByteBuffer buffer = handler.preparePacket(mPacket);

        // "RECEIVE PACKET"
        NetworkPacket mPacket2 = handler.createPacket(buffer);
        assertEquals(mPacket2.toString(), mPacket.toString());

    }

    @Test
    public void testEmptyPacket() {

        PacketHandler handler = PacketHandler.getInstance();
        NetworkPacket mPacket = new LoginPacket("");

        // "SEND PACKET"
        ByteBuffer buffer = handler.preparePacket(mPacket);

        // "RECEIVE PACKET"
        NetworkPacket mPacket2 = handler.createPacket(buffer);
        assertEquals(mPacket2.toString(), mPacket.toString());

    }

}
