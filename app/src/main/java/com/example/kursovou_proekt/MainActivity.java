package com.example.kursovou_proekt;

import static android.content.ContentValues.TAG;

import static com.example.kursovou_proekt.database.DatabaseHelper.TABLE_USERS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Model.Categoryy;
import Model.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import retrofit2.http.Query;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.kursovou_proekt.Retrofit.INodeJS;
import com.example.kursovou_proekt.Retrofit.RetrofitClient;
import com.example.kursovou_proekt.database.DatabaseHelper;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Adapter.CategoryAdapter;
import Adapter.GameAdapter;
import Model.Category;
import Model.Game;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import io.reactivex.Observer;


public class MainActivity extends AppCompatActivity  {
    DatabaseHelper sqlHelper;
    SQLiteDatabase dbb;
    Cursor userCursor;

    RecyclerView categoryRecycler, gameRecycler;
    CategoryAdapter categoryAdapter;
    static GameAdapter gameAdapter;
    static List<Game> gameList = new ArrayList<>();
    static List<Game> fullGamesList = new ArrayList<>();

    final Context context = MainActivity.this;




    String email_main;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email_main = getIntent().getStringExtra("email");



        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");

        TextView textView4 = findViewById(R.id.textView4);
        textView4.setText(email);



        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        //userCursor = db.rawQuery("select id from " + DatabaseHelper.TABLE_USERS, null);
        //String email_main = getIntent().getStringExtra("email");


        String[] columns = {"id"};
        String selection = "email=?";
        String[] selectionArgs = {email};
        Cursor userCursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        if (userCursor.moveToFirst()) {
            int userId = userCursor.getInt(userCursor.getColumnIndex("id"));


            SharedPreferences sharedPreferences_forid = getSharedPreferences("myPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences_forid.edit();
            editor.putInt("userId", userId);
            editor.apply();



            // делайте что-то с userId
            //Intent intent = new Intent(MainActivity.this, OrderPage.class);
            //intent.putExtra("userId", userId); // Добавляем email в Intent




            //Сдесь реализовать шаред преф




            textView4.setText(Integer.toString(userId));
        } else {
            // пользователя с таким email не найдено
        }


        Intent intent = new Intent(MainActivity.this, OrderPage.class);
        intent.putExtra("email", email); // Добавляем email в Intent

//        List<Category> categoryList = new ArrayList<>();
//        categoryList.add(new Category(1,"Стратегия"));
//        categoryList.add(new Category(2,"Шутер"));
//        categoryList.add(new Category(3,"Хоррор"));
//        categoryList.add(new Category(4,"Приключения"));
//
//        setCategoryRecycler(categoryList);


        ///////////////////////////////////////////////////////////////////////

        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:3000/category_from_client";//Для ЭМУЛЯТОРА
        //String url = "http://192.168.1.101:3000/category_from_client";//Для физ устройства

        Request request = new Request.Builder()
                        .url(url)
                        .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            List<Category> categoryList = new ArrayList<>();
                            try {
                                JSONArray jsonArray = new JSONArray(myResponse);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    SQLiteDatabase db = DatabaseHelper.getDatabase(context);
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String idcategory = jsonObject.getString("idcategory");
                                    String gameCategory = jsonObject.getString("game_category");
                                    Category category = new Category(idcategory, gameCategory);
                                    categoryList.add(category);
                                    setCategoryRecycler(categoryList);

                                    // создаем объект ContentValues для хранения значений столбцов
                                    ContentValues values = new ContentValues();
                                    values.put(DatabaseHelper.CATEGORY_ID, idcategory);
                                    values.put(DatabaseHelper.CATEGORY_TITLE, gameCategory);

                                    // вставляем данные в таблицу games
                                    db.insert(DatabaseHelper.TABLE_CATEGORY, null, values);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }





                        }
                    });
                }
            }
        });

        //////////////////////////////////////////////////////






        ///////////////////////////////////////////////////////////////////////
        OkHttpClient client_game_url = new OkHttpClient();
        String game_url = "http://10.0.2.2:3000/games_from_client"; //Для эмулятора
        //String game_url = "http://192.168.1.101:3000/games_from_client"; //Для физустройства

        Request game_request = new Request.Builder()
                .url(game_url)
                .build();
        client_game_url.newCall(game_request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String myResponse_games = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(myResponse_games);
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    SQLiteDatabase db = DatabaseHelper.getDatabase(context);
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int id_game = jsonObject.getInt("id_game");
                                    String game_name = jsonObject.getString("game_name");
                                    String game_price = jsonObject.getString("game_price");
                                    String game_publish = jsonObject.getString("game_publish");
                                    String game_publisher = jsonObject.getString("game_publisher");
                                    String firstTenChars = game_publish.substring(0, Math.min(game_publish.length(), 10));
                                    String discription = jsonObject.getString("discription");
                                    String game_category = jsonObject.getString("game_category");
                                    Game game = new Game(id_game, game_name, game_price, firstTenChars, game_publisher, discription, game_category);
                                    gameList.add(game);
                                    fullGamesList.clear();
                                    fullGamesList.addAll(gameList);
                                    setGameRecycler(gameList);




                                    // создаем объект ContentValues для хранения значений столбцов
                                    ContentValues values = new ContentValues();
                                    values.put(DatabaseHelper.ID_GAME, id_game);
                                    values.put(DatabaseHelper.GAME_NAME, game_name);
                                    values.put(DatabaseHelper.GAME_PRICE, game_price);
                                    values.put(DatabaseHelper.GAME_PUBLISHED, game_publish);
                                    values.put(DatabaseHelper.GAME_PUBLISHER, game_publisher);
                                    values.put(DatabaseHelper.GAME_DESCRIPTION, discription);
                                    values.put(DatabaseHelper.GAME_CATEGORY, game_category);

                                    // вставляем данные в таблицу games
                                    db.insert(DatabaseHelper.TABLE_GAMES, null, values);





                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }





                        }
                    });
                }
            }
        });

        //////////////////////////////////////////////////////



        ///////////////////////////////////////////////////////////////////////

        OkHttpClient user_client = new OkHttpClient();
        String user_url = "http://10.0.2.2:3000/id_from_user";//Для ЭМУЛЯТОРА
        //String user_url = "http://192.168.1.101:3000/id_from_user";//Для физ устройства

        Request user_request = new Request.Builder()
                .url(user_url)
                .build();
        user_client.newCall(user_request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String myResponse = response.body().string();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            List<Category> categoryList = new ArrayList<>();
                            try {
                                JSONArray jsonArray = new JSONArray(myResponse);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    SQLiteDatabase db = DatabaseHelper.getDatabase(context);
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Integer id = jsonObject.getInt("id");
                                    String unique_id = jsonObject.getString("unique_id");
                                    String name = jsonObject.getString("name");
                                    String email = jsonObject.getString("email");
                                    String encrypted_password = jsonObject.getString("encrypted_password");
                                    String salt = jsonObject.getString("salt");
                                    String created_at = jsonObject.getString("created_at");
                                    String updated_at = jsonObject.getString("updated_at");

                                    User user = new User(id, unique_id, name, email, encrypted_password, salt, created_at, updated_at);
//                                    gameList.add(user);
//                                    fullGamesList.clear();
//                                    fullGamesList.addAll(gameList);
//                                    setGameRecycler(gameList);





//                                    Intent intent = new Intent(MainActivity.this, OrderPage.class);
//                                    intent.putExtra("email", email); // Добавляем email в Intent


                                    // создаем объект ContentValues для хранения значений столбцов
                                    ContentValues values = new ContentValues();
                                    values.put(DatabaseHelper.USER_ID, id);
                                    values.put(DatabaseHelper.USER_UNIQUE_ID, unique_id);
                                    values.put(DatabaseHelper.USER_NAME, name);
                                    values.put(DatabaseHelper.USER_EMAIL, email);
                                    values.put(DatabaseHelper.USER_ENCRYPTED_PASSWORD, encrypted_password);
                                    values.put(DatabaseHelper.USER_SALT, salt);
                                    values.put(DatabaseHelper.USER_CREATED_AT, created_at);
                                    values.put(DatabaseHelper.USER_UPDATED_AT, updated_at);

                                    // вставляем данные в таблицу games
                                    db.insert(TABLE_USERS, null, values);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }





                        }
                    });
                }
            }
        });

        //////////////////////////////////////////////////////














