package com.example.kursovou_proekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kursovou_proekt.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import Model.Game;
import Model.Order;

public class OrderPage extends AppCompatActivity {

     //String userId = getIntent().getStringExtra("userId");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        final Context context = OrderPage.this;

//        String email = getIntent().getStringExtra("email");
//        Intent intent = new Intent(OrderPage.this, MainActivity.class);
//        intent.putExtra("email", email); // Добавляем email в Intent
        TextView textView3 = findViewById(R.id.textView3);


        ListView order_list = findViewById(R.id.order_list);

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        Integer userId = sharedPreferences.getInt("userId", 0);
        textView3.setText(Integer.toString(userId));



        List<String> gameTitle = new ArrayList<>();
        for(Game c: MainActivity.fullGamesList){
            if(Order.items_id.contains(c.getId()))
                gameTitle.add(c.getTitle());


            /////////////////////////////////
//
//
//            // Получаем id игры
//            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
//            SQLiteDatabase db = dbHelper.getWritableDatabase();
//            String[] columns = {DatabaseHelper.ID_GAME};
//            String selection = DatabaseHelper.GAME_NAME + "=?";
//            String[] selectionArgs = {gameName};
//            Cursor cursor = db.query(DatabaseHelper.TABLE_GAMES, columns, selection, selectionArgs, null, null, null);
////        int gameId = -1;
////        if (cursor.moveToFirst()) {
////            gameId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_GAME));
////        }
//            cursor.close();
//
//            // Вставляем данные в таблицу "TABLE_ORDERS"
//            ContentValues cv = new ContentValues();
//            cv.put(DatabaseHelper.ORDER_USER_ID, userId);
//            cv.put(DatabaseHelper.ORDER_GAME_NAME, gameName);
//            db.insert(DatabaseHelper.TABLE_ORDERS, null, cv);
//
//            // Закрываем соединение с базой данных
//            db.close();



            /////////////////////////////////


        }



        if (gameTitle.isEmpty()) {
            // список пуст, выведите сообщение об ошибке
            Toast.makeText(this, "Список игр пуст", Toast.LENGTH_SHORT).show();
        } else {
            // список не пуст, обработайте его
            for (String gameName : gameTitle) {

                /////////////////////////////////


                // Получаем id игры
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String[] columns = {DatabaseHelper.ID_GAME};
                String selection = DatabaseHelper.GAME_NAME + "=?";
                String[] selectionArgs = {gameName};
                Cursor cursor = db.query(DatabaseHelper.TABLE_GAMES, columns, selection, selectionArgs, null, null, null);
//        int gameId = -1;
//        if (cursor.moveToFirst()) {
//            gameId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID_GAME));
//        }
                cursor.close();

                // Вставляем данные в таблицу "TABLE_ORDERS"
                // Определяем конфликтующую колонку
                // Проверяем, существует ли уже заказ в таблице "TABLE_ORDERS"
                String[] checkColumns = {DatabaseHelper.ORDER_USER_ID, DatabaseHelper.ORDER_GAME_NAME};
                String checkSelection = DatabaseHelper.ORDER_USER_ID + "=? AND " + DatabaseHelper.ORDER_GAME_NAME + "=?";
                String[] checkSelectionArgs = {userId.toString(), gameName};
                Cursor checkCursor = db.query(DatabaseHelper.TABLE_ORDERS, checkColumns, checkSelection, checkSelectionArgs, null, null, null);

                // Если Cursor пустой, значит, заказ еще не существует
                if (checkCursor.getCount() == 0) {
                    // Вставляем данные в таблицу "TABLE_ORDERS"
                    ContentValues cv = new ContentValues();
                    cv.put(DatabaseHelper.ORDER_USER_ID, userId);
                    cv.put(DatabaseHelper.ORDER_GAME_NAME, gameName);
                    db.insert(DatabaseHelper.TABLE_ORDERS, null, cv);
                }

                checkCursor.close();
                // Закрываем соединение с базой данных
                db.close();



                /////////////////////////////////


            }

        }



















        //order_list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Order.items_id.toArray()));
        order_list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gameTitle));

    }

    public void openMainPage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }




}