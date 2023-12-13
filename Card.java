public class Card {
    private int chestOrChance; //0 is chest, 1 is chance
    private String name;
    private String description;
    private int type;
    private int amtPosColor;

    Card(int coc, String n, String d, int t, int apc) {
        this.chestOrChance = coc;
        this.name = n;
        this.description = d;
        this.type = t;
        this.amtPosColor = apc;
    }

    public String getName() {
        return name;
    }

    public int getCoc() {
        return chestOrChance;
    }

    public String getDescription() {
        return description;
    }

    public int getAmc() {
        return amtPosColor;
    }

    public int getType() {
        return type;
    }

    /* types of cards: 
        Card(int coc, String n, String d, int t, int apc)
        0 - gain money (amt)
        1 - go to jail
        2 - goojf
        3 - go to spot (pos)
        4 - go to certain color property (color)
        5 - go back 3 spaces
        6 - pay money (amt)
        7 - pay each player 50
        8 - get 50 from each player
        9 - street repairs (25/100 if chance, 40/115 if chest)
    */
}