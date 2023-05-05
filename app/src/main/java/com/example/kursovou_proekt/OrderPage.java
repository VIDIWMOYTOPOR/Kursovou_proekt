package com.example.kursovou_proekt;

import static com.example.kursovou_proekt.database.DatabaseHelper.ORDER_GAME_NAME;
import static com.example.kursovou_proekt.database.DatabaseHelper.ORDER_USER_ID;
import static com.example.kursovou_proekt.database.DatabaseHelper.TABLE_ORDERS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        final Context context = OrderPage.this;

//        String email = getIntent().getStringExtra("email");
//        Intent intent = new Intent(OrderPage.this, MainActivity.class);
//        intent.putExtra("email", email); // Добавляем email в Intent
        TextView textView3 = findViewById(R.id.textView3);


        //ListView order_list = findViewById(R.id.order_list);

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        Integer userId = sharedPreferences.getInt("userId", 0);
        textView3.setText(Integer.toString(userId));



        List<String> gameTitle = new ArrayList<>();
        for(Game c: MainActivity.fullGamesList){
            if(Order.items_id.contains(c.getId()))
                gameTitle.add(c.getTitle());

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
                String[] checkColumns = {ORDER_USER_ID, ORDER_GAME_NAME};
                String checkSelection = ORDER_USER_ID + "=? AND " + ORDER_GAME_NAME + "=?";
                String[] checkSelectionArgs = {userId.toString(), gameName};
                Cursor checkCursor = db.query(TABLE_ORDERS, checkColumns, checkSelection, checkSelectionArgs, null, null, null);

                // Если Cursor пустой, значит, заказ еще не существует
                if (checkCursor.getCount() == 0) {
                    // Вставляем данные в таблицу "TABLE_ORDERS"
                    ContentValues cv = new ContentValues();
                    cv.put(ORDER_USER_ID, userId);
                    cv.put(ORDER_GAME_NAME, gameName);
                    db.insert(TABLE_ORDERS, null, cv);
                }

                checkCursor.close();
                // Закрываем соединение с базой данных
                db.close();



                /////////////////////////////////


            }

        }



        ListView order_list = findViewById(R.id.order_list);

            // Определяем слушатель нажатия на элемент списка
            order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Получаем название игры по ее позиции в списке
                String gameName = gameTitle.get(position);


// Удаляем игру из списка, если она содержится во множестве
                if (Order.items_id.contains(gameName)) {
                    Order.items_id.remove(gameName);


                }
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                String deleteQuery = "DELETE FROM " + TABLE_ORDERS + " WHERE id_user = '" + userId.toString() + "' AND game_name = '" + gameName + "'";
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL(deleteQuery);
                db.close();


                gameTitle.remove(position);

// Обновляем адаптер списка
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, gameTitle);
                order_list.setAdapter(adapter);
            }
        });















        order_list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gameTitle));





    }

    public void openMainPage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }




    public void SendToServer(View view){

    }


}