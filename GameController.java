import java.util.ArrayList;

public class GameController {
    private String[] spaces = {"", ""};
    private ArrayList<Property> properties;
    private ArrayList<Player> players;

    public GameController(ArrayList<Property> properties, ArrayList<Player> players) {
        this.properties = properties;
        this.players = players;
    }

    public Boolean takeTurn(Player currPlayer) {
        return false;
    }

    public void Chest() {

    }

    public void Chance() {

    }
}