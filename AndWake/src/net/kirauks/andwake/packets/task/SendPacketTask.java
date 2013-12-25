package net.kirauks.andwake.packets.task;

import java.io.IOException;

import net.kirauks.andwake.packets.Emitter;
import net.kirauks.andwake.packets.Packet;
import android.os.AsyncTask;

public class SendPacketTask extends AsyncTask<Packet, Void, Void>{
	private int sendError;
	private int sendSucess;
	private OnPacketSendListener onSendHandler;
	
	public void setOnPacketSendListener(OnPacketSendListener handler){
		this.onSendHandler = handler;
	}
	
	@Override
	protected Void doInBackground(Packet... params) {
		this.sendError = 0;
		this.sendSucess = 0;
		for(Packet p : params){
			try {
				new Emitter(p).send();
				this.sendSucess++;
			} catch (IOException e) {
				this.sendError++;
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(this.onSendHandler != null){
			this.onSendHandler.onPacketSend(this.sendSucess, this.sendError);
		}
	}

}
