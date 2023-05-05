package com.example.kursovou_proekt.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userstore.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    public static final String TABLE_USERS = "users"; // название таблицы пользователей
    public static final String TABLE_GAMES = "games"; // название таблицы игр
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_CATEGORY = "category";



    // названия столбцов users
    public static final String USER_ID = "id";
    public static final String USER_UNIQUE_ID = "unique_id";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String USER_ENCRYPTED_PASSWORD = "encrypted_password";
    public static final String USER_SALT = "salt";
    public static final String USER_CREATED_AT = "created_at";
    public static final String USER_UPDATED_AT = "updated_at";


    // названия столбцов games
    public static final String ID_GAME = "id_game";
    public static final String GAME_NAME = "game_name";
    public static final String GAME_PRICE = "game_price";
    public static final String GAME_DESCRIPTION = "description";
    public static final String GAME_PUBLISHER = "game_publisher";
    public static final String GAME_PUBLISHED = "game_published";
    public static final String GAME_CATEGORY = "game_category";


    // названия столбцов таблицы "orders"
    public static final String ORDER_ID = "id_order";
    public static final String ORDER_USER_ID = "id_user";
    public static final String ORDER_GAME_ID = "id_game";
    public static final String ORDER_GAME_NAME = "game_name";
    public static final String ORDER_DATE = "order_date";


    // названия столбцов таблицы "category"
    public static final String CATEGORY_ID = "id_order";
    public static final String CATEGORY_TITLE = "title";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " ("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_UNIQUE_ID + " VARCHAR(100) NOT NULL, "
                + USER_NAME + " VARCHAR(100), "
                + USER_EMAIL + " VARCHAR(50), "
                + USER_ENCRYPTED_PASSWORD + " VARCHAR(128), "
                + USER_SALT + " VARCHAR(16), "
                + USER_CREATED_AT + " DATETIME, "
                + USER_UPDATED_AT + " DATETIME)";
        db.execSQL(createTable);

        String createTableGames = "CREATE TABLE " + TABLE_GAMES + " ("
                + ID_GAME + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GAME_NAME + " VARCHAR(45) NOT NULL, "
                + GAME_PRICE + " VARCHAR(45), "
                + GAME_DESCRIPTION + " VARCHAR(300), "
                + GAME_PUBLISHER + " VARCHAR(45), "
                + GAME_PUBLISHED + " DATETIME, "
                + GAME_CATEGORY + " VARCHAR(45),"
                + "FOREIGN KEY(" + GAME_CATEGORY + ") REFERENCES " + TABLE_CATEGORY + "(" + CATEGORY_TITLE + "))";
        db.execSQL(createTableGames);

        // создаем таблицу "orders"
        String createOrderTable = "CREATE TABLE " + TABLE_ORDERS + " ("
                + ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ORDER_USER_ID + " INT NOT NULL, "
                + ORDER_GAME_NAME + " VARCHAR(45), "
                + "FOREIGN KEY(" + ORDER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + "), "
                + "FOREIGN KEY(" + ORDER_GAME_NAME + ") REFERENCES " + TABLE_GAMES + "(" + GAME_NAME + "))";
        db.execSQL(createOrderTable);


        // создаем таблицу "category"
        String createCategoryTable = "CREATE TABLE " + TABLE_CATEGORY + " ("
                + CATEGORY_ID + " VARCHAR(45) PRIMARY KEY, "
                + CATEGORY_TITLE + " VARCHAR(45) NOT NULL) ";
        db.execSQL(createCategoryTable);







    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public int getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {USER_ID};
        String selection = USER_EMAIL + "=?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int userId = -1;
        cursor.close();
        return userId;
    }

    public static SQLiteDatabase getDatabase(Context context) {
        return new DatabaseHelper(context).getWritableDatabase();
    }

}