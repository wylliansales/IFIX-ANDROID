package DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCore extends SQLiteOpenHelper {

    private static final String NAME_DB = "ifix";
    private static final int VERSAO_DB = 6;

    public DBCore(Context ctx) {
        super(ctx, NAME_DB, null, VERSAO_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table credentials(_id integer primary key autoincrement, username text not null, password text not null, access_token text not null, expires_in text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table credentials");
        onCreate(sqLiteDatabase);
    }
}
