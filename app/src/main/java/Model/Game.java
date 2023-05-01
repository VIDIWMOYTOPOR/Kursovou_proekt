package Model;

public class Game {
    int id_game, game_category;
    String game_name, game_price, game_publish, game_publisher, discription;

    public Game(int id_game, String game_name, String game_price, String game_publish, String game_publisher, String discription, int game_category) {
        this.id_game = id_game;
        this.game_name = game_name;
        this.game_price = game_price;
        this.game_publish = game_publish;
        this.game_publisher = game_publisher;
        this.discription = discription;
        this.game_category = game_category;
    }

    public int getCategory() {
        return game_category;
    }

    public void setCategory(int category) {
        this.game_category = category;
    }

    public String getGamePrice() {
        return game_price;
    }

    public void setGamePrice(String price) {
        this.game_price = price;
    }

    public String getText() {
        return discription;
    }

    public void setText(String text) {
        this.discription = text;
    }

    public int getId() {
        return id_game;
    }

    public void setId(int id) {
        this.id_game = id;
    }

    public String getTitle() {
        return game_name;
    }

    public void setTitle(String title) {
        this.game_name = title;
    }

    public String getDate() {
        return game_publish;
    }

    public void setDate(String date) {
        this.game_publish = date;
    }

    public String getPoster() {
        return game_publisher;
    }

    public void setPoster(String poster) {
        this.game_publisher = poster;
    }
}
