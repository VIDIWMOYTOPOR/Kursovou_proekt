package com.example.kursovou_proekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Adapter.CategoryAdapter;
import Adapter.GameAdapter;
import Model.Category;
import Model.Game;





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



        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1,"Стратегия"));
        categoryList.add(new Category(2,"Шутер"));
        categoryList.add(new Category(3,"Хоррор"));
        categoryList.add(new Category(4,"Приключения"));

        setCategoryRecycler(categoryList);


        ///////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////






        gameList.clear();
        gameList.add(new Game(1,"warcraft", "Игра по вселенной \nWarcraft", "05.04.2001", "#137ebd","Blizard","Массовая многопользовательская ролевая онлайн-игра, разработанная и издаваемая компанией Blizzard Entertainment. Действие World of Warcraft происходит в фэнтезийной вселенной Warcraft.",1));
        gameList.add(new Game(2,"csgo", "Шутер от \n1 лица", "03.11.2007", "#EC6D6D","Steam","Многопользовательский шутер от 1 лица",2));


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