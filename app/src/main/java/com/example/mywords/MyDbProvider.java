package com.example.mywords;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MyDbProvider extends ContentProvider {
    private dbHelper mydbHelper;
    private static final UriMatcher myUriMatcher;
    static {
        myUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        myUriMatcher.addURI(MyDb.AUTHORITY,"words",MyDb.WORDS);
        myUriMatcher.addURI(MyDb.AUTHORITY,"words/#",MyDb.WORDS_ID);


    }

    @Override
    public boolean onCreate() {
        return false;
    }

    public int delete(Uri uri, String selection, String[] slelctionArgs){
        if(myUriMatcher.match(uri)!=MyDb.WORDS_ID){
            throw new IllegalArgumentException("wrong insert type:"+uri);
        }
        String id=uri.getPathSegments().get(1);
        if(selection==null)
            selection=MyDb.ID+"="+id;
        else
            selection=MyDb.ID+"="+id+" and "+selection;
        SQLiteDatabase db=mydbHelper.getWritableDatabase();
        int i=db.delete(dbHelper.TB_NAME,selection,slelctionArgs);
        if(i>0){
            Log.i("myDbDemo","数据更新成功");

        }
        else{
            Log.i("myDbDemo","数据更新失败");

        }
        return i;



    }
    public String getType(Uri uri){
        switch(myUriMatcher.match(uri)){
            case MyDb.WORDS:
                return MyDb.CONTENT_TYPE;
            case MyDb.WORDS_ID:
                return MyDb.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknow URI get type: "+uri);

        }

    }
    public Uri insert(Uri uri, ContentValues values){

        if(myUriMatcher.match(uri)!=MyDb.WORDS){
            throw  new IllegalArgumentException("wrong insert typr:"+uri);
        }
        if(values==null){
            throw new IllegalArgumentException("wrong data.");
        }
        SQLiteDatabase db=mydbHelper.getWritableDatabase();
        long rowId=db.insert(mydbHelper.TB_NAME
                ,null,null);
        if(rowId>0){
            Uri insertUri= ContentUris.withAppendedId(MyDb.CONTENT_URI,rowId);
            return insertUri;
        }
        return null;
    }

    public boolean onCreat(){
        mydbHelper=new dbHelper
                (getContext(),MyDb.TABLE_NAME,null,MyDb.DATABASE_VERSION);
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        switch (myUriMatcher.match(uri)){
            case MyDb.WORDS:
                break;
            case MyDb.WORDS_ID:
                Log.d("MyDbProvider","select id");
                String id=uri.getPathSegments().get(1);
                if (selection==null)
                    selection=MyDb.ID+"="+id+ " and "+selection;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI type: "+uri);
        }
        if (sortOrder==null)
            sortOrder="_id ASC";
        SQLiteDatabase db=mydbHelper.getReadableDatabase();
        Cursor c=db.query(MyDb.TABLE_NAME,projection,selection,selectionArgs,
                null,
                null,sortOrder);
        Log.d("MyDBProvider",""+c.getCount());
        return c;


    }

    public int update(Uri uri,ContentValues values,String selection,
                      String[] selectionArgs){
        if(myUriMatcher.match(uri)!=MyDb.WORDS_ID){
            throw new IllegalArgumentException("wrong insert type: "+uri);

        }
        if(values==null){
            throw new IllegalArgumentException("wrong data.");
        }
        String id=uri.getPathSegments().get(1);
        if(selection==null)
            selection=MyDb.ID+"="+id;
        else
            selection=MyDb.ID+"="+id+" and "+selection;
        SQLiteDatabase db=mydbHelper.getWritableDatabase();
        int i=db.update(dbHelper.TB_NAME,values,selection,selectionArgs);
        if(i>0){
            Log.i("myDbDemo","数据更新成功");

        }
        else{
            Log.i("myDbDemo","数据更新失败");

        }
        return i;
    }


}