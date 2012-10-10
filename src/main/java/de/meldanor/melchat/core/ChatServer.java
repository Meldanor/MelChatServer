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

package de.meldanor.melchat.core;

import java.util.Scanner;

public class ChatServer implements Runnable {

    private Scanner scanner;

    public boolean isRunning = true;

    public ChatServer(Scanner scanner) {
        System.out.println("Start Chat Server...");
        this.scanner = scanner;

        System.out.println("-----------------");
        System.out.print("Enter a port for this server:");
        int port = scanner.nextInt();
        System.out.println("This server listens to the port " + port);
    }

    @Override
    public void run() {
        while (isRunning) {

        }
    }

}
