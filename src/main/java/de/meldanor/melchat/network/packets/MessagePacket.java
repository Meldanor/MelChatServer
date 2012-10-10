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
import de.meldanor.melchat.exception.TextToLargeException;

public class MessagePacket extends NetworkPacket {

    private final static int BUFFER_SIZE = 2024;

    private final static int SENDER_SIZE = 28;
    private final static int RECEIVER_SIZE = 28;

    private final static int TEXT_SIZE = 440;

    // MAX 28 Chars
    private String sender;
    // MAX 28 Chars
    private String receiver;
    // Max 440 Chars
    private String text;

    // 8 Bytes
    private long timestamp;

    public MessagePacket(ByteBuffer buffer) {
        super(buffer);
    }

    public MessagePacket(String sender, String receiver, String text) throws TextToLargeException {
        super(BUFFER_SIZE);

        // ONLY 450 CHARS PER MESSAGE
        if (text.length() > TEXT_SIZE) {
            throw new TextToLargeException();
        }

        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public ByteBuffer onGetData() {
        int pos = 0;
        packetBuffer.clear();

        // WRITE TIMESTAMP
        pos = packetBuffer.putLong(timestamp).position();

        // WRITE SENDER
        packetBuffer.put(sender.getBytes(MelChatCore.CHARSET));
        packetBuffer.put(ETX);
        pos = packetBuffer.position(pos + SENDER_SIZE + 2).position();

        // WRITE RECEIVER
        packetBuffer.put(receiver.getBytes(MelChatCore.CHARSET));
        packetBuffer.put(ETX);
        pos = packetBuffer.position(pos + RECEIVER_SIZE + 2).position();

        // WIRTE TEXT
        packetBuffer.put(text.getBytes(MelChatCore.CHARSET));
        packetBuffer.put(ETX);

        return packetBuffer;
    }

    @Override
    public void onExtractData(ByteBuffer buffer) {
        // READ TIMESTAMPS
        this.timestamp = buffer.getLong();

        byte[] bytes = new byte[SENDER_SIZE + 2 + 1];

        // READ SENDER
        buffer.get(bytes, 0, SENDER_SIZE + 2);
        this.sender = new String(bytes, 0, endOfString(bytes), MelChatCore.CHARSET);

        // READ RECEIVER
        buffer.get(bytes, 0, RECEIVER_SIZE + 2);
        this.receiver = new String(bytes, 0, endOfString(bytes), MelChatCore.CHARSET);

        // READ TEXT
        bytes = new byte[TEXT_SIZE + 2 + 1];
        buffer.get(bytes, 0, TEXT_SIZE + 2);
        this.text = new String(bytes, 0, endOfString(bytes), MelChatCore.CHARSET);
    }

    @Override
    public String toString() {
        return "MessagePacket={Timestamp=" + timestamp + ";Sender=" + sender + ";Receiver=" + receiver + ";Text=" + text + "}";
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }
    
    public long getTimestamp() {
        return timestamp;
    }

}
