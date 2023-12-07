import java.util.ArrayList;

public class GameController {
    private String[] spaces = {"", ""};
    private ArrayList<Property> properties;
    private ArrayList<Player> players;
    int die1;
    int die2;

    public GameController(ArrayList<Property> properties, ArrayList<Player> players) {
        this.properties = properties;
        this.players = players;
    }

    public void takeTurn(Player currPlayer, int doublesCount) {
        die1 = (int)(Math.floor(Math.random() * 6) + 1);
        die2 = (int)(Math.floor(Math.random() * 6) + 1);
        ArrayList<Integer> playerOptions = new ArrayList<Integer>();
        int[] options = new int[]{0,1,2,3,4,5,6};
        /*options:
        0 - offer a trade
        1 - view/mortgage properties
        2 - buy houses
        3 - sell houses
        4 - pay $50 to get out of jail
        5 - use get out of jail free
        6 - end turn
         */
        playerOptions.add(0);
        if(currPlayer.getPropertyCount() > 0) {
            playerOptions.add(1);
        }
        if(currPlayer.getMonopolies() > 0) {
            playerOptions.add(2);
        }
        Boolean repeat = false;
        if (die1 == die2) {
            repeat = true;
            doublesCount++;
        }
        if (doublesCount == 2) { //second doubles
            currPlayer.setJailed(true);
        }
        if(!currPlayer.getJailed()) { //if player is in normal state
            clearScreen();

        } else { //if player is jailed
            currPlayer.setPosition(10); //jail position
            
        }
        if(repeat) {
            takeTurn(currPlayer, doublesCount);
        }
    }

    public void Chest() {

    }

    public void Chance() {

    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}