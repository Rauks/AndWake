package net.kirauks.andwake.targets.db;

import net.kirauks.andwake.targets.Computer;
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
    public static final String TARGETS_TABLE_FIELD_GROUPS = "groups";
    public static final char TARGETS_TABLE_FIELD_GROUPS_SEPARATOR = '#';
    private static final String TARGETS_TABLE_CREATE =
        "CREATE TABLE " + TARGETS_TABLE_NAME + " (" +
        TARGETS_TABLE_FIELD_ID + " INTEGER PRIMARY KEY, " +
        TARGETS_TABLE_FIELD_NAME + " TEXT, " +
        TARGETS_TABLE_FIELD_ADDRESS + " TEXT, " +
        TARGETS_TABLE_FIELD_MAC + " TEXT, " +
        TARGETS_TABLE_FIELD_PORT + " INTEGER, " +
        TARGETS_TABLE_FIELD_GROUPS + " TEXT);";
    
    public static final String GROUPS_TABLE_NAME = "groups";
    public static final String GROUPS_TABLE_FIELD_ID = "_id";
    public static final String GROUPS_TABLE_FIELD_NAME = "name";
    private static final String GROUPS_TABLE_CREATE = 
        "CREATE TABLE " + GROUPS_TABLE_NAME + " (" +
        GROUPS_TABLE_FIELD_ID + " INTEGER PRIMARY KEY, " +
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

	public void createComputer(Computer target) {
		this.getWritableDatabase().execSQL("INSERT INTO " + TARGETS_TABLE_NAME +
			" (" + TARGETS_TABLE_FIELD_NAME + ", " + TARGETS_TABLE_FIELD_ADDRESS + ", " + TARGETS_TABLE_FIELD_MAC + ", " + TARGETS_TABLE_FIELD_PORT + ")" +
			" VALUES (" + target.getName() + ", " + target.getAddress() + ", " + target.getMac() + ", " + String.valueOf(target.getPort()) + ")");
	}
}
