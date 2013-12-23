package net.kirauks.andwake.targets.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "andwake_targets.db";
	
    public static final String TARGETS_TABLE_NAME = "targets";
    public static final String TARGETS_TABLE_FIELD_ID = "_id";
    public static final String TARGETS_TABLE_FIELD_NAME = "name";
    public static final String TARGETS_TABLE_FIELD_MAC = "mac";
    public static final String TARGETS_TABLE_FIELD_ADDRESS = "address";
    public static final String TARGETS_TABLE_FIELD_PORT = "port";
    private static final String TARGETS_TABLE_CREATE =
	        "CREATE TABLE " + TARGETS_TABLE_NAME + " (" +
	        TARGETS_TABLE_FIELD_ID + " INTEGER PRIMARY KEY, " +
	        TARGETS_TABLE_FIELD_NAME + " TEXT, " +
	        TARGETS_TABLE_FIELD_ADDRESS + " TEXT, " +
	        TARGETS_TABLE_FIELD_MAC + " TEXT, " +
	        TARGETS_TABLE_FIELD_PORT + " INTEGER);";
    
    public static final String GROUPS_TABLE_NAME = "groups";
    public static final String GROUPS_TABLE_FIELD_ID = "_id";
    public static final String GROUPS_TABLE_FIELD_NAME = "name";
    private static final String GROUPS_TABLE_CREATE = 
	        "CREATE TABLE " + GROUPS_TABLE_NAME + " (" +
	        GROUPS_TABLE_FIELD_ID + " INTEGER PRIMARY KEY, " +
	        GROUPS_TABLE_FIELD_NAME + " TEXT);";
    
    public static final String LINK_TARGET_GROUP_TABLE_NAME = "link_target_group";
    public static final String LINK_TARGET_GROUP_FIELD_GROUP = "group_id";
    public static final String LINK_TARGET_GROUP_FIELD_TARGET = "target_id";
    private static final String LINK_TARGET_GROUP_TABLE_CREATE = 
            "CREATE TABLE " + LINK_TARGET_GROUP_TABLE_NAME + " (" +
            LINK_TARGET_GROUP_FIELD_GROUP + " INTEGER, " +
            LINK_TARGET_GROUP_FIELD_TARGET + " INTEGER);";
    
    private static final String LINK_TARGET_GROUP_DELETE_TARGET_TRIGGER =
    		"CREATE TRIGGER delete_link_target_group_target BEFORE DELETE ON " + TARGETS_TABLE_NAME + " " +
	        "FOR EACH ROW BEGIN " +
	        "DELETE FROM " + LINK_TARGET_GROUP_TABLE_NAME + 
	        " WHERE " + LINK_TARGET_GROUP_FIELD_TARGET + " = OLD." + TARGETS_TABLE_FIELD_ID + "; " +
	        "END";
    private static final String LINK_TARGET_GROUP_DELETE_GROUP_TRIGGER =
    		"CREATE TRIGGER delete_link_target_group_group BEFORE DELETE ON " + GROUPS_TABLE_NAME + " " +
	        "FOR EACH ROW BEGIN " +
	        "DELETE FROM " + LINK_TARGET_GROUP_TABLE_NAME + 
	        " WHERE " + LINK_TARGET_GROUP_FIELD_GROUP + " = OLD." + GROUPS_TABLE_FIELD_ID + "; " +
	        "END";
	
	public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TARGETS_TABLE_CREATE);
		db.execSQL(GROUPS_TABLE_CREATE);
		db.execSQL(LINK_TARGET_GROUP_TABLE_CREATE);
		db.execSQL(LINK_TARGET_GROUP_DELETE_TARGET_TRIGGER);
		db.execSQL(LINK_TARGET_GROUP_DELETE_GROUP_TRIGGER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
