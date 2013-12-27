package net.kirauks.andwake.targets.db;

import java.util.ArrayList;
import java.util.List;

import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.Group;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GroupDataSource {
    private final DatabaseHelper dbHelper;
    private final String[] allColumns =
        { DatabaseHelper.GROUPS_TABLE_FIELD_ID, DatabaseHelper.GROUPS_TABLE_FIELD_NAME };
    private final String[] allLinkColumns =
        { DatabaseHelper.LINK_TARGET_GROUP_FIELD_GROUP, DatabaseHelper.LINK_TARGET_GROUP_FIELD_TARGET };

    private final ComputerDataSource computerDataSource;

    public GroupDataSource(Context context, ComputerDataSource computerDataSource) {
        this.dbHelper = DatabaseHelper.getInstance(context);
        this.computerDataSource = computerDataSource;
    }

    public Group createGroup(Group group) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.GROUPS_TABLE_FIELD_NAME, group.getName());
        SQLiteDatabase db = this.dbHelper.openDatabase();
        long insertId = db.insert(DatabaseHelper.GROUPS_TABLE_NAME, null, values);

        for (Computer computer : group.getChildren()) {
            ContentValues linkValues = new ContentValues();
            linkValues.put(DatabaseHelper.LINK_TARGET_GROUP_FIELD_TARGET, computer.getId());
            linkValues.put(DatabaseHelper.LINK_TARGET_GROUP_FIELD_GROUP, insertId);
            db.insert(DatabaseHelper.LINK_TARGET_GROUP_TABLE_NAME, null, linkValues);
        }

        Cursor cursor = db.query(DatabaseHelper.GROUPS_TABLE_NAME, this.allColumns, DatabaseHelper.GROUPS_TABLE_FIELD_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Group newComputer = this.cursorToGroup(cursor);
        cursor.close();
        this.dbHelper.closeDatabase();
        return newComputer;
    }

    private Group cursorToGroup(Cursor cursor) {
        Group group = new Group();
        group.setId(cursor.getLong(0));
        group.setName(cursor.getString(1));
        return group;
    }

    public void deleteGroup(Group group) {
        long id = group.getId();
        SQLiteDatabase db = this.dbHelper.openDatabase();
        db.delete(DatabaseHelper.GROUPS_TABLE_NAME, DatabaseHelper.GROUPS_TABLE_FIELD_ID + " = " + id, null);
        this.dbHelper.closeDatabase();
    }

    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<Group>();
        SQLiteDatabase db = this.dbHelper.openDatabase();
        Cursor cursor = db.query(DatabaseHelper.GROUPS_TABLE_NAME, this.allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Group group = this.populateChildren(this.cursorToGroup(cursor));
            groups.add(group);
            cursor.moveToNext();
        }
        cursor.close();
        this.dbHelper.closeDatabase();
        return groups;
    }

    public List<Group> getGroups(long[] ids) {
        List<Group> groups = new ArrayList<Group>();
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (int i = 0; i < ids.length; i++) {
            sb.append(ids[i]);
            if (i != (ids.length - 1)) {
                sb.append(',');
            }
        }
        sb.append(')');
        SQLiteDatabase db = this.dbHelper.openDatabase();
        Cursor cursor = db.query(DatabaseHelper.GROUPS_TABLE_NAME, this.allColumns, DatabaseHelper.GROUPS_TABLE_FIELD_ID + " IN " + sb.toString(), null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Group group = this.populateChildren(this.cursorToGroup(cursor));
            groups.add(group);
            cursor.moveToNext();
        }
        cursor.close();
        this.dbHelper.closeDatabase();
        return groups;
    }

    private Group populateChildren(Group group) {
        List<Long> ids = new ArrayList<Long>();
        SQLiteDatabase db = this.dbHelper.openDatabase();
        Cursor cursor = db.query(DatabaseHelper.LINK_TARGET_GROUP_TABLE_NAME, this.allLinkColumns, DatabaseHelper.LINK_TARGET_GROUP_FIELD_GROUP + " = " + group.getId(), null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ids.add((long) cursor.getInt(1));
            cursor.moveToNext();
        }
        cursor.close();
        this.dbHelper.closeDatabase();

        long[] idsP = new long[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            idsP[i] = ids.get(i);
        }

        group.getChildren().clear();
        group.getChildren().addAll(this.computerDataSource.getComputers(idsP));
        return group;
    }

    public void updateGroup(Group group) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.GROUPS_TABLE_FIELD_NAME, group.getName());
        SQLiteDatabase db = this.dbHelper.openDatabase();
        db.update(DatabaseHelper.GROUPS_TABLE_NAME, values, DatabaseHelper.GROUPS_TABLE_FIELD_ID + " = " + group.getId(), null);

        db.delete(DatabaseHelper.LINK_TARGET_GROUP_TABLE_NAME, DatabaseHelper.LINK_TARGET_GROUP_FIELD_GROUP + " = " + group.getId(), null);

        for (Computer computer : group.getChildren()) {
            ContentValues linkValues = new ContentValues();
            linkValues.put(DatabaseHelper.LINK_TARGET_GROUP_FIELD_TARGET, computer.getId());
            linkValues.put(DatabaseHelper.LINK_TARGET_GROUP_FIELD_GROUP, group.getId());
            db.insert(DatabaseHelper.LINK_TARGET_GROUP_TABLE_NAME, null, linkValues);
        }
        this.dbHelper.closeDatabase();
    }
}
