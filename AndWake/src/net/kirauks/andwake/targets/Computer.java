package net.kirauks.andwake.targets;

public class Computer{
	private long id;
	private String name;
	private String mac;
	private String address;
	private int port;
	
	public Computer(){
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getAddress() {
		return this.address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPort() {
		return this.port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", mac=" + mac
				+ ", address=" + address + ", port=" + port + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((mac == null) ? 0 : mac.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (id != other.id)
			return false;
		if (mac == null) {
			if (other.mac != null)
				return false;
		} else if (!mac.equals(other.mac))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
}
