import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    int money = 1500;
    int jailFree = 0;
    int jailTurns;
    int ID;
    int piece;
    /* pieces
       0 - top hat
       1 - thimble
       2 - shoe
       3 - iron
       4 - wedding ring U+014E
       5 - cannon
       horseshoe U+028A

       house U+1260
       hotel U+1268
       nose U+1228
       person U+127E
       arrow U+1445
       goal post U+040F
       trident U+03A8
       spinny wheel U+058D

       U+2591	░	Light shade

        U+2592	▒	Medium shade
        
        U+2593	▓	Dark shade

        colors needed:
            background - text
            black - N/A (borders)
            cream - black (background with black text)
            cream - N/A (most background)
            cream - gray (pieces)
            cream - red (go)
            red - white(monopoly logo + free parking amount)
            each monopoly color - green (houses)
            each monopoly color - dark red (hotels)

            //color = "\u001B[38;5;xm"
            //background color = "\u001B[38;5;xm"

     */
    //int monopolies = 0;
    Boolean passGo = false;
    int position = 0;
    Boolean jailed = false;
    ArrayList<Integer> monopolies = new ArrayList<Integer>();
    ArrayList<Property> playerProperties;

    public Player(ArrayList<Property> properties, int ID) {
        this.playerProperties = properties;
        this.ID = ID;
    }

    //called when a person buys a house, sells a house, buys a property, or mortgages/sells a property
    public void updateRent(Property toUpdate) { 
        ArrayList<Integer> pos = new ArrayList<Integer>(); //save position of each property of same color as toUpdate
        for(int i = 0; i < playerProperties.size(); i++) {
            if (playerProperties.get(i).getColor() == toUpdate.getColor() && !playerProperties.get(i).getMortgaged()) {
                pos.add(i);
            }
        }
        if (toUpdate.getColor() == 8 || toUpdate.getColor() == 9) { //railroad or utility
            for (int i : pos) { //each owned property in set
                changeRent(i, pos.size() - 1);
                /*if (toUpdate.getMortgaged()) { //if property was just mortgaged, decrease rent
                    changeRent(i, getRentPos(playerProperties.get(i)) - 1);
                } else { //if just bought, increase rent
                    changeRent(i, getRentPos(playerProperties.get(i)) + pos.size() - 1);
                }*/
            }
        } else if (toUpdate.getColor() != 0 && toUpdate.getColor() != 7) { //all 3-property monopolies
            if(pos.size() == 3) { //only change something if monopoly is owned
                if(monopolies.indexOf(playerProperties.get(pos.get(0)).getColor()) == -1) { //if monopoly is new
                    /*System.out.println("adding monopoly " + playerProperties.get(pos.get(0)).getColor());
                    Scanner scan = new Scanner(System.in);
                    scan.nextLine();*/
                    monopolies.add(playerProperties.get(pos.get(0)).getColor());
                }
                ArrayList<Integer> houses = getHouseCount(pos);
                for (int i = 0; i < pos.size(); i++) {
                    changeRent(pos.get(i), 1 + houses.get(i)); //sets rent to doubled rent position + number of houses
                }
            } else if (pos.size() < 3 && getRentPos(toUpdate) != 0) { //if not a monopoly but rent is larger than initial
                if(monopolies.contains(playerProperties.get(pos.get(0)).getColor())) {
                    /*System.out.println("removing monopoly " + playerProperties.get(pos.get(0)).getColor());
                    Scanner scan = new Scanner(System.in);
                    scan.nextLine();*/
                    //monopolies.remove(playerProperties.get(pos.get(0)).getColor());
                    monopolies.remove(Integer.valueOf(playerProperties.get(pos.get(0)).getColor()));
                }   
                for(int i : pos) {
                    changeRent(i, 0);
                }
            }
        } else { //dark blue & purple properties
            if(pos.size() == 2) { //only change something if monopoly is owned
                ArrayList<Integer> houses = getHouseCount(pos);
                if(!monopolies.contains(playerProperties.get(pos.get(0)).getColor())) { //if monopoly is new
                    /*System.out.println("adding monopoly " + playerProperties.get(pos.get(0)).getColor());
                    Scanner scan = new Scanner(System.in);
                    scan.nextLine();*/
                    monopolies.add(playerProperties.get(pos.get(0)).getColor());
                }
                for (int i = 0; i < pos.size(); i++) {
                    changeRent(pos.get(i), 1 + houses.get(i)); //sets rent to doubled rent position + number of houses
                }
            } else if (pos.size() < 2 && getRentPos(toUpdate) != 0) { //if not a monopoly but rent is larger than initial
                /*System.out.println("removing monopoly " + playerProperties.get(pos.get(0)).getColor());
                    Scanner scan = new Scanner(System.in);
                    scan.nextLine();*/
                //monopolies.indexOf();
                monopolies.remove(Integer.valueOf(playerProperties.get(pos.get(0)).getColor()));
                //monopolies.remove(new Integer(playerProperties.get(pos.get(0)).getColor()));
                //monopolies.remove((int)playerProperties.get(pos.get(0)).getColor());
                for(int i : pos) {
                    changeRent(i, 0);
                }
            }
        }
    }

    public void changeRent(int pos, int amt) { //which property and where to 
        playerProperties.get(pos).setRent(playerProperties.get(pos).getRent(amt));
    }

    public int getRentPos(Property prop) { //get position in rent ArrayList of current rent
        int rentPos = 0;
        for (int i = 0; i < prop.getRentSize(); i++) {
            if (prop.getCurrentRent() == prop.getRent(i)) {
                rentPos = i;
            }
        }
        return rentPos;
    }

    public void giveProperties(ArrayList<Property> props, Player receive) {
        for(Property prop: props) {
            giveProperty(prop, receive);
        }
    }

    public ArrayList<Integer> getHouseCount(ArrayList<Integer> properties) {
        ArrayList<Integer> houses = new ArrayList<Integer>();
        for (int i : properties) {
            houses.add(playerProperties.get(i).getHouses());
        }
        return houses;
    }

    public void addProperty(Property prop) {
        playerProperties.add(prop);
        updateRent(playerProperties.get(playerProperties.size() - 1));
    }

    public int getID() {
        return ID;
    }

    public Boolean getGo() {
        return passGo;
    }

    public ArrayList<Integer> getAllMonopolies() {
        return monopolies;
    }

    public void setGo(Boolean x) {
        passGo = x;
    }

    public void removeProperty(Property prop) { //if property is traded away (prereq has no houses)
        for (int i = 0; i < playerProperties.size(); i++) {
            if (prop.equals(playerProperties.get(i))) {
                playerProperties.remove(i);
            }
        }
        updateRent(prop);
    }

    public ArrayList<Integer> getAllColors() {
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int i = 0; i < playerProperties.size(); i++) {
            if(!colors.contains(playerProperties.get(i).getColor())) {
                colors.add(playerProperties.get(i).getColor());
            }
        }
        return colors;
    }

    public void addHouse(Property prop) {
        prop.setHouses(prop.getHouses() + 1);
        updateRent(prop);
    }

    public void removeHouse(Property prop) { //remove house from a specific property
        prop.setHouses(prop.getHouses() - 1);
        money += ((prop.getColor() + 2)/2) * 25;
        updateRent(prop);
    }

    public void removeHouse(int monopoly) { //remove house from a monopoly
        ArrayList<Integer> props = new ArrayList<Integer>();
        for(int i = 0; i < playerProperties.size(); i++) { //get an arraylist containing each property in the monopoly
            if (playerProperties.get(i).getColor() == monopoly) {
                props.add(i);
            }
        }
        int mostHousesPos = -1;
        int mostHouses = 0;
        for(int i = 0; i < props.size(); i++) {
            if(playerProperties.get(props.get(i)).getHouses() > mostHouses) {
                mostHouses = playerProperties.get(props.get(i)).getHouses();
                mostHousesPos = i;
            }
        }
        removeHouse(playerProperties.get(props.get(mostHousesPos)));
    }

    public int getMaxMonopolyHouses(int monopoly) {
        if(monopoly == 0 || monopoly == 7) {
            return 10;
        } else{
            return 15;
        }
    }

    public void setGOOJF(int amt) {
        jailFree = amt;
    }

    public void addHouse(int monopoly) {
        ArrayList<Integer> props = new ArrayList<Integer>();
        for(int i = 0; i < playerProperties.size(); i++) { //get an arraylist containing each property in the monopoly
            if (playerProperties.get(i).getColor() == monopoly) {
                props.add(i);
            }
        }
        int leastHousesPos = 10;
        int leastHouses = 10;
        for(int i = props.size() - 1; i >= 0; i--) {
            if(playerProperties.get(props.get(i)).getHouses() < leastHouses) {
                leastHouses = playerProperties.get(props.get(i)).getHouses();
                leastHousesPos = i;
            }
        }
        addHouse(playerProperties.get(props.get(leastHousesPos)));
    }

    public void mortgageProperty(Property prop) {
        prop.setMortgage(true);
        //money += prop.getCost() / 2;
        updateRent(prop);
    }

    public void unmortgageProperty(Property prop) {
        prop.setMortgage(false);
        //need to check if can afford cost
        updateRent(prop);
    }

    public void giveProperty(Property prop, Player p) {
        removeProperty(prop);
        p.addProperty(prop);
    }

    public void giveMoney(Player p, int amt) {
        money -= amt;
        p.money += amt;
    }

    public void setMoney(int amt) {
        money = amt;
    }

    public int getMoney() {
        return money;
    }

    /*monopolies vector
        has color of each owned monopoly
    */

    public Boolean canBuyHouses() {
        Boolean openSpots = false;
        int costCheapestHouse = 0;
        for (int i = 0; i < 8; i++) {
            //check if an owned monopoly has less than 5 houses per property
            if(monopolies.contains(i) && ((i == 0 || i == 7) && getMonopolyHouses(i) < 10) || ((i > 0 && i < 7) && getMonopolyHouses(i) < 15)) {
                openSpots = true;
                costCheapestHouse = ((i + 2)/2)*50; //goes left to right so first found monopoly is cheapest
                break;
            }
        }
        return openSpots && money >= costCheapestHouse; //open spots and affordable
    }

    public int getMonopolyHouses(int color) { //total houses in a monopoly
        int house = 0;
        for(int i = 0; i < playerProperties.size(); i++) {
            if (playerProperties.get(i).getColor() == color) {
                house += playerProperties.get(i).getHouses();
            }
        }
        return house;
    }

    public int getTotalHouses() { //total number of houses the player has
        int house = 0;
        for (int i = 0; i < playerProperties.size(); i++) {
            house += playerProperties.get(i).getHouses();
        }
        return house;
    }

    public int getMonopolies() {
        return monopolies.size();
    }

    public void setJailed(Boolean x) {
        jailed = x;
        if(jailed) {
            position = 10;
            jailTurns = 4;
        }
    }

    public void setJailTurns(int amt) {
        jailTurns = amt;
    }

    public int getJailTurns() {
        return jailTurns;
    }

    public Boolean getJailed() {
        return jailed;
    }

    public void setPosition(int pos) {
        position = pos;
        if (position > 39) {
            position -= 40;
            passGo = true;
        }
    }

    public Boolean hasProperty(Property prop) {
        for (int i = 0; i < playerProperties.size(); i++) {
            if (playerProperties.get(i).getPosition() == prop.getPosition()) {
                return true;
            }
        }
        return false;
    }

    public int getPosition() {
        return position;
    }

    public ArrayList<Property> getProperties() {
        return playerProperties;
    }

    public int getPropertyCount() {
        return playerProperties.size();
    }

    public int getUnmortgagedPropertyCount() {
        int counter = 0;
        for(int i = 0; i < playerProperties.size(); i++) {
            if(!playerProperties.get(i).getMortgaged()) {
                counter++;
            }
        }
        return counter;
    }

    public int getGOOJF() {
        return jailFree;
    }

}
