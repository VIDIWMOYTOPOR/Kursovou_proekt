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
        int defaultImageResId = R.drawable.warcraft;

        ConstraintLayout gameBg = findViewById(R.id.GamePageBg);
        ImageView gameImage = findViewById(R.id.gameImage);
        TextView gameTitle = findViewById(R.id.gamePageTitle);
        TextView gameDate = findViewById(R.id.gamePageDate);
        TextView gamePoster = findViewById(R.id.gamePagePoster);
        TextView gameText = findViewById(R.id.gamePageText);
        TextView gamePrice = findViewById(R.id.gamePagePrice);


        gameBg.setBackgroundColor(getIntent().getIntExtra("gameBg", 0));
        gameImage.setImageResource(getIntent().getIntExtra("gameImage", defaultImageResId));
        gameTitle.setText(getIntent().getStringExtra("gameTitle"));
        gameDate.setText(getIntent().getStringExtra("gameDate"));
        gamePoster.setText(getIntent().getStringExtra("gamePoster"));
        gameText.setText(getIntent().getStringExtra("gameText"));
        gamePrice.setText(getIntent().getStringExtra("gamePrice"));

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