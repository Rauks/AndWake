package net.kirauks.andwake.targets;

public class Target{
	private String name;
	private String mac;
	private String adress;
	
	public Target(){}
	
	public Target(String name, String mac, String adress){
		this.name = name;
		this.mac = mac;
		this.adress = adress;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMac() {
		return this.mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getAdress() {
		return this.adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
}
