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

import de.meldanor.melchat.network.packets.MessagePacket;
import de.meldanor.melchat.network.packets.NetworkPacket;
import de.meldanor.melchat.network.packets.PacketType;

public class PacketHandler {

    private static final byte EOT = 4;

    private static final ByteBuffer PACKET_BUFFER = ByteBuffer.allocate(4096);

    public NetworkPacket createPacket(ByteBuffer buffer) {
        buffer.rewind();
        byte packetID = buffer.get();
        switch (packetID) {
            case 1 :
                return new MessagePacket(buffer);
            default :
                // UNKNOWN PACKET
                return null;
        }
    }

    public ByteBuffer preparePacket(NetworkPacket packet) {
        PACKET_BUFFER.clear();
        PACKET_BUFFER.put(PacketType.getPacketID(packet));
        PACKET_BUFFER.put(packet.getData());
        PACKET_BUFFER.put(EOT);
        PACKET_BUFFER.rewind();
        return PACKET_BUFFER;
    }

}
