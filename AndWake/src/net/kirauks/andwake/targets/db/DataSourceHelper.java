package net.kirauks.andwake.targets.db;

import android.content.Context;

public class DataSourceHelper {
    private final ComputerDataSource computerDataSource;
    private final GroupDataSource groupDataSource;

    public DataSourceHelper(Context context) {
        this.computerDataSource = new ComputerDataSource(context);
        this.groupDataSource = new GroupDataSource(context, this.computerDataSource);
    }

    public ComputerDataSource getComputerDataSource() {
        return this.computerDataSource;
    }

    public GroupDataSource getGroupDataSource() {
        return this.groupDataSource;
    }
}
