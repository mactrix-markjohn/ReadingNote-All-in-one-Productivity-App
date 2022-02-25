package com.mactrix.www.readingnotepro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mactrix on 4/15/2018.
 */

public class StudyDatabase extends SQLiteOpenHelper {

    Cursor cursor;

    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String NOTE = "note";
    public static final String MEDIAFILE = "mediafile";

    public static final String DATABASENAME = "pronote.db";

    public static final String TABLENAME = "pronotetable";

    String[] columns = new String[]{ID,TITLE,NOTE,MEDIAFILE};

    public static final int VERSION = 1;




    public StudyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASENAME, factory,VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+TABLENAME+"("+ID+" integer primary key, "+TITLE+" text, "+NOTE+" text, "+MEDIAFILE+" text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE "+TABLENAME+" ADD COLUMN ALARM INTEGER");
        onCreate(db);

    }

    public long addNote(String title, String note, String mediafile){

        SQLiteDatabase dbase = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(TITLE,title);
        value.put(NOTE,note);
        value.put(MEDIAFILE,mediafile);
        return dbase.insert(TABLENAME,null,value);
    }

    public boolean deleteNote(long id){
        SQLiteDatabase dbase = this.getWritableDatabase();
        return dbase.delete(TABLENAME,ID+"=?",new String[]{Long.toString(id)})>0;
    }

    public Cursor getAllNote(){
        SQLiteDatabase dbase = this.getWritableDatabase();
        return dbase.query(TABLENAME,columns,null,null,null,null,null);
    }

    public Cursor getNote(Long id){

        Cursor z=null;
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            z = db.rawQuery("select * from " + TABLENAME + " where _id=" + id
                    + "", null);


           /* cursor = dbase.query(true,TABLENAME,column,ID+"="+id,null,null,null,null,null);
            if(cursor!=null){cursor.moveToFirst();}*/

        }catch (SQLException e){ e.printStackTrace();}
        return z;
    }
    public boolean updateNote(long id, String title,String note, String mediafile){
        SQLiteDatabase dbase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE,title);
        values.put(NOTE,note);
        values.put(MEDIAFILE,mediafile);
        return dbase.update(TABLENAME,values,ID+"="+id,null)>0;

    }
}
