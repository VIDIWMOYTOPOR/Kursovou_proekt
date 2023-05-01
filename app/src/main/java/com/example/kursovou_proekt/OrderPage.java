package com.example.kursovou_proekt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Model.Game;
import Model.Order;

public class OrderPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        ListView order_list = findViewById(R.id.order_list);

        List<String> gameTitle = new ArrayList<>();
        for(Game c: MainActivity.fullGamesList){
            if(Order.items_id.contains(c.getId()))
                gameTitle.add(c.getTitle());
        }
        //order_list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Order.items_id.toArray()));
        order_list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gameTitle));

    }

}