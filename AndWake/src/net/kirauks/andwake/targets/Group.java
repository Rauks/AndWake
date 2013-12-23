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
}
