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

package de.meldanor.melchat.server;

import java.io.BufferedInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import de.meldanor.melchat.network.PacketHandler;
import de.meldanor.melchat.network.packets.NetworkPacket;

public class LoginThread implements Runnable {

    private ServerSocket socket;
    private ChatServer chatServer;

    public LoginThread(ChatServer chatServer, ServerSocket socket) {
        this.chatServer = chatServer;
        this.socket = socket;
    }

    private ByteBuffer buffer = ByteBuffer.allocate(4096);

    @Override
    public void run() {
        Socket client = null;
        while (true) {
            try {
                client = socket.accept();
                chatServer.addClient(client);
                BufferedInputStream in = new BufferedInputStream(client.getInputStream());
                in.read(buffer.array(), 0, buffer.limit());
                NetworkPacket packet = PacketHandler.getInstance().createPacket(buffer);
                System.out.println(packet);
//                PacketHandler.getInstance().createPacket(ByteBuffer.wrap(in.read(b, off, len)))
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
