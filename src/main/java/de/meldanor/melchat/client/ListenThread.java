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

package de.meldanor.melchat.client;

import java.nio.ByteBuffer;

import de.meldanor.melchat.network.PacketHandler;
import de.meldanor.melchat.network.packets.NetworkPacket;

public class ListenThread implements Runnable {

    private ChatClient client;

    private ByteBuffer buffer = ByteBuffer.allocate(4096);

    public ListenThread(ChatClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        NetworkPacket packet = null;
        while (client.isRunning) {
            try {
                client.getSocket().getInputStream().read(buffer.array());
                packet = PacketHandler.getInstance().createPacket(buffer);

                client.handleIncomingPacket(packet);
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
