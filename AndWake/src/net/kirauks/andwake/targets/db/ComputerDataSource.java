package net.kirauks.andwake.targets.db;

import java.util.ArrayList;
import java.util.List;

import net.kirauks.andwake.targets.Computer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ComputerDataSource {
    private final DatabaseHelper dbHelper;
    private final String[] allColumns =
        { DatabaseHelper.TARGETS_TABLE_FIELD_ID, DatabaseHelper.TARGETS_TABLE_FIELD_NAME, DatabaseHelper.TARGETS_TABLE_FIELD_ADDRESS, DatabaseHelper.TARGETS_TABLE_FIELD_MAC, DatabaseHelper.TARGETS_TABLE_FIELD_PORT };

    public ComputerDataSource(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public Computer createComputer(Computer computer) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.TARGETS_TABLE_FIELD_NAME, computer.getName());
        values.put(DatabaseHelper.TARGETS_TABLE_FIELD_ADDRESS, computer.getAddress());
        values.put(DatabaseHelper.TARGETS_TABLE_FIELD_MAC, computer.getMac());
        values.put(DatabaseHelper.TARGETS_TABLE_FIELD_PORT, String.valueOf(computer.getPort()));
        SQLiteDatabase db = this.dbHelper.openDatabase();
        long insertId = db.insert(DatabaseHelper.TARGETS_TABLE_NAME, null, values);
        Cursor cursor = db.query(DatabaseHelper.TARGETS_TABLE_NAME, this.allColumns, DatabaseHelper.TARGETS_TABLE_FIELD_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Computer newComputer = this.cursorToComputer(cursor);
        cursor.close();
        this.dbHelper.closeDatabase();
        return newComputer;
    }

    private Computer cursorToComputer(Cursor cursor) {
        Computer computer = new Computer();
        computer.setId(cursor.getLong(0));
        computer.setName(cursor.getString(1));
        computer.setAddress(cursor.getString(2));
        computer.setMac(cursor.getString(3));
        computer.setPort(cursor.getInt(4));
        return computer;
    }

    public void deleteComputer(Computer computer) {
        SQLiteDatabase db = this.dbHelper.openDatabase();
        db.delete(DatabaseHelper.TARGETS_TABLE_NAME, DatabaseHelper.TARGETS_TABLE_FIELD_ID + " = " + computer.getId(), null);
        this.dbHelper.closeDatabase();
    }

    public List<Computer> getAllComputers() {
        List<Computer> computers = new ArrayList<Computer>();
        SQLiteDatabase db = this.dbHelper.openDatabase();
        Cursor cursor = db.query(DatabaseHelper.TARGETS_TABLE_NAME, this.allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Computer computer = this.cursorToComputer(cursor);
            computers.add(computer);
            cursor.moveToNext();
        }
        cursor.close();
        this.dbHelper.closeDatabase();
        return computers;
    }

    public List<Computer> getComputers(long[] ids) {
        List<Computer> computers = new ArrayList<Computer>();
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
        Cursor cursor = db.query(DatabaseHelper.TARGETS_TABLE_NAME, this.allColumns, DatabaseHelper.TARGETS_TABLE_FIELD_ID + " IN " + sb.toString(), null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Computer computer = this.cursorToComputer(cursor);
            computers.add(computer);
            cursor.moveToNext();
        }
        cursor.close();
        this.dbHelper.closeDatabase();
        return computers;
    }

    public void updateComputer(Computer computer) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.TARGETS_TABLE_FIELD_NAME, computer.getName());
        values.put(DatabaseHelper.TARGETS_TABLE_FIELD_ADDRESS, computer.getAddress());
        values.put(DatabaseHelper.TARGETS_TABLE_FIELD_MAC, computer.getMac());
        values.put(DatabaseHelper.TARGETS_TABLE_FIELD_PORT, String.valueOf(computer.getPort()));

        SQLiteDatabase db = this.dbHelper.openDatabase();
        db.update(DatabaseHelper.TARGETS_TABLE_NAME, values, DatabaseHelper.TARGETS_TABLE_FIELD_ID + " = " + computer.getId(), null);
        this.dbHelper.closeDatabase();
    }
}
