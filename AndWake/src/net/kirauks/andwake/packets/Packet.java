package net.kirauks.andwake.packets;

public abstract class Packet {
	public abstract String getAddress();
	public abstract int getPort();
	public abstract byte[] getBytes();
}
