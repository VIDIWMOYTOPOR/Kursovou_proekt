package com.example.kursovou_proekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Model.Order;

public class GamePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        ConstraintLayout gameBg = findViewById(R.id.GamePageBg);
        ImageView gameImage = findViewById(R.id.GamePageImage);
        TextView gameTitle = findViewById(R.id.gamePageTitle);
        TextView gameDate = findViewById(R.id.gamePageDate);
        TextView gamePoster = findViewById(R.id.gamePagePoster);
        TextView gameText = findViewById(R.id.gamePageText);

        gameBg.setBackgroundColor(getIntent().getIntExtra("gameBg", 0));
        gameImage.setImageResource(getIntent().getIntExtra("gameImage",0));
        gameTitle.setText(getIntent().getStringExtra("gameTitle"));
        gameDate.setText(getIntent().getStringExtra("gameDate"));
        gamePoster.setText(getIntent().getStringExtra("gamePoster"));
        gameText.setText(getIntent().getStringExtra("gameText"));
    }

    public void addToCart(View view) {
        int item_id = getIntent().getIntExtra("gameId", 0);
        Order.items_id.add(item_id);
        Toast.makeText(this,"Добавлено", Toast.LENGTH_LONG).show();
    }

    public void openShoppingCart(View view){
        Intent intent = new Intent(this, OrderPage.class);
        startActivity(intent);
    }

    public void openMainPage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}