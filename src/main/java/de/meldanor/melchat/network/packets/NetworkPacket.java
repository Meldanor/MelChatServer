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

public abstract class NetworkPacket {

    /**
     * END OF TRANSMISSION CONTROL CODE
     */
    public static final byte ETX = 3;

    /**
     * Buffer per packet
     */
    protected ByteBuffer packetBuffer;

    /**
     * Create a network packet with an buffer. This is used to encode packets
     * and sent them
     * 
     * @param bufferSize
     *            The size of the buffer
     */
    public NetworkPacket(int bufferSize) {
        this.packetBuffer = ByteBuffer.allocate(bufferSize);
    }

    /**
     * Create a network packet based of incoming bytes. This is used to decode
     * packets and read them
     * 
     * @param buffer
     *            The incoming buffer
     */
    public NetworkPacket(ByteBuffer buffer) {
        extractData(buffer);
    }

    /**
     * Get the data of the packet as one byte string. <br>
     * 
     * @return ByteBuffer containing the byte string.
     */
    public final ByteBuffer getData() {
        packetBuffer.clear();
        return onGetData();
    }

    /**
     * The encoding algorithm to convert the data of the packet to one single
     * byte stream
     * 
     * @return ByteBuffer containing the encoded byte string
     */
    public abstract ByteBuffer onGetData();

    /**
     * Fill the packet with data from the buffer. This methods invokes
     * {@link NetworkPacket#onExtractData(ByteBuffer)}
     * 
     * @param buffer
     *            The Buffer containing the byte string
     */
    public final void extractData(ByteBuffer buffer) {
        onExtractData(buffer);
        buffer.clear();
    }

    /**
     * The decoding algorithm to convert byte string into readable data
     * 
     * @param buffer
     *            The Buffer containing the byte string
     */
    public abstract void onExtractData(ByteBuffer buffer);

    protected final int endOfString(byte[] bytes) {
        // SENTINEL LOOP
        bytes[bytes.length - 1] = ETX;
        int i = 0;
        while (bytes[i] != ETX)
            ++i;
        return i;
    }

}
