package com.project.udayanga.keepmerelax;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Udayanga on 10/22/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "KeepMeRelax";

    // Contacts table name
    private static final String TABLE_USER = "user";

    // Contacts Table Columns names
    //private static final String KEY_ID = "id";

    private static final String KEY_NAME = "name", KEY_PASS = "password",KEY_DOB="date_of_birth",KEY_GENDER="gender";
    private static final String KEY_LOW="low_rate",KEY_PEAK="peak_value";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_NAME + " TEXT," + KEY_PASS + " TEXT,"
                + KEY_DOB + " TEXT" +KEY_GENDER+"TEXT"+KEY_LOW+"INTEGER"+KEY_PEAK+"INTEGER"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }
    // Adding new contact
    public void addContact(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName()); // Contact Name
        values.put(KEY_PASS, user.getPassword()); // Contact Phone Number
        values.put(KEY_DOB,user.getDob());//
        values.put(KEY_GENDER,user.getGender());
        values.put(KEY_LOW,user.getLow());
        values.put(KEY_PEAK,user.getPeak());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
//    public User getContact(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_USER, new String[] { KEY_NAME,
//                        KEY_PASS, KEY_DOB,KEY_GENDER,KEY_LOW,KEY_PEAK }, KEY_NAME + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        User user= new User(cursor.getString(0),cursor.getString(1),cursor.getString(3),cursor.getString(4),Integer.parseInt(cursor.getString(5)),Integer.parseInt(cursor.getString(6)));
////        User contact = new User(Integer.parseInt(cursor.getString(0)),
////                cursor.getString(1), cursor.getString(2));
//        // return contact
//        return user;
//    }
//
//    // Getting All Contacts
//    public List<User> getAllContacts() {
//        List<User> contactList = new ArrayList<User>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_USER;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                User user = new User();
//                user.setName(cursor.getString(0));
//                user.setPassword(cursor.getString(1));
//                user.setDob(cursor.getString(2));
//                user.setGender(cursor.getString(3));
//                user.setLow(Integer.parseInt(cursor.getString(4)));
//                user.setPeak(Integer.parseInt(cursor.getString(5)));
//
//                // Adding contact to list
//                contactList.add(user);
//            } while (cursor.moveToNext());
//        }
//
//        // return contact list
//        return contactList;
//    }
//
//    // Getting contacts Count
//    public int getContactsCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_USER;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
//    // Updating single contact
//    public int updateContact(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_NAME, user.getName());
//        values.put(KEY_PASS, user.getPassword());
//        values.put(KEY_DOB, user.getDob());
//        values.put(KEY_GENDER, user.getGender());
//        values.put(KEY_LOW, user.getLow());
//        values.put(KEY_PEAK, user.getPeak());
//
//
//        // updating row
//        return db.update(TABLE_USER, values, KEY_NAME + " = ?",
//                new String[] { user.getName() });
//    }
//
//    // Deleting single contact
//    public void deleteContact(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_USER, KEY_NAME + " = ?",
//                new String[] { user.getName()});
//        db.close();
//    }
}
