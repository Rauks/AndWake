package net.kirauks.andwake.targets.db;

import android.content.Context;

public class DataSourceHelper {
    private ComputerDataSource computerDataSource;
    private GroupDataSource groupDataSource;

    public DataSourceHelper(Context context){
	    this.computerDataSource = new ComputerDataSource(context);
	    this.computerDataSource.open();
	    this.groupDataSource = new GroupDataSource(context, this.computerDataSource);
	    this.groupDataSource.open();
    }
    
    public void open(){
        this.computerDataSource.open();
		this.groupDataSource.open();
    }
    
    public void close(){
		this.groupDataSource.close();
        this.computerDataSource.close();
    }

	public ComputerDataSource getComputerDataSource() {
		return computerDataSource;
	}

	public GroupDataSource getGroupDataSource() {
		return groupDataSource;
	}
}
