package com.example.removedragon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static  final  String DATABASE_NAME="app.db";
    public static  final  String TABLE_NAME="app_table";
    public static  final  String COL_1="ID";
    public static  final  String COL_2="NAME";





    public DatabaseHelper(@Nullable Context context)
    {
            super(context, DATABASE_NAME, null, 1);
    }
//**************************************************************
    @Override
    public void onCreate(SQLiteDatabase db)
    {
    db.execSQL("create table "+TABLE_NAME+"(ID INTEGER primary key autoincrement,NAME TEXT)");// this will execute our SQL statement & create a table.
    }
//***************************************************************
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NAME); // it will drop table if already existing .
        onCreate(db);// this  is again calling onCreate method ,to create a new table ,after deleting the previously existing table.
    }
    //***********************************************************
    public boolean insertData(String name) // this method is used to insert data in table.
    {
        SQLiteDatabase db=this.getWritableDatabase();// creating SQLite database Object, for getting writable database.
        ContentValues contentValues=new ContentValues();// creating an object of class "ContentValues" and calling "put" methods on that object to store the value in the desired coloumn,like key-value parameter.
        contentValues.put(COL_2,name);

        long result= db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }
    //*************************************************************
    public Cursor getAllData() //This interface provides random read-write access to the result set returned  by a database query.
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery(" select * from "+TABLE_NAME,null);// this will show all data in the table.
        return  res;
    }
    //**************************************************************

    public  int updateData(String name,String surname,String marks,String id)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);


        return  db.update(TABLE_NAME,contentValues," id = ? ",new String[]{id});

    /* ?s in the where clause, which will be replaced by the values from whereArgs. The values will be bound as Strings.*/
    }
 //****************************************************************************************

     public int delData(String id)
     {
         SQLiteDatabase db=this.getWritableDatabase();
         return db.delete(TABLE_NAME," id=? ",new String[] {id});
     }
//***********************************************************************

    public void cleraData()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }
}
