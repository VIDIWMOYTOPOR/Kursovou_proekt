package com.example.kursovou_proekt;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Model.Categoryy;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import retrofit2.http.Query;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.kursovou_proekt.Retrofit.INodeJS;
import com.example.kursovou_proekt.Retrofit.RetrofitClient;
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


public class MainActivity extends AppCompatActivity {


    RecyclerView categoryRecycler, gameRecycler;
    CategoryAdapter categoryAdapter;
    static GameAdapter gameAdapter;
    static List<Game> gameList = new ArrayList<>();
    static List<Game> fullGamesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String email = getIntent().getStringExtra("email");

        TextView textView4 = findViewById(R.id.textView4);
        textView4.setText(email);



//        List<Category> categoryList = new ArrayList<>();
//        categoryList.add(new Category(1,"Стратегия"));
//        categoryList.add(new Category(2,"Шутер"));
//        categoryList.add(new Category(3,"Хоррор"));
//        categoryList.add(new Category(4,"Приключения"));
//
//        setCategoryRecycler(categoryList);


        ///////////////////////////////////////////////////////////////////////
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:3000/category_from_client";

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
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int idcategory = jsonObject.getInt("idcategory");
                                    String gameCategory = jsonObject.getString("game_category");
                                    Category category = new Category(idcategory, gameCategory);
                                    categoryList.add(category);
                                    setCategoryRecycler(categoryList);
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
        String game_url = "http://10.0.2.2:3000/games_from_client";

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
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int id_game = jsonObject.getInt("id_game");
                                    String title = jsonObject.getString("title");
                                    String game_price = jsonObject.getString("game_price");
                                    String date = jsonObject.getString("date");
                                    String poster = jsonObject.getString("poster");
                                    String text = jsonObject.getString("text");
                                    int category = jsonObject.getInt("category");
                                    Game game = new Game(id_game, title, game_price, date, poster, text, category);
                                    gameList.add(game);
                                    fullGamesList.clear();
                                    fullGamesList.addAll(gameList);
                                    setGameRecycler(gameList);
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
       gameList.add(new Game(1, "Игра по вселенной \nWarcraft","5", "05.04.2001", "Blizard","Массовая многопользовательская ролевая онлайн-игра, разработанная и издаваемая компанией Blizzard Entertainment. Действие World of Warcraft происходит в фэнтезийной вселенной Warcraft.",1));
       gameList.add(new Game(2, "Шутер от \n1 лица", "100", "03.11.2007","Steam","Многопользовательский шутер от 1 лица",2));


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


    public static void showGamesByCategory(int category){

        gameList.clear();
        gameList.addAll(fullGamesList);

        List<Game> filterGames = new ArrayList<>();
        for(Game c: gameList){
            if (c.getCategory() == category)
                filterGames.add(c);
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