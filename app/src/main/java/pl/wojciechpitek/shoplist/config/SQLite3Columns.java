package pl.wojciechpitek.shoplist.config;

import android.provider.BaseColumns;

public final class SQLite3Columns implements BaseColumns {

    public SQLite3Columns() { }
    public static final String TABLE_NAME_ITEMS = "list";
    public static final String TABLE_NAME_AUTOCOMPLETE = "helper";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_STATE = "state";
    public static final String COLUMN_NAME_IMPORTANT = "important";
}
