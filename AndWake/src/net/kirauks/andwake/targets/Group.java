package net.kirauks.andwake.targets;

import java.util.ArrayList;
import java.util.List;

public class Group {
	private String name;
	private ArrayList<Computer> children;
	
	public Group(String name){
		this.name = name;
		this.children = new ArrayList<Computer>();
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
