package net.kirauks.andwake.targets;

import java.util.ArrayList;
import java.util.List;

public class Group {
	private String name;
	private ArrayList<Target> children;
	
	public Group(String name){
		this.name = name;
		this.children = new ArrayList<Target>();
	}
	
	public List<Target> getChildren(){
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
