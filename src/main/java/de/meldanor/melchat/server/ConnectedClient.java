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

import java.net.Socket;
import java.nio.ByteBuffer;

import de.meldanor.melchat.network.PacketHandler;
import de.meldanor.melchat.network.packets.NetworkPacket;

public class ConnectedClient implements Runnable {

    private ByteBuffer buffer = ByteBuffer.allocate(4096);

    private String nickname;
    private String address;
    private Socket socket;

    private ChatServer server;

    public ConnectedClient(String nickname, Socket socket, ChatServer server) {
        this.nickname = nickname;
        this.socket = socket;
        this.address = this.socket.getInetAddress().toString();
        this.server = server;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAddress() {
        return address;
    }

    public Socket getSocket() {
        return socket;
    }

    public void sendPacket(NetworkPacket packet) throws Exception {
        // SEND THE PACKET TO THE STREAM
        socket.getOutputStream().write(PacketHandler.getInstance().preparePacket(packet).array());
    }

    @Override
    public void run() {

        // READ PACKETS
        NetworkPacket receivedPacket = null;
        while (socket != null && !socket.isClosed()) {
            buffer.clear();
            try {
                if (!socket.isInputShutdown()) {
                    socket.getInputStream().read(buffer.array(), 0, buffer.limit());
                    receivedPacket = PacketHandler.getInstance().createPacket(buffer);
                    this.server.handleReceivedPacket(receivedPacket, this);
                } else {
                    System.out.println("Input stream is down!");
                }
            } catch (Exception e) {
                System.out.println("Error while reading input stream!");
                e.printStackTrace();
            }
        }
    }
}
