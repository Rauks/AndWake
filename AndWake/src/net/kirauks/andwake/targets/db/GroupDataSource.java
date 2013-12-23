package net.kirauks.andwake.targets.db;

import java.util.ArrayList;
import java.util.List;

import net.kirauks.andwake.targets.Group;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class GroupDataSource {
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { DatabaseHelper.GROUPS_TABLE_FIELD_ID,
			DatabaseHelper.GROUPS_TABLE_FIELD_NAME};
	private String[] allLinkColumns = { DatabaseHelper.LINK_TARGET_GROUP_FIELD_GROUP,
			DatabaseHelper.LINK_TARGET_GROUP_FIELD_TARGET};
	
	private ComputerDataSource computerDataSource;
	
	public GroupDataSource(Context context, ComputerDataSource computerDataSource) {
	    this.dbHelper = new DatabaseHelper(context);
	    this.computerDataSource = computerDataSource;
	}
	
	public void open() throws SQLException {
	    this.db = this.dbHelper.getWritableDatabase();
	}
	
	public void close() {
	    this.db.close();
	}
	
	public Group createGroup(String name, long[] computerIds){
		ContentValues values = new ContentValues();
	    values.put(DatabaseHelper.GROUPS_TABLE_FIELD_NAME, name);
	    long insertId = this.db.insert(DatabaseHelper.GROUPS_TABLE_NAME, null, values);
	    Cursor cursor = this.db.query(DatabaseHelper.GROUPS_TABLE_NAME,
	        this.allColumns, DatabaseHelper.GROUPS_TABLE_FIELD_ID + " = " + insertId, 
	        null, null, null, null);
	    cursor.moveToFirst();
	    Group newComputer = cursorToGroup(cursor);
	    cursor.close();
	    return newComputer;
	}

	public void updateGroup(Group group) {
		ContentValues values = new ContentValues();
	    values.put(DatabaseHelper.GROUPS_TABLE_FIELD_NAME, group.getName());
		this.db.update(DatabaseHelper.GROUPS_TABLE_NAME, values, DatabaseHelper.GROUPS_TABLE_FIELD_ID + " = " + group.getId(), null);
	}
	
	public void deleteGroup(Group group) {
	    long id = group.getId();
	    this.db.delete(DatabaseHelper.GROUPS_TABLE_NAME, DatabaseHelper.GROUPS_TABLE_FIELD_ID
	        + " = " + id, null);
	}

	public List<Group> getAllGroups() {
	    List<Group> groups = new ArrayList<Group>();
	    Cursor cursor = this.db.query(DatabaseHelper.GROUPS_TABLE_NAME, this.allColumns, 
	    	null, null, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	Group group = populateChildren(cursorToGroup(cursor));
	    	groups.add(group);
			cursor.moveToNext();
	    }
	    cursor.close();
	    return groups;
	}
	
	private Group populateChildren(Group group){
		List<Long> ids = new ArrayList<Long>();
	    Cursor cursor = this.db.query(DatabaseHelper.LINK_TARGET_GROUP_TABLE_NAME, this.allLinkColumns, 
		    	DatabaseHelper.LINK_TARGET_GROUP_FIELD_GROUP + " = " + group.getId(), null, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	ids.add((long)cursor.getInt(1));
			cursor.moveToNext();
	    }
	    cursor.close();
	    
	    long[] idsP = new long[ids.size()];
	    for(int i = 0; i < ids.size(); i++){
	    	idsP[i] = ids.get(i);
	    }
	    
		group.getChildren().clear();
		group.getChildren().addAll(this.computerDataSource.getComputers(idsP));
		return group;
	}
	
	private Group cursorToGroup(Cursor cursor) {
		Group group = new Group();
		group.setId(cursor.getLong(0));
		group.setName(cursor.getString(1));
	    return group;
	}
}
