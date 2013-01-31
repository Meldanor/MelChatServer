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
