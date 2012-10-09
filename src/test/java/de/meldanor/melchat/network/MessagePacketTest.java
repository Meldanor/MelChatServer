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

package de.meldanor.melchat.network;

import java.nio.ByteBuffer;

import junit.framework.TestCase;

import org.junit.Test;

import de.meldanor.melchat.exception.TextToLargeException;
import de.meldanor.melchat.network.packets.MessagePacket;
import de.meldanor.melchat.network.packets.NetworkPacket;

public class MessagePacketTest extends TestCase {

    @Test
    public void testNormalPacket() {

        try {
            PacketHandler handler = PacketHandler.getInstance();
            NetworkPacket mPacket = new MessagePacket("Kilian", "Florian", "Hallo Florian!");

            // "SEND PACKET"
            ByteBuffer buffer = handler.preparePacket(mPacket);

            // "RECEIVE PACKET"
            NetworkPacket mPacket2 = handler.createPacket(buffer);

            assertEquals(mPacket2.toString(), mPacket.toString());
        } catch (TextToLargeException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testMaxPacket() {

        try {
            PacketHandler handler = PacketHandler.getInstance();
            // SENDER = 28 CHARS
            // RECEIVER = 28 CHARS
            // TEXT = 440 CHARS
            NetworkPacket mPacket = new MessagePacket("Lorem ipsum dolor sit amet, ", "Lorem ipsum dolor sit amet, ", "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed dia");

            // "SEND PACKET"
            ByteBuffer buffer = handler.preparePacket(mPacket);

            // "RECEIVE PACKET"
            NetworkPacket mPacket2 = handler.createPacket(buffer);

            assertEquals(mPacket2.toString(), mPacket.toString());
        } catch (TextToLargeException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testEmptyPacket() {

        try {
            PacketHandler handler = PacketHandler.getInstance();
            // SENDER = 28 CHARS
            // RECEIVER = 28 CHARS
            // TEXT = 440 CHARS
            NetworkPacket mPacket = new MessagePacket("", "", "");

            // "SEND PACKET"
            ByteBuffer buffer = handler.preparePacket(mPacket);

            // "RECEIVE PACKET"
            NetworkPacket mPacket2 = handler.createPacket(buffer);

            assertEquals(mPacket2.toString(), mPacket.toString());
        } catch (TextToLargeException e) {
            e.printStackTrace();
        }

    }

}
