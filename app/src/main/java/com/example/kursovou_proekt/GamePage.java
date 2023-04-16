package com.example.kursovou_proekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
}