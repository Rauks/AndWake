package net.kirauks.andwake.packets;

import net.kirauks.andwake.packets.task.OnPacketSendListener;
import net.kirauks.andwake.packets.task.SendPacketTask;
import android.content.Context;

public class PacketSendHelper implements OnPacketSendListener {
	private Context context;

	public PacketSendHelper(Context context) {
		this.context = context;
	}

	public void doSendPackets(Packet... packets) {
		SendPacketTask task = new SendPacketTask();
		task.setOnPacketSendListener(this);
		task.execute(packets);
	}

	public Context getContext() {
		return this.context;
	}

	@Override
	public void onPacketSend(int success, int error) {
	}

	public void setContext(Context context) {
		this.context = context;
	}
}
