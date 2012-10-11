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

public class ChatClient implements Runnable {

    private Scanner scanner;

    public boolean isRunning = true;

    private Socket socket;

    private String nickName;

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

        login(host, port, nickname);
    }

    private void login(String host, int port, String nickName) {
        try {
            System.out.println("Login startet...");
            socket = new Socket(InetAddress.getByName(host), port);
            System.out.println("Schreibe LoginPacket...");
            socket.getOutputStream().write(PacketHandler.getInstance().preparePacket(new LoginPacket(nickName)).array());
            System.out.println("Fertig!");

            this.nickName = nickName;
        } catch (Exception e) {
            e.printStackTrace();
            this.isRunning = false;
        }
    }

    private void sendMessage(String text) {
        try {
            System.out.println(text);
            socket.getOutputStream().write(PacketHandler.getInstance().preparePacket(new MessagePacket(nickName, "ALL", text)).array());

            socket.getOutputStream().flush();
            System.out.println(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        String text = null;
        while (isRunning) {

            text = scanner.nextLine();
            if (text != null) {
                System.out.println(text);
                sendMessage(text);
            }
            try {

                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
