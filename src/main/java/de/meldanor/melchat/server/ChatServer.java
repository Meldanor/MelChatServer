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

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

import de.meldanor.melchat.exception.TextToLargeException;
import de.meldanor.melchat.network.packets.MessagePacket;
import de.meldanor.melchat.network.packets.NetworkPacket;
import de.meldanor.melchat.network.packets.PacketType;

public class ChatServer implements Runnable {

    private Scanner scanner;

    public boolean isRunning = true;

    private Thread loginThread;

    private Timer timer;

    private ServerSocket socket;

    private List<ConnectedClient> clients = Collections.synchronizedList(new ArrayList<ConnectedClient>(16));

    public ChatServer(Scanner scanner) {
        this.scanner = scanner;

        init();
    }

    private void init() {
        System.out.println("Prepare Chat Server...");

        System.out.println("-----------------");
        System.out.print("Enter a port for this server:");
        int port = scanner.nextInt();
        System.out.println("This server listens to the port " + port);

        try {
            socket = new ServerSocket(port);
            loginThread = new Thread(new LoginThread(this, socket));
            loginThread.start();

//            timer = new Timer();
//            timer.schedule(new PingThread(this), 0L, TimeUnit.SECONDS.toMillis(10));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void addClient(ConnectedClient client) {
        System.out.println("Client " + client.getNickname() + " with the address " + client.getAddress() + " connected!");
        new Thread(client).start();
        clients.add(client);
    }

    public synchronized void closeClient(ConnectedClient client) {
        if (!client.getSocket().isClosed()) {
            try {
                client.getSocket().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clients.remove(client);
        System.out.println("Client " + client.getNickname() + " with the address " + client.getAddress() + " disconnected!");
    }

    public List<ConnectedClient> getClients() {
        return new ArrayList<ConnectedClient>(clients);
    }

    public synchronized void handleReceivedPacket(NetworkPacket packet, ConnectedClient receiver) {
        // GET PACKET TYPE
        byte packetID = PacketType.getPacketID(packet);

        // HANDLE DIFFERENT KINDS OF PACKETS
        switch (packetID) {
            case 1 :
                MessagePacket messagePacket = (MessagePacket) packet;
                broadcastMessage(messagePacket.getText(), messagePacket.getSender());
                break;
        }
    }

    public synchronized void broadcastMessage(String message, String sender) {
        // PREPARE PACKET
        MessagePacket packet = null;
        try {
            packet = new MessagePacket(sender, "ALL", message);
        } catch (TextToLargeException e1) {
            System.out.println("Message to long! Length = " + message.length());
            e1.printStackTrace();
            return;
        }

        System.out.println("Message from " + sender + ": " + message);
        // SEND PACKET TO EVERY CLIENT
        for (ConnectedClient client : clients) {
            try {
                client.sendPacket(packet);
            } catch (Exception e) {
                System.out.println("Can't send a message to client " + client.getNickname() + "!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            // LISTENING TO EVERYTHING
            try {
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            timer.cancel();
            loginThread.interrupt();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
