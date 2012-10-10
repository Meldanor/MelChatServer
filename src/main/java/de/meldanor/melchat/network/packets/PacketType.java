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

package de.meldanor.melchat.network.packets;

import java.util.HashMap;
import java.util.Map;

public enum PacketType {

    // @formatter:off
    MESSAGE((byte) 1, MessagePacket.class),
    LOGIN((byte) 2, LoginPacket.class);
    
    // @formatter:on

    private Class<? extends NetworkPacket> clazz;
    private byte packedID;

    private PacketType(byte packetID, Class<? extends NetworkPacket> clazz) {
        this.packedID = packetID;
        this.clazz = clazz;
    }

    public byte getPackedID() {
        return packedID;
    }

    private final static Map<Class<? extends NetworkPacket>, Byte> mapByClazz;

    static {
        mapByClazz = new HashMap<Class<? extends NetworkPacket>, Byte>();

        for (PacketType entity : values()) {
            mapByClazz.put(entity.clazz, entity.packedID);
        }
    }

    public static byte getPacketID(NetworkPacket packet) {
        return mapByClazz.get(packet.getClass());
    }
}
