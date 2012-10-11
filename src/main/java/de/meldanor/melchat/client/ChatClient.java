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

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import de.meldanor.melchat.network.PacketHandler;
import de.meldanor.melchat.network.packets.LoginPacket;
import de.meldanor.melchat.network.packets.MessagePacket;
import de.meldanor.melchat.network.packets.NetworkPacket;
import de.meldanor.melchat.network.packets.PacketType;

public class ChatClient implements Runnable {

    private Scanner scanner;

    public boolean isRunning = true;

    private Socket socket;

    private String nickName;

    private Thread listenThread;

    public ChatClient(Scanner scanner) {
        this.scanner = scanner;
        init();

    }

    private void init() {
        System.out.println("Start Chat Client...");

        System.out.print("Serveraddress: ");
        String host = scanner.next();

        System.out.print("Serverport: ");
        int port = scanner.nextInt();

        System.out.print("Your Nickname:");
        String nickname = scanner.next();

        if (!login(host, port, nickname)) {
            System.out.println("login failed!");
            this.isRunning = false;
            return;
        }

        this.listenThread = new Thread(new ListenThread(this));
        this.listenThread.start();
    }

    private boolean login(String host, int port, String nickName) {
        try {
            System.out.println("Login startet...");
            socket = new Socket(InetAddress.getByName(host), port);
            System.out.println("Schreibe LoginPacket...");
            socket.getOutputStream().write(PacketHandler.getInstance().preparePacket(new LoginPacket(nickName)).array());
            System.out.println("Fertig!");

            this.nickName = nickName;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendMessage(String text) {
        try {
            socket.getOutputStream().write(PacketHandler.getInstance().preparePacket(new MessagePacket(nickName, "ALL", text)).array());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void handleIncomingPacket(NetworkPacket packet) {

        byte packetID = PacketType.getPacketID(packet);
        switch (packetID) {
            case 1 :
                MessagePacket messagePacket = (MessagePacket) packet;
                System.out.println(messagePacket.getSender() + ": " + messagePacket.getText());
                break;

            default :

        }
    }

    @Override
    public void run() {

        String text = null;
        while (isRunning) {

            text = scanner.nextLine();
            if (text != null) {
                sendMessage(text);
            }
            try {

                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        listenThread.interrupt();
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public Socket getSocket() {
        return socket;
    }

}
