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

    public void takeTurn(Player currPlayer, int doublesCount, GridController controller) {
        die1 = (int)(Math.floor(Math.random() * 6) + 1);
        die2 = (int)(Math.floor(Math.random() * 6) + 1); //roll dice

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
        if(currPlayer.getMonopolies() > 0 && currPlayer.canBuyHouses()) {
            playerOptions.add(2);
        }
        if(currPlayer.getTotalHouses() > 0) {
            playerOptions.add(3);
        }
        if(currPlayer.getJailed()) {
            playerOptions.add(4);
            if(currPlayer.getGOOJF() > 0) {
                playerOptions.add(5);
            }
        }
        playerOptions.add(6);

        Boolean repeat = false;
        if (die1 == die2) {
            repeat = true;
            doublesCount++;
        }
        if (doublesCount == 2) { //third doubles
            currPlayer.setJailed(true);
        }
        if(!currPlayer.getJailed()) { //if player is in normal state
            clearScreen();
            currPlayer.setPosition(currPlayer.getPosition() + die1 + die2);
            controller.updateView();

        } else { //if player is jailed
            currPlayer.setPosition(10); //jail position
            clearScreen();
            
        }
        if(repeat) {
            takeTurn(currPlayer, doublesCount, controller);
        }
    }

    public void executeOptions(int option) {
        if(option == 0) { //trade

        } else if (option == 1) { //view/mortgage properties

        } else if (option == 2) { //buy houses

        } else if (option == 3) { //sell houses

        } else if (option == 4) { //pay $50 GOOJ

        } else if (option == 5) { //use GOOJF

        } else if (option == 6) { //end turn
            
        }
    }

    public void executePos(Player player, int pos) {
        if ((pos == 4 || pos == 38)) { //tax
            player.giveMoney(players.get(0), (7300 - (pos*125))/34);
        } else if (pos == 20) { //free parking
            players.get(0).giveMoney(player, players.get(0).getMoney());
        } else if (pos == 2 || pos == 17 || pos == 33) {
            Chest();
        } else if (pos == 7 || pos == 22 || pos == 36) {
            Chance();
        } else if (pos == 0) { //regular go money plus 300
            player.setMoney(player.getMoney() + 300);
        } else if (pos == 30) { //go to miege
            player.setJailed(true);

        }
    }

    public Property getByPos(int pos) {
        Property prop = new Property();
        for(int i = 0; i < properties.size(); i++) {
            if (properties.get(i).getPosition() == i) {
                prop = properties.get(i);
                break;
            }
        }
        return prop;
    }

    public int whoOwns(Property prop) { //see which player owns a given property
        int pl = -1; //return -1 if property unowned
        for(int i = 1; i < players.size(); i++) {
            if(players.get(i).hasProperty(prop)) {
                pl = i;
                break;
            }
        }
        return pl;
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