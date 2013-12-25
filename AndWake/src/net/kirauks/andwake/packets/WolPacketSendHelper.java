package net.kirauks.andwake.packets;

import java.util.ArrayList;
import java.util.List;

import net.kirauks.andwake.R;
import net.kirauks.andwake.targets.Computer;
import android.content.Context;
import android.widget.Toast;

public class WolPacketSendHelper extends PacketSendHelper {
	public WolPacketSendHelper(Context context) {
		super(context);
	}

	public void doSendWakePacket(Computer computer) {
		WolPacket wakePacket = new WolPacket(computer.getAddress(),
				computer.getMac(), computer.getPort());
		this.doSendWakePackets(wakePacket);
	}

	public void doSendWakePacket(List<Computer> computers) {
		List<WolPacket> packets = new ArrayList<WolPacket>();
		for (Computer computer : computers) {
			packets.add(new WolPacket(computer.getAddress(), computer.getMac(),
					computer.getPort()));
		}
		this.doSendWakePackets(packets.toArray(new WolPacket[packets.size()]));
	}

	public void doSendWakePackets(WolPacket... packets) {
		if (packets.length == 0) {
			Toast.makeText(this.getContext(),
					R.string.toast_wake_group_empty_error, Toast.LENGTH_SHORT)
					.show();
		} else {
			if (packets.length > 1) {
				Toast.makeText(this.getContext(),
						R.string.toast_wake_group_init, Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(this.getContext(), R.string.toast_wake_init,
						Toast.LENGTH_SHORT).show();
			}
			super.doSendPackets(packets);
		}
	}

	@Override
	public void onPacketSend(int success, int error) {
		super.onPacketSend(success, error);
		if ((success + error) > 1) {
			if (error > 0) {
				Toast.makeText(this.getContext(),
						R.string.toast_wake_group_error, Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(this.getContext(),
						R.string.toast_wake_group_done, Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			if (error > 0) {
				Toast.makeText(this.getContext(), R.string.toast_wake_error,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this.getContext(), R.string.toast_wake_done,
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
