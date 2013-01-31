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

package de.meldanor.data;

import java.nio.ByteBuffer;

public class Client {

    private String name;
    private ByteBuffer buffer = ByteBuffer.allocate(4096);

    public Client(String name) {
        this.name = name;
        this.buffer = ByteBuffer.allocate(4096);
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public String getName() {
        return name;
    }
}
