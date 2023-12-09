import java.util.ArrayList;

public class Property {
    private String name;
    private String line1;
    private String line2;
    private int color;
    private int cost;
    private int position;
    private int[] rents;
    private int currentRent;
    private int houses = 0;
    private Boolean mortgaged = false;

    public Property(String name, String longName, int color, int position, int[] rents, int cost) {
        fixName(longName);
        this.name = name;
        this.color = color;
        this.position = position;
        this.rents = rents;
        currentRent = rents[0];
        this.cost = cost;
    }

    public Property() {}

    private void fixName(String word) {
        String first = word.substring(0,7);
        String second = word.substring(7);
        this.line1 = first;
        this.line2 = second;
    }

    public void setRent(int rent) {
        currentRent = rent;
    }

    public int getRent(int pos) {
        return rents[pos];
    }

    public int getCurrentRent() {
        return currentRent;
    }

    public int getRentSize() {
        return rents.length;
    }

    public Boolean getMortgaged() {
        return mortgaged;
    }

    public int getPosition() {
        return position;
    }

    public int getHouses() {
        return houses;
    }

    public int getCost() {
        return cost;
    }

    public int getColor() {
        return color;
    }

    public void setHouses(int x) {
        houses = x;
    }

    public String getName() {
        return name;
    }

    public void setMortgage(Boolean x) {
        mortgaged = x;
    }
}

/*colors: 
0: purple
1: light blue
2: pink
3: orange
4: red
5: yellow
6: green
7: dark blue
8: railroad
9: utility

rents:
colored properties:   utilites:   railroads:
0 - initial rent      0 - 4       0 - 25
1 - doubled rent      1 - 10      1 - 50
2 - 1 house                       2 - 100
3 - 2 houses                      3 - 200
4 - 3 houses
5 - 4 houses
6 - hotel
*/