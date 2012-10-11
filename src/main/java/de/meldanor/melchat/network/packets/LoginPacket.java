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

import java.nio.ByteBuffer;

import de.meldanor.melchat.core.MelChatCore;

public class LoginPacket extends NetworkPacket {

    private static final int BUFFER_SIZE = 128;

    private final static int CLIENT_NAME_SIZE = 28;

    private String clientName;
    private long timestamp;

    public LoginPacket(String clientName) {
        super(BUFFER_SIZE);
        this.clientName = clientName;
        this.timestamp = System.currentTimeMillis();
    }

    public LoginPacket(ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    public ByteBuffer onGetData() {

        packetBuffer.clear();

        // WRITE TIMESTAMP
        packetBuffer.putLong(timestamp).position();

        // WRITE CLIENT NAME
        packetBuffer.put(clientName.getBytes(MelChatCore.CHARSET));
        packetBuffer.put(ETX);

        return packetBuffer;
    }

    @Override
    public void onExtractData(ByteBuffer buffer) {
        // READ TIMESTAMPS
        this.timestamp = buffer.getLong();

        byte[] bytes = new byte[CLIENT_NAME_SIZE + ETX_LENGTH];

        // READ SENDER
        buffer.get(bytes, 0, CLIENT_NAME_SIZE);
        this.clientName = new String(bytes, 0, endOfString(bytes), MelChatCore.CHARSET);
    }

    public String getClientName() {
        return clientName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "LoginPacket={Timestamp=" + timestamp + ";ClientName=" + clientName + "}";
    }
}
