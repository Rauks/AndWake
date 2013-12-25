package net.kirauks.andwake.targets.db;

import android.content.Context;

public class DataSourceHelper {
	private final ComputerDataSource computerDataSource;
	private final GroupDataSource groupDataSource;

	public DataSourceHelper(Context context) {
		this.computerDataSource = new ComputerDataSource(context);
		this.computerDataSource.open();
		this.groupDataSource = new GroupDataSource(context,
				this.computerDataSource);
		this.groupDataSource.open();
	}

	public void close() {
		this.groupDataSource.close();
		this.computerDataSource.close();
	}

	public ComputerDataSource getComputerDataSource() {
		return this.computerDataSource;
	}

	public GroupDataSource getGroupDataSource() {
		return this.groupDataSource;
	}

	public void open() {
		this.computerDataSource.open();
		this.groupDataSource.open();
	}
}
