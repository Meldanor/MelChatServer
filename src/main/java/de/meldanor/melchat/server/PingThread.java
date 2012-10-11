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

import java.util.List;
import java.util.TimerTask;

public class PingThread extends TimerTask {

    /**
     * Max Timeout is 30 seconds
     */
    private final static int TIMEOUT = 30000;

    private ChatServer chatServer;

    public PingThread(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    @Override
    public void run() {
        List<ConnectedClient> clients = chatServer.getClients();
        try {
            for (ConnectedClient client : clients) {
                if (client.getSocket().isClosed() || !client.getSocket().getInetAddress().isReachable(TIMEOUT))
                    chatServer.closeClient(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
