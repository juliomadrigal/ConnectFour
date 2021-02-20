package com.example.csci350program2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

public class ConnectFourData {
    public static final String MYAPP = "MYAPP";
    private SQLiteDatabase db;
    public static final String TABLE_ROW_ID = "_id";
    public static final String TABLE_ROW_NAME = "name";
    public static final String TABLE_ROW_WINS = "wins";
    public static final String TABLE_ROW_LOSSES = "losses";
    public static final String DB_NAME = "connectfourdata_db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_PLAYERS = "name_wins_losses";


    public ConnectFourData(Context context) {
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
    }
    public void Delete(String name) {
        String query = "DELETE FROM " + TABLE_PLAYERS + " WHERE " + TABLE_ROW_NAME +
                " = '" + name + "';";
        Log.i("MYAPP",   query);

        db.execSQL(query);
    }
    public void DeleteWins(int wins) { //what wins to delete
        String query = "DELETE FROM " + TABLE_PLAYERS + " WHERE " + TABLE_ROW_WINS +
                " = '" + wins + "';";
        Log.i("MYAPP",   query);

        db.execSQL(query);
    }
    public void DeleteLosses(int losses) { //what losses to delete
        String query = "DELETE FROM " + TABLE_PLAYERS + " WHERE " + TABLE_ROW_LOSSES +
                " = '" + losses + "';";
        Log.i("MYAPP",   query);

        db.execSQL(query);
    }
    public void Insert(String name, int wins, int losses) {
        String query = "INSERT INTO " + TABLE_PLAYERS + " (" +
                TABLE_ROW_NAME + ", " + TABLE_ROW_WINS + ", " + TABLE_ROW_LOSSES + ") " +
                "VALUES (" + "'" + name +  "'" + ", " +
                wins +  ", " + losses + ");";

        Log.i("MYAPP",   query);
        db.execSQL(query);
    }

    public Cursor SelectAll()
    {
        Cursor c = db.rawQuery("SELECT *" + " from " + TABLE_PLAYERS, null);
        return c;
    }
    public Cursor SearchName(String name)
    {
        String query = "SELECT " + TABLE_ROW_ID + "," +
                TABLE_ROW_NAME + ", " + TABLE_ROW_WINS
                + ", " + TABLE_ROW_LOSSES + " from " +
                TABLE_PLAYERS + " WHERE " +
                TABLE_ROW_NAME + " = '" + name + "';";

        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public Cursor SortWins()
    {
        String query = "SELECT " + TABLE_ROW_NAME + ", " + TABLE_ROW_WINS + ", " + TABLE_ROW_LOSSES + " FROM " + TABLE_PLAYERS +
                " ORDER BY " +  TABLE_ROW_WINS + " DESC;";

        Log.i(MYAPP,query);
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public Cursor SearchWins(int wins)
    {
        String query = "SELECT " + TABLE_ROW_ID + ", " +
                TABLE_ROW_NAME + ", " + TABLE_ROW_WINS
                + " from " +
                TABLE_PLAYERS + " WHERE " +
                TABLE_ROW_WINS + " = '" + wins + "';";

        Log.i(MYAPP,query);
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public Cursor SearchLosses(int losses)
    {
        String query = "SELECT " + TABLE_ROW_ID + ", " +
                TABLE_ROW_NAME + ", " + TABLE_ROW_LOSSES
                + " from " +
                TABLE_PLAYERS + " WHERE " +
                TABLE_ROW_WINS + " = '" + losses + "';";

        Log.i(MYAPP,query);
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    public void UpdateWins(String player, int win)
    {
        String query = "UPDATE " + TABLE_PLAYERS + " SET " +
                TABLE_ROW_WINS + " = '" + win + "' WHERE " +
                TABLE_ROW_NAME + " = '" + player + "';";

        Log.i(MYAPP,query);
        db.execSQL(query);
    }

    public void UpdateLosses(String player, int lose)
    {
        String query = "UPDATE " + TABLE_PLAYERS + " SET " +
                TABLE_ROW_LOSSES + " = '" + lose + "' WHERE " +
                TABLE_ROW_NAME + " = '" + player + "';";

        Log.i(MYAPP,query);
        db.execSQL(query);
    }

    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String newTableQueryString = "create table "
                    + TABLE_PLAYERS + " ("
                    + TABLE_ROW_ID
                    + " integer primary key autoincrement not null,"
                    + TABLE_ROW_NAME
                    + " text not null,"
                    + TABLE_ROW_WINS
                    + " integer not null,"
                    + TABLE_ROW_LOSSES
                    + " integer not null);";
            db.execSQL(newTableQueryString);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
