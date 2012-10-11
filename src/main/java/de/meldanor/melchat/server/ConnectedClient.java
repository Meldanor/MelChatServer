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

public class ConnectedClient {

    private String nickname;
    private String address;
    private Socket socket;

    public ConnectedClient(String nickname, Socket socket) {
        this.nickname = nickname;
        this.socket = socket;
        this.address = this.socket.getInetAddress().toString();
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

}
