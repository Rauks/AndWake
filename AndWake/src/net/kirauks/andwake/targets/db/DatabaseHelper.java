package net.kirauks.andwake.targets.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "andwake_targets";
	
    private static final String TARGETS_TABLE_NAME = "targets";
    private static final String TARGETS_TABLE_FIELD_ID = "id";
    private static final String TARGETS_TABLE_FIELD_NAME = "name";
    private static final String TARGETS_TABLE_FIELD_MAC = "mac";
    private static final String TARGETS_TABLE_FIELD_ADRESS = "address";
    private static final String TARGETS_TABLE_FIELD_GROUPS = "groups";
    private static final String TARGETS_TABLE_CREATE =
        "CREATE TABLE " + TARGETS_TABLE_NAME + " (" +
        TARGETS_TABLE_FIELD_ID + " INTEGER AUTOINCREMENT, " +
        TARGETS_TABLE_FIELD_NAME + " TEXT, " +
        TARGETS_TABLE_FIELD_ADRESS + " TEXT, " +
        TARGETS_TABLE_FIELD_MAC + " TEXT, " +
        TARGETS_TABLE_FIELD_GROUPS + " TEXT);";
    
    private static final String GROUPS_TABLE_NAME = "groups";
    private static final String GROUPS_TABLE_FIELD_ID = "id";
    private static final String GROUPS_TABLE_FIELD_NAME = "name";
    private static final String GROUPS_TABLE_CREATE = 
        "CREATE TABLE " + GROUPS_TABLE_NAME + " (" +
        GROUPS_TABLE_FIELD_ID + " INTEGER AUTOINCREMENT, " +
        GROUPS_TABLE_FIELD_NAME + " TEXT);";
	
	public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TARGETS_TABLE_CREATE);
		db.execSQL(GROUPS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
