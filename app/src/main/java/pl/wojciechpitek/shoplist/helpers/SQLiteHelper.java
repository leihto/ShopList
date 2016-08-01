package pl.wojciechpitek.shoplist.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import pl.wojciechpitek.shoplist.config.SQLite3Columns;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES_ITEMS =
            "CREATE TABLE " + SQLite3Columns.TABLE_NAME_ITEMS + " (" +
                    SQLite3Columns._ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    SQLite3Columns.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    SQLite3Columns.COLUMN_NAME_STATE + INTEGER_TYPE + COMMA_SEP +
                    SQLite3Columns.COLUMN_NAME_IMPORTANT + INTEGER_TYPE +
            " )";
    private static final String SQL_CREATE_ENTRIES_AUTOCOMPLETE =
            "CREATE TABLE " + SQLite3Columns.TABLE_NAME_AUTOCOMPLETE + " (" +
                    SQLite3Columns._ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    SQLite3Columns.COLUMN_NAME_NAME + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES_ITEMS =
            "DROP TABLE IF EXISTS " + SQLite3Columns.TABLE_NAME_ITEMS;

    private static final String SQL_DELETE_ENTRIES_AUTOCOMPLETE =
            "DROP TABLE IF EXISTS " + SQLite3Columns.TABLE_NAME_AUTOCOMPLETE;

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "ShopList.db";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_ITEMS);
        db.execSQL(SQL_CREATE_ENTRIES_AUTOCOMPLETE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES_ITEMS);
        db.execSQL(SQL_DELETE_ENTRIES_AUTOCOMPLETE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
