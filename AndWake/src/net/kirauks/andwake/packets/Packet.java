package net.kirauks.andwake.packets;

public abstract class Packet {
	public abstract String getAddress();

	public abstract byte[] getBytes();

	public abstract int getPort();

	@Override
	public String toString() {
		byte[] bytes = this.getBytes();
		final int maxBytesPrinted = 150;

		StringBuilder hexString = new StringBuilder();
		hexString.append("PACKET{");
		for (int i = 0; i < ((bytes.length < maxBytesPrinted) ? bytes.length
				: maxBytesPrinted); i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
			if (i != (bytes.length - 1)) {
				hexString.append(":");
			}
			if ((i == (maxBytesPrinted - 1))
					&& (bytes.length != maxBytesPrinted)) {
				hexString.append("...");
			}
		}
		hexString.append("}");
		return hexString.toString();
	}
}
