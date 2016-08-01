package pl.wojciechpitek.shoplist.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import pl.wojciechpitek.shoplist.config.SQLite3Columns;
import pl.wojciechpitek.shoplist.dtos.ListElementDto;

import java.util.ArrayList;
import java.util.List;

public class DBHelper {

    private SQLiteHelper sqlLiteHelper;

    public DBHelper(Context context) {
        sqlLiteHelper = new SQLiteHelper(context);
    }

    public Long insertRecord(ListElementDto listElementDto) {
        SQLiteDatabase sqlLiteDatabase = sqlLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLite3Columns.COLUMN_NAME_NAME, listElementDto.getName());
        values.put(SQLite3Columns.COLUMN_NAME_STATE, listElementDto.getState().getType());
        values.put(SQLite3Columns.COLUMN_NAME_IMPORTANT, listElementDto.getImportant() ? 1 : 0);
        Long id = sqlLiteDatabase.insert(SQLite3Columns.TABLE_NAME_ITEMS, null, values);
        listElementDto.setId(id);

        String[] projection = { SQLite3Columns.COLUMN_NAME_NAME };
        Cursor cursor = sqlLiteDatabase.query(
                SQLite3Columns.TABLE_NAME_AUTOCOMPLETE,
                projection,
                SQLite3Columns.COLUMN_NAME_NAME + " LIKE ?",
                new String[] { String.valueOf(listElementDto.getName().toLowerCase()) },
                null, null, null
        );

        if(cursor.getCount() == 0) {
            ContentValues values2 = new ContentValues();
            values2.put(SQLite3Columns.COLUMN_NAME_NAME, listElementDto.getName().toLowerCase());
            sqlLiteDatabase.insert(SQLite3Columns.TABLE_NAME_AUTOCOMPLETE, null, values2);
        }
        cursor.close();
        sqlLiteDatabase.close();
        return id;
    }

    public List<ListElementDto> fetchAllRecords() {
        SQLiteDatabase sqlLiteDatabase = sqlLiteHelper.getReadableDatabase();
        String[] projection = {
          SQLite3Columns._ID, SQLite3Columns.COLUMN_NAME_NAME, SQLite3Columns.COLUMN_NAME_STATE, SQLite3Columns.COLUMN_NAME_IMPORTANT
        };

        Cursor cursor = sqlLiteDatabase.query(
                SQLite3Columns.TABLE_NAME_ITEMS,
                projection,
                null, null, null, null,
                SQLite3Columns.COLUMN_NAME_NAME + " ASC"
        );
        cursor.moveToFirst();
        List<ListElementDto> listElementDtos = new ArrayList<>();
        try {
            do {
                listElementDtos.add(
                  new ListElementDto(
                          cursor.getLong(cursor.getColumnIndex(SQLite3Columns._ID)),
                          cursor.getString(cursor.getColumnIndex(SQLite3Columns.COLUMN_NAME_NAME)),
                          ListElementDto.getStateByInt(cursor.getInt(cursor.getColumnIndex(SQLite3Columns.COLUMN_NAME_STATE))),
                          ListElementDto.getIsImportant(cursor.getInt(cursor.getColumnIndex(SQLite3Columns.COLUMN_NAME_IMPORTANT)))
                  )
                );
            } while (cursor.moveToNext());
        } catch(CursorIndexOutOfBoundsException e) {
            //Nothing. Empty db
        } finally {
            cursor.close();
        }
        sqlLiteDatabase.close();
        return listElementDtos;
    }

    public Integer clearDb() {
        SQLiteDatabase sqLiteDatabase = sqlLiteHelper.getWritableDatabase();
        int v = sqLiteDatabase.delete(SQLite3Columns.TABLE_NAME_ITEMS, null, null);
        sqLiteDatabase.close();
        return v;
    }

    public Integer removeItem(Long id) {
        SQLiteDatabase sqLiteDatabase = sqlLiteHelper.getWritableDatabase();
        int v = sqLiteDatabase.delete(SQLite3Columns.TABLE_NAME_ITEMS, SQLite3Columns._ID + " = ?", new String[] { String.valueOf(id) });
        sqLiteDatabase.close();
        return v;
    }

    public Integer updateState(Long id, ListElementDto.StateTypes state) {
        SQLiteDatabase sqLiteDatabase = sqlLiteHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLite3Columns.COLUMN_NAME_STATE, state.getType());
        int v = sqLiteDatabase.update(SQLite3Columns.TABLE_NAME_ITEMS, contentValues, SQLite3Columns._ID + " = ?", new String[] { String.valueOf(id) });
        sqLiteDatabase.close();
        return v;
    }

    public List<String> getAutocompletePhrases() {
        SQLiteDatabase sqlLiteDatabase = sqlLiteHelper.getReadableDatabase();
        String[] projection = { SQLite3Columns.COLUMN_NAME_NAME };

        Cursor cursor = sqlLiteDatabase.query(
                SQLite3Columns.TABLE_NAME_AUTOCOMPLETE,
                projection,
                null, null, null, null,
                SQLite3Columns.COLUMN_NAME_NAME + " ASC"
        );
        cursor.moveToFirst();
        List<String> autocompleteList = new ArrayList<>();
        try {
            do {
                autocompleteList.add(cursor.getString(cursor.getColumnIndex(SQLite3Columns.COLUMN_NAME_NAME)));
            } while (cursor.moveToNext());
        } catch(CursorIndexOutOfBoundsException e) {
            //Nothing. Empty db
        }finally {
            cursor.close();
        }
        sqlLiteDatabase.close();
        return autocompleteList;
    }
}
