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

package de.meldanor.melchat.core;

import java.nio.charset.Charset;
import java.util.InputMismatchException;
import java.util.Scanner;

import de.meldanor.melchat.client.ChatClient;
import de.meldanor.melchat.server.ChatServer;

public class MelChatCore {

    public final static Charset CHARSET = Charset.forName("UTF-8");

    private final static int SERVER_OPTION = 1;
    private final static int CLIENT_OPTION = 2;

    public static void main(String[] args) {
        System.out.println("---------------");
        System.out.println("----MelChat----");
        System.out.println("---------------");

        System.out.println();

        boolean menu = false;
        Thread thread = null;
        do {
            System.out.println("(" + SERVER_OPTION + ") - Start chat server");
            System.out.println("(" + CLIENT_OPTION + ") - Start chat client");
            System.out.print("Option: ");
            Scanner scanner = new Scanner(System.in);
            try {
                int i = scanner.nextInt();
                switch (i) {
                    case SERVER_OPTION :
                        thread = new Thread(new ChatServer(scanner), "ServerThread");
                        break;
                    case CLIENT_OPTION :
                        thread = new Thread(new ChatClient(scanner), "ClientThread");
                        break;
                    default :
                        System.out.println("Wrong input!");
                        System.out.println("---------------");
                        menu = true;
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong input! Please correct your input and try it again.");
                System.out.println("---------------");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (menu);

        if (thread != null) {
            thread.start();
        } else {
            System.out.println("Error! No Thread created!");
        }

    }
}
