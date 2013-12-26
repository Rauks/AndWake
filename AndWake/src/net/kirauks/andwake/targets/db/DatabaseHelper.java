package net.kirauks.andwake.targets.db;

import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private AtomicInteger mOpenCounter = new AtomicInteger();
	
	private static DatabaseHelper instance = null;
    private SQLiteDatabase mDatabase;

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "andwake_targets.db";
	public static final String TARGETS_TABLE_NAME = "targets";

	public static final String TARGETS_TABLE_FIELD_ID = "_id";
	public static final String TARGETS_TABLE_FIELD_NAME = "name";
	public static final String TARGETS_TABLE_FIELD_MAC = "mac";
	public static final String TARGETS_TABLE_FIELD_ADDRESS = "address";
	public static final String TARGETS_TABLE_FIELD_PORT = "port";
	private static final String TARGETS_TABLE_CREATE = "CREATE TABLE "
			+ DatabaseHelper.TARGETS_TABLE_NAME + " ("
			+ DatabaseHelper.TARGETS_TABLE_FIELD_ID + " INTEGER PRIMARY KEY, "
			+ DatabaseHelper.TARGETS_TABLE_FIELD_NAME + " TEXT, "
			+ DatabaseHelper.TARGETS_TABLE_FIELD_ADDRESS + " TEXT, "
			+ DatabaseHelper.TARGETS_TABLE_FIELD_MAC + " TEXT, "
			+ DatabaseHelper.TARGETS_TABLE_FIELD_PORT + " INTEGER);";
	public static final String GROUPS_TABLE_NAME = "groups";

	public static final String GROUPS_TABLE_FIELD_ID = "_id";
	public static final String GROUPS_TABLE_FIELD_NAME = "name";
	private static final String GROUPS_TABLE_CREATE = "CREATE TABLE "
			+ DatabaseHelper.GROUPS_TABLE_NAME + " ("
			+ DatabaseHelper.GROUPS_TABLE_FIELD_ID + " INTEGER PRIMARY KEY, "
			+ DatabaseHelper.GROUPS_TABLE_FIELD_NAME + " TEXT);";
	public static final String LINK_TARGET_GROUP_TABLE_NAME = "link_target_group";

	public static final String LINK_TARGET_GROUP_FIELD_GROUP = "group_id";
	public static final String LINK_TARGET_GROUP_FIELD_TARGET = "target_id";
	private static final String LINK_TARGET_GROUP_TABLE_CREATE = "CREATE TABLE "
			+ DatabaseHelper.LINK_TARGET_GROUP_TABLE_NAME
			+ " ("
			+ DatabaseHelper.LINK_TARGET_GROUP_FIELD_GROUP
			+ " INTEGER, "
			+ DatabaseHelper.LINK_TARGET_GROUP_FIELD_TARGET + " INTEGER);";
	private static final String LINK_TARGET_GROUP_DELETE_TARGET_TRIGGER = "CREATE TRIGGER delete_link_target_group_target BEFORE DELETE ON "
			+ DatabaseHelper.TARGETS_TABLE_NAME
			+ " "
			+ "FOR EACH ROW BEGIN "
			+ "DELETE FROM "
			+ DatabaseHelper.LINK_TARGET_GROUP_TABLE_NAME
			+ " WHERE "
			+ DatabaseHelper.LINK_TARGET_GROUP_FIELD_TARGET
			+ " = OLD." + DatabaseHelper.TARGETS_TABLE_FIELD_ID + "; " + "END";

	private static final String LINK_TARGET_GROUP_DELETE_GROUP_TRIGGER = "CREATE TRIGGER delete_link_target_group_group BEFORE DELETE ON "
			+ DatabaseHelper.GROUPS_TABLE_NAME
			+ " "
			+ "FOR EACH ROW BEGIN "
			+ "DELETE FROM "
			+ DatabaseHelper.LINK_TARGET_GROUP_TABLE_NAME
			+ " WHERE "
			+ DatabaseHelper.LINK_TARGET_GROUP_FIELD_GROUP
			+ " = OLD." + DatabaseHelper.GROUPS_TABLE_FIELD_ID + "; " + "END";

	public static synchronized DatabaseHelper getInstance(Context context) {
		if (DatabaseHelper.instance == null) {
			DatabaseHelper.instance = new DatabaseHelper(
					context.getApplicationContext());
		}
		return DatabaseHelper.instance;
	}

	private DatabaseHelper(Context context) {
		super(context, DatabaseHelper.DATABASE_NAME, null,
				DatabaseHelper.DATABASE_VERSION);
	}
	
    public SQLiteDatabase openDatabase() {
        if(this.mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            this.mDatabase = DatabaseHelper.instance.getWritableDatabase();
        }
        return this.mDatabase;
    }

    public void closeDatabase() {
        if(this.mOpenCounter.decrementAndGet() == 0) {
            // Closing database
        	this.mDatabase.close();

        }
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DatabaseHelper.TARGETS_TABLE_CREATE);
		db.execSQL(DatabaseHelper.GROUPS_TABLE_CREATE);
		db.execSQL(DatabaseHelper.LINK_TARGET_GROUP_TABLE_CREATE);
		db.execSQL(DatabaseHelper.LINK_TARGET_GROUP_DELETE_TARGET_TRIGGER);
		db.execSQL(DatabaseHelper.LINK_TARGET_GROUP_DELETE_GROUP_TRIGGER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
