import java.util.ArrayList;

public class Player {
    int money = 1500;
    int jailFree = 0;
    //int monopolies = 0;
    int position = 0;
    Boolean jailed = false;
    ArrayList<Integer> monopolies;
    ArrayList<Property> playerProperties;

    public Player(ArrayList<Property> properties) {
        this.playerProperties = properties;
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
                monopolies.add(playerProperties.get(pos.get(0)).getColor());
                ArrayList<Integer> houses = getHouseCount(pos);
                for (int i = 0; i < pos.size(); i++) {
                    changeRent(pos.get(i), 1 + houses.get(i)); //sets rent to doubled rent position + number of houses
                }
            } else if (pos.size() < 3 && getRentPos(toUpdate) != 0) { //if not a monopoly but rent is larger than initial
                monopolies.remove(playerProperties.get(pos.get(0)).getColor());
                for(int i : pos) {
                    changeRent(i, 0);
                }
            }
        } else { //dark blue & purple properties
            if(pos.size() == 2) { //only change something if monopoly is owned
                ArrayList<Integer> houses = getHouseCount(pos);
                monopolies.add(playerProperties.get(pos.get(0)).getColor());
                for (int i = 0; i < pos.size(); i++) {
                    changeRent(pos.get(i), 1 + houses.get(i)); //sets rent to doubled rent position + number of houses
                }
            } else if (pos.size() < 2 && getRentPos(toUpdate) != 0) { //if not a monopoly but rent is larger than initial
                monopolies.remove(playerProperties.get(pos.get(0)).getColor());
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

    public void removeProperty(Property prop) { //if property is traded away (prereq has no houses)
        for (int i = 0; i < playerProperties.size(); i++) {
            if (prop == playerProperties.get(i)) {
                playerProperties.remove(i);
            }
        }
        updateRent(prop);
    }

    public void addHouse(Property prop) {
        prop.setHouses(prop.getHouses() + 1);
        updateRent(prop);
    }

    public void removeHouse(Property prop) {
        prop.setHouses(prop.getHouses() - 1);
        updateRent(prop);
    }

    public void mortgageProperty(Property prop) {
        prop.setMortgage(true);
        money += prop.getCost() / 2;
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
        if(checkMoney(amt)) {
            money -= amt;
            p.money += amt;
        }
        else {
            System.out.println("You cannot afford this.");
        }
    }

    public Boolean canBuyHouses() {
        for (int i = 0; i < monopolies.size(); i++) {

        }
        return true;
    }

    public int getHouses(int color) {
        
    }

    public int getMonopolies() {
        return monopolies.size();
    }

    public Boolean checkMoney(int amt) {
        if(money - amt < 0) {
            return false;
        }
        return true;
    }

    public void setJailed(Boolean x) {
        jailed = x;
    }

    public Boolean getJailed() {
        return jailed;
    }

    public void setPosition(int pos) {
        position = pos;
    }

    public int getPosition() {
        return position;
    }

    public int getPropertyCount() {
        return playerProperties.size();
    }

}
