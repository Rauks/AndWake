package net.kirauks.andwake.packets;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class WolPacket extends Packet{
	private final static int DEFAULT_PORT = 9;
    public static final char DEFAULT_MAC_SEPARATOR = ':';
	
	private final String ip;
	private int port = DEFAULT_PORT;
	private final String[] mac;
	
	public WolPacket(String ip, String mac) throws IllegalArgumentException{
		this.ip = ip;
		this.mac = splitMac(mac);
	}
	
	public WolPacket(String ip, String mac, int port) throws IllegalArgumentException{
		this(ip, mac);
		this.port = port;
	}

	@Override
	public int getPort(){
		return this.port;
	}
	@Override
	public String getAddress() {
		return this.ip;
	}
	@Override
	public byte[] getBytes(){
		final byte[] macBytes = new byte[6];
		final byte[] wolBytes = new byte[102];
		
        for(int i=0; i < 6; i++){
            macBytes[i] = (byte) Integer.parseInt(this.mac[i], 16);
        }
        
        //Fill 6 * 0xFF
        for(int i=0; i < 6; i++){
        	wolBytes[i] = (byte) 0xff;
	    }
	    //Fill 16 * Mac
	    for(int i=6; i < wolBytes.length; i += macBytes.length){
	        System.arraycopy(macBytes, 0, wolBytes, i, macBytes.length);
	    }

	    return wolBytes;
	}
	
	private String[] splitMac(String mac) throws IllegalArgumentException{
		String cleanMac = "";

        if(mac.matches("([a-fA-F0-9]){12}")){
            for(int i=0; i < mac.length(); i++){
                if((i > 1) && (i % 2 == 0)) {
                	cleanMac += ":";
                }
                cleanMac += mac.charAt(i);
            }
        }
        else{
        	cleanMac = mac;
        }
        
        final Pattern pat = Pattern.compile("((([0-9a-fA-F]){2}[-:]){5}([0-9a-fA-F]){2})");
        final Matcher m = pat.matcher(cleanMac);

        if(m.find()){
            String result = m.group();
            return result.split("(\\:|\\-)");
        }else{
            throw new IllegalArgumentException("Invalid MAC address");
        }
	}
}
