/*
 * Copyright (C) 2013 Kilian Gaertner
 * 
 * This file is part of MelChatServer.
 * 
 * MelChatServer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * MelChatServer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MelChatServer.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.meldanor.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import de.meldanor.data.Client;

public class NetworkHandler {

    private Selector selector;

    private ServerSocketChannel serverSocket;

    public NetworkHandler(String port) throws Exception {
        init(port);
    }

    private void init(String port) throws Exception {
        selector = Selector.open();

        // Create server socket at the port
        serverSocket = ServerSocketChannel.open().bind(new InetSocketAddress(Integer.parseInt(port)));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void serverLoop() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
        try {
            while (true) {
                int rdyChannels = selector.select(0);
                if (rdyChannels == 0)
                    continue;

                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    buffer.clear();
                    // New client want to connect
                    if (key.isAcceptable()) {
                        // accept new client
                        SocketChannel clientSocket = serverSocket.accept();
                        clientSocket.configureBlocking(false);
                        // create local object representing all necessary
                        // information
                        Client client = new Client(clientSocket.getRemoteAddress().toString());
                        // register new client to selector
                        clientSocket.register(selector, SelectionKey.OP_READ).attach(client);
                        System.out.println("New client " + client.getName() + " connected.");
                    } else if (key.isReadable()) {
                        // Client want something
                        if (key.channel() instanceof SocketChannel) {
                            Client client = (Client) key.attachment();
                            SocketChannel channel = (SocketChannel) key.channel();
                            try {
                                channel.read(buffer);
                                buffer.flip();
                                ByteBuffer clientBuffer = client.getBuffer();
                                clientBuffer.put(buffer);
                                clientBuffer.flip();
                                channel.write(clientBuffer);
                                clientBuffer.clear();
                            } catch (IOException IOe) {
                                key.cancel();
                                System.out.println("Client " + client.getName() + " disconnected.");
                            }
                        }
                        // TODO: Console input
                        else {

                        }
                    }
                    it.remove();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            selector.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