//        List<Category> categoryList = new ArrayList<>();
//        categoryList.add(new Category(1,"Стратегия"));
//        categoryList.add(new Category(2,"Шутер"));
//        categoryList.add(new Category(3,"Хоррор"));
//        categoryList.add(new Category(4,"Приключения"));
//
//        setCategoryRecycler(categoryList);


        gameList.clear();
       //gameList.add(new Game(1, "Игра по вселенной \nWarcraft","5", "05.04.2001", "Blizard","Массовая многопользовательская ролевая онлайн-игра, разработанная и издаваемая компанией Blizzard Entertainment. Действие World of Warcraft происходит в фэнтезийной вселенной Warcraft.","2"));
       //gameList.add(new Game(2, "Шутер от \n1 лица", "100", "03.11.2007","Steam","Многопользовательский шутер от 1 лица","1"));


        fullGamesList.clear();
        fullGamesList.addAll(gameList);

        setGameRecycler(gameList);
    }

    private void setGameRecycler(List<Game> gameList) {


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        gameRecycler = findViewById(R.id.gameRecycler);
        gameRecycler.setLayoutManager(layoutManager);

        gameAdapter = new GameAdapter(this, gameList);
        gameRecycler.setAdapter(gameAdapter);
        //
        //gameAdapter.notifyDataSetChanged();
        //
    }

    private void setCategoryRecycler(List<Category> categoryList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        categoryRecycler = findViewById(R.id.categoryRecycler);
        categoryRecycler.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryRecycler.setAdapter(categoryAdapter);

    }

    public void openShoppingCart(View view){
        Intent intent = new Intent(this, OrderPage.class);
        startActivity(intent);
    }


    public static void showGamesByCategory(String category) {
        gameList.clear();
        gameList.addAll(fullGamesList);

        List<Game> filterGames = new ArrayList<>();
        for (Game c : gameList) {
            if (c.getCategory().equals(category)) {
                filterGames.add(c);
            }
        }

        gameList.clear();
        gameList.addAll(filterGames);

        gameAdapter.notifyDataSetChanged();
    }

    public void showallcategory(View view) {
        gameList.clear();
        gameList.addAll(fullGamesList);
        gameAdapter.notifyDataSetChanged();
    }
}