//package com.example.sportease;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.util.Log;
//import android.widget.ImageView;
//
//import java.io.ByteArrayOutputStream;
//
//public class DbHelper extends SQLiteOpenHelper {
//
//    public static final String DATABASE_NAME = "UserDB.db";
//    public static final String TABLE_USER = "users";
//    public static final String TABLE_CLUB_OWNER = "club_owners";
//    public static final String TABLE_COACH = "coaches";
//
//    // User table columns
//    public static final String COL_USER_ID = "ID";
//    public static final String COL_USER_USERNAME = "USERNAME";
//    public static final String COL_USER_LASTNAME = "LASTNAME";
//    public static final String COL_USER_EMAIL = "EMAIL";
//    public static final String COL_USER_PASSWORD = "PASSWORD";
//
//    // Club Owner table columns
//    public static final String COL_CLUB_OWNER_ID = "ID";
//    public static final String COL_CLUB_OWNER_EMAIL = "EMAIL";
//    public static final String COL_CLUB_OWNER_PASSWORD = "PASSWORD";
//    public static final String COL_CLUB_OWNER_ADDRESS = "ADDRESS";
//    public static final String COL_CLUB_OWNER_CLUB_NAME = "CLUB_NAME";
//    public static final String COL_CLUB_OWNER_OPEN_TIME = "OPEN_TIME";
//    public static final String COL_CLUB_OWNER_CLOSE_TIME = "CLOSE_TIME";
//    public static final String COL_CLUB_OWNER_IMAGE = "IMAGE";
//
//    // Coach table columns
//    public static final String COL_COACH_ID = "ID";
//    public static final String COL_COACH_FULL_NAME = "FULL_NAME";
//    public static final String COL_COACH_EMAIL = "EMAIL";
//    public static final String COL_COACH_PHONE_NUMBER = "PHONE_NUMBER";
//    public static final String COL_COACH_PASSWORD = "PASSWORD";
//    public static final String COL_COACH_SPECIALIZATION = "SPECIALIZATION";
//    public static final String COL_COACH_EXPERIENCE = "EXPERIENCE";
//    public static final String COL_COACH_CERTIFICATIONS = "CERTIFICATIONS";
//    public static final String COL_COACH_BIO = "BIO";
//    public static final String COL_COACH_AVAILABILITY = "AVAILABILITY";
//
//    public DbHelper(Context context) {
//        super(context, DATABASE_NAME, null, 3);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
//                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL_USER_USERNAME + " TEXT, " +
//                COL_USER_LASTNAME + " TEXT, " +
//                COL_USER_EMAIL + " TEXT, " +
//                COL_USER_PASSWORD + " TEXT)");
//
//        db.execSQL("CREATE TABLE " + TABLE_CLUB_OWNER + " (" +
//                COL_CLUB_OWNER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL_CLUB_OWNER_EMAIL + " TEXT, " +
//                COL_CLUB_OWNER_PASSWORD + " TEXT, " +
//                COL_CLUB_OWNER_ADDRESS + " TEXT, " +
//                COL_CLUB_OWNER_CLUB_NAME + " TEXT, " +
//                COL_CLUB_OWNER_OPEN_TIME + " TEXT, " +
//                COL_CLUB_OWNER_CLOSE_TIME + " TEXT, " +
//                COL_CLUB_OWNER_IMAGE + " BLOB)");
//
//        db.execSQL("CREATE TABLE " + TABLE_COACH + " (" +
//                COL_COACH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COL_COACH_FULL_NAME + " TEXT, " +
//                COL_COACH_EMAIL + " TEXT, " +
//                COL_COACH_PHONE_NUMBER + " TEXT, " +
//                COL_COACH_PASSWORD + " TEXT, " +
//                COL_COACH_SPECIALIZATION + " TEXT, " +
//                COL_COACH_EXPERIENCE + " TEXT, " +
//                COL_COACH_CERTIFICATIONS + " TEXT, " +
//                COL_COACH_BIO + " TEXT, " +
//                COL_COACH_AVAILABILITY + " TEXT)");
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion < 2) {
//            db.execSQL("ALTER TABLE " + TABLE_CLUB_OWNER + " ADD COLUMN " + COL_CLUB_OWNER_IMAGE + " BLOB");
//        }
//        if (oldVersion < 3) {
//            // No need to alter TABLE_USER and TABLE_CLUB_OWNER in version 3, only create TABLE_COACH
//            db.execSQL("CREATE TABLE " + TABLE_COACH + " (" +
//                    COL_COACH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    COL_COACH_FULL_NAME + " TEXT, " +
//                    COL_COACH_EMAIL + " TEXT, " +
//                    COL_COACH_PHONE_NUMBER + " TEXT, " +
//                    COL_COACH_PASSWORD + " TEXT, " +
//                    COL_COACH_SPECIALIZATION + " TEXT, " +
//                    COL_COACH_EXPERIENCE + " TEXT, " +
//                    COL_COACH_CERTIFICATIONS + " TEXT, " +
//                    COL_COACH_BIO + " TEXT, " +
//                    COL_COACH_AVAILABILITY + " TEXT)");
//        }
//    }
//
//    // Insert user method
//    public boolean insertUser(String username, String lastname, String email, String password) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_USER_USERNAME, username);
//        contentValues.put(COL_USER_LASTNAME, lastname);
//        contentValues.put(COL_USER_EMAIL, email);
//        contentValues.put(COL_USER_PASSWORD, password);
//        long result = db.insert(TABLE_USER, null, contentValues);
//        return result != -1;
//    }
//
//    // Insert club owner method
//    public boolean insertClubOwner(String email, String password, String address, String clubName, String openTime, String closeTime, byte[] image) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_CLUB_OWNER_EMAIL, email);
//        contentValues.put(COL_CLUB_OWNER_PASSWORD, password);
//        contentValues.put(COL_CLUB_OWNER_ADDRESS, address);
//        contentValues.put(COL_CLUB_OWNER_CLUB_NAME, clubName);
//        contentValues.put(COL_CLUB_OWNER_OPEN_TIME, openTime);
//        contentValues.put(COL_CLUB_OWNER_CLOSE_TIME, closeTime);
//        contentValues.put(COL_CLUB_OWNER_IMAGE, image);
//
//        long result = db.insert(TABLE_CLUB_OWNER, null, contentValues);
//        if (result == -1) {
//            Log.e("DbHelper", "Failed to insert club owner data.");
//        }
//        return result != -1;
//    }
//
//    // Insert coach method
//    public boolean insertCoach(String fullName, String email, String phoneNumber, String password, String specialization, String experience, String certifications, String bio, String availability) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_COACH_FULL_NAME, fullName);
//        contentValues.put(COL_COACH_EMAIL, email);
//        contentValues.put(COL_COACH_PHONE_NUMBER, phoneNumber);
//        contentValues.put(COL_COACH_PASSWORD, password);
//        contentValues.put(COL_COACH_SPECIALIZATION, specialization);
//        contentValues.put(COL_COACH_EXPERIENCE, experience);
//        contentValues.put(COL_COACH_CERTIFICATIONS, certifications);
//        contentValues.put(COL_COACH_BIO, bio);
//        contentValues.put(COL_COACH_AVAILABILITY, availability);
//        long result = db.insert(TABLE_COACH, null, contentValues);
//        return result != -1;
//    }
//
//    // Check app user method
//    public boolean checkAppUser(String email, String password) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE EMAIL=? AND PASSWORD=?", new String[]{email, password});
//        boolean result = cursor.getCount() > 0;
//        cursor.close();
//        return result;
//    }
//
//    // Check club owner method
//    public boolean checkClubOwner(String email, String password) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CLUB_OWNER + " WHERE EMAIL=? AND PASSWORD=?", new String[]{email, password});
//        boolean result = cursor.getCount() > 0;
//        cursor.close();
//        return result;
//    }
//
//    // Check coach method
//    public boolean checkCoach(String email, String password) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COACH + " WHERE EMAIL=? AND PASSWORD=?", new String[]{email, password});
//        boolean result = cursor.getCount() > 0;
//        cursor.close();
//        return result;
//    }
//
//    public byte[] imageViewToByte(ImageView imageView) {
//        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        Log.d("DbHelper", "Image Byte Array Size: " + byteArray.length);
//        return byteArray;
//    }
//
//}
