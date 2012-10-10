/*
 * Copyright (C) 2012 Kilian G�rtner
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

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.TimeUnit;


public class ChatServer implements Runnable {

    private Scanner scanner;

    public boolean isRunning = true;

    private Thread loginThread;

    private Timer timer;

    private ServerSocket socket;

    private List<Socket> clients = Collections.synchronizedList(new ArrayList<Socket>(16));

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

            timer = new Timer();
            timer.schedule(new PingThread(this), 0L, TimeUnit.SECONDS.toMillis(10));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void addClient(Socket client) {
        System.out.println("Client " + client.getInetAddress().toString() + " connected!");
        clients.add(client);
    }

    public synchronized void closeClient(Socket client) {
        InetAddress address = client.getInetAddress();
        if (!client.isClosed()) {
            try {
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        clients.remove(client);
        System.out.println("Client " + address + " timed out!");
    }

    public List<Socket> getClients() {
        return new ArrayList<Socket>(clients);
    }

    @Override
    public void run() {
        while (isRunning) {

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