package net.kirauks.andwake.targets.db;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.Group;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ComputerDataSource {
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { DatabaseHelper.TARGETS_TABLE_FIELD_ID,
			DatabaseHelper.TARGETS_TABLE_FIELD_NAME,
			DatabaseHelper.TARGETS_TABLE_FIELD_ADDRESS,
			DatabaseHelper.TARGETS_TABLE_FIELD_MAC,
			DatabaseHelper.TARGETS_TABLE_FIELD_PORT,
			DatabaseHelper.TARGETS_TABLE_FIELD_GROUPS};
	
	public ComputerDataSource(Context context) {
	    this.dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
	    this.db = this.dbHelper.getWritableDatabase();
	}
	
	public void close() {
	    this.db.close();
	}
	
	public Computer createComputer(String name, String mac, String address, int port){
		ContentValues values = new ContentValues();
	    values.put(DatabaseHelper.TARGETS_TABLE_FIELD_NAME, name);
	    values.put(DatabaseHelper.TARGETS_TABLE_FIELD_ADDRESS, address);
	    values.put(DatabaseHelper.TARGETS_TABLE_FIELD_MAC, mac);
	    values.put(DatabaseHelper.TARGETS_TABLE_FIELD_PORT, String.valueOf(port));
	    values.put(DatabaseHelper.TARGETS_TABLE_FIELD_GROUPS, "");
	    long insertId = this.db.insert(DatabaseHelper.TARGETS_TABLE_NAME, null, values);
	    Cursor cursor = this.db.query(DatabaseHelper.TARGETS_TABLE_NAME,
	        this.allColumns, DatabaseHelper.TARGETS_TABLE_FIELD_ID + " = " + insertId, 
	        null, null, null, null);
	    cursor.moveToFirst();
	    Computer newComputer = cursorToComputer(cursor);
	    cursor.close();
	    return newComputer;
	}

	public void updateComputer(long id, String name, String mac, String address, int port) {
		ContentValues values = new ContentValues();
	    values.put(DatabaseHelper.TARGETS_TABLE_FIELD_NAME, name);
	    values.put(DatabaseHelper.TARGETS_TABLE_FIELD_ADDRESS, address);
	    values.put(DatabaseHelper.TARGETS_TABLE_FIELD_MAC, mac);
	    values.put(DatabaseHelper.TARGETS_TABLE_FIELD_PORT, String.valueOf(port));
	    values.put(DatabaseHelper.TARGETS_TABLE_FIELD_GROUPS, "");
		this.db.update(DatabaseHelper.TARGETS_TABLE_NAME, values, DatabaseHelper.TARGETS_TABLE_FIELD_ID + " = " + id, null);
	}
	
	public void deleteComputer(Computer computer) {
	    long id = computer.getId();
	    this.db.delete(DatabaseHelper.TARGETS_TABLE_NAME, DatabaseHelper.TARGETS_TABLE_FIELD_ID
	        + " = " + id, null);
	}

	public List<Computer> getAllComputers() {
	    List<Computer> computers = new ArrayList<Computer>();
	    Cursor cursor = this.db.query(DatabaseHelper.TARGETS_TABLE_NAME, this.allColumns, 
	    	null, null, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	Computer computer = cursorToComputer(cursor);
	    	computers.add(computer);
			cursor.moveToNext();
	    }
	    cursor.close();
	    return computers;
	}
	
	private Computer cursorToComputer(Cursor cursor) {
		Computer computer = new Computer();
		computer.setId(cursor.getLong(0));
		computer.setName(cursor.getString(1));
		computer.setAddress(cursor.getString(2));
		computer.setMac(cursor.getString(3));
		computer.setPort(cursor.getInt(4));
	    String groups = cursor.getString(5);
	    StringTokenizer st = new StringTokenizer(groups, String.valueOf(DatabaseHelper.TARGETS_TABLE_FIELD_GROUPS_SEPARATOR));
	    while(st.hasMoreElements()){		
	    	computer.getGroups().add(new Group(st.nextToken()));
	    }
	    return computer;
	}
}
