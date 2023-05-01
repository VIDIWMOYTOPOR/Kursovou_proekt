package Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kursovou_proekt.GamePage;
import com.example.kursovou_proekt.R;

import java.util.List;

import Model.Game;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    Context context;
    List<Game> games;

    public GameAdapter(Context context, List<Game> games) {
        this.context = context;
        this.games = games;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View gameItems = LayoutInflater.from(context).inflate(R.layout.game_item, parent, false);
        return new GameAdapter.GameViewHolder(gameItems);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.gameTitle.setText(games.get(position).getTitle());
        holder.gameDate.setText(games.get(position).getDate());
        holder.gamePoster.setText(games.get(position).getPoster());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GamePage.class);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        (Activity) context,
                        new Pair<View, String>(holder.gameImage,"gameImage")
                        );

                intent.putExtra("gameTitle",games.get(position).getTitle());
                intent.putExtra("gameDate",games.get(position).getDate());
                intent.putExtra("gamePoster",games.get(position).getPoster());
                intent.putExtra("gameText",games.get(position).getText());
                intent.putExtra("gameId",games.get(position).getId());


                context.startActivity(intent, options.toBundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public static final class GameViewHolder extends RecyclerView.ViewHolder{

        CardView gameBg;
        ImageView gameImage;
        TextView gameTitle, gameDate, gamePoster;



        public GameViewHolder(@NonNull View itemView) {
            super(itemView);

            gameBg = itemView.findViewById(R.id.gameBg);
            gameImage = itemView.findViewById(R.id.gameImage);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            gameDate = itemView.findViewById(R.id.gameDate);
            gamePoster = itemView.findViewById(R.id.gamePoster);


        }
    }

}
