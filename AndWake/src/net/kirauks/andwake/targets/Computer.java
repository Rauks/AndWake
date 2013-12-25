package net.kirauks.andwake.targets;

import android.os.Parcel;
import android.os.Parcelable;

public class Computer implements Parcelable {
	private long id;
	private String name = "";
	private String mac = "";
	private String address = "";
	private int port = 0;

	public static final Parcelable.Creator<Computer> CREATOR = new Parcelable.Creator<Computer>() {
		@Override
		public Computer createFromParcel(Parcel source) {
			Computer c = new Computer();
			c.setId(source.readLong());
			c.setName(source.readString());
			c.setMac(source.readString());
			c.setAddress(source.readString());
			c.setPort(source.readInt());
			return c;
		}

		@Override
		public Computer[] newArray(int size) {
			return new Computer[size];
		}
	};

	public Computer() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Computer other = (Computer) obj;
		if (this.address == null) {
			if (other.address != null) {
				return false;
			}
		} else if (!this.address.equals(other.address)) {
			return false;
		}
		if (this.id != other.id) {
			return false;
		}
		if (this.mac == null) {
			if (other.mac != null) {
				return false;
			}
		} else if (!this.mac.equals(other.mac)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (this.port != other.port) {
			return false;
		}
		return true;
	}

	public String getAddress() {
		return this.address;
	}

	public long getId() {
		return this.id;
	}

	public String getMac() {
		return this.mac;
	}

	public String getName() {
		return this.name;
	}

	public int getPort() {
		return this.port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.address == null) ? 0 : this.address.hashCode());
		result = (prime * result) + (int) (this.id ^ (this.id >>> 32));
		result = (prime * result)
				+ ((this.mac == null) ? 0 : this.mac.hashCode());
		result = (prime * result)
				+ ((this.name == null) ? 0 : this.name.hashCode());
		result = (prime * result) + this.port;
		return result;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "Computer [id=" + this.id + ", name=" + this.name + ", mac="
				+ this.mac + ", address=" + this.address + ", port="
				+ this.port + "]";
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeString(this.name);
		dest.writeString(this.mac);
		dest.writeString(this.address);
		dest.writeInt(this.port);
	}
}
