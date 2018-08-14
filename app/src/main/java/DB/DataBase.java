package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import models.Credential;

public class DataBase {

    private SQLiteDatabase db;

    public DataBase(Context context) {
        DBCore core = new DBCore(context);
        db = core.getWritableDatabase();
    }

    public void insertCredentials(Credential credential){
        ContentValues values = new ContentValues();
        values.put("username", credential.getUsername());
        values.put("password", credential.getPassword());
        values.put("access_token", credential.getAccess_token());
        values.put("expires_in", credential.getExpires_in());

        db.insert("credentials", null, values);
    }

    public void updateCredentials(Credential credential){
        ContentValues values = new ContentValues();
        values.put("username", credential.getUsername());
        values.put("password", credential.getPassword());
        values.put("access_token", credential.getAccess_token());
        values.put("expires_in", credential.getExpires_in());

        db.update("credentials", values, "_id = ?", new String[]{""+ credential.get_id()});
    }

    public void deleteCredentials(Credential credential){
        db.delete("credentials", "_id = " + credential.get_id(), null);
    }

    public Credential getCredentials(){
        Credential credential = new Credential();
        String[] columns = new String[]{"_id", "username", "password", "access_token", "expires_in"};
        Cursor cursor = db.query("credentials", columns, null, null, null,null,null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            credential.set_id((int) cursor.getLong(0));
            credential.setUsername(cursor.getString(1));
            credential.setPassword(cursor.getString(2));
            credential.setAccess_token(cursor.getString(3));
            credential.setExpires_in(cursor.getString(4));
        }
        return credential;
    }

}
