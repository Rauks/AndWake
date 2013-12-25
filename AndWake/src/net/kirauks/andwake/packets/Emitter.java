package net.kirauks.andwake.packets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Emitter {
	private final Packet packet;

	public Emitter(Packet packet) {
		this.packet = packet;
	}

	public void send() throws IOException {
		byte[] bytes = this.packet.getBytes();
		int port = this.packet.getPort();
		String address = this.packet.getAddress();

		final InetAddress inet = InetAddress.getByName(address);
		final DatagramPacket packet = new DatagramPacket(bytes, bytes.length,
				inet, port);
		final DatagramSocket socket = new DatagramSocket();

		socket.send(packet);
		socket.close();
	}
}
