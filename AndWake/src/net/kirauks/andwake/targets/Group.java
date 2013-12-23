package net.kirauks.andwake.targets;

import java.util.ArrayList;
import java.util.List;

public class Group {
	private long id;
	private String name;
	private ArrayList<Computer> children;
	
	public Group(){
		this.children = new ArrayList<Computer>();
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<Computer> getChildren(){
		return this.children;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	@Override
	public String toString(){
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((children == null) ? 0 : children.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Group other = (Group) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
