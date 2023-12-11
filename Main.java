import java.util.*;

class Grid {
    private char[][] grid;
    private char[][] gridColorsChar; //encoded ANSI
    private String[][] gridColors; //decoded ANSI
    ArrayList<Property> properties;

    public Grid(int rows, int cols) {
        grid = new char[rows][cols];
        gridColorsChar = new char[rows][cols];
        //change gridColorsChar from char to string here
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                //make full display with setCell
            }
        }
    }

    public void setCell(int row, int col, char value, String valueColor) {
        grid[row][col] = value;
        gridColors[row][col] = valueColor;
    }

    public char[][] getGrid() {
        return grid;
    }

    public String[][] getGridColors() {
        return gridColors;
    }
}

class GridView {
    public void displayGrid(char[][] grid, String[][] gridColors) {
        clearScreen();
        final String ANSI_RESET = "\u001B[0m";
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; i++) {
                System.out.print(gridColors[i][j] + grid[i][j] + ANSI_RESET);
            }
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}

class GridController {
    private Grid model;
    private GridView view;

    public GridController(Grid model, GridView view) {
        this.model = model;
        this.view = view;
    }

    public void updateView() {
        //check player positions, houses, free parking balance
        //view.displayGrid(model.getGrid(), model.getGridColors());
        clearScreen();
        System.out.println("board");
    }

    public void setCell(int row, int col, char value, String valueColor) {
        model.setCell(row, col, value, valueColor);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        Property prop1 = new Property("Fairfax Trafficway", "FAIRFAX TWFY  ", 0, 1, new int[]{2,4,10,30,90,160,250}, 60);
        Property prop2 = new Property("Front Street", " FRONT   ST   ", 0, 3, new int[]{4,8,20,60,180,320,450},60);
        Property prop3 = new Property("KC Streetcar", "STREET   CAR  ", 8, 5, new int[]{25,50,100,200}, 200);
        Property prop4 = new Property("Prospect Avenue", "  THE   SPECT ", 1, 6, new int[]{6,12,30,90,270,400,550}, 100);
        Property prop5 = new Property("Linwood Boulevard", "LINWOOD BLVD  ", 1, 8, new int[]{6,12,30,90,270,400,550},100);
        Property prop6 = new Property("Independence Avenue", "THE AVE       ", 1, 9, new int[]{8,16,40,100,300,450,600},120);
        Property prop7 = new Property("Hickman Mills Drive", "HICKMAN MILLS ", 2, 11, new int[]{10,20,50,150,450,625,750},140);
        Property prop8 = new Property("KC Power & Light", " KCP&L        ", 9, 12, new int[]{4,10},150);
        Property prop9 = new Property("Blue Ridge Boulevard", " BLUE   RIDGE ", 2, 13, new int[]{10,20,50,150,450,625,750},140);
        Property prop10 = new Property("Red Bridge Boulevard", "  RED  BRIDGE ", 2, 14, new int[]{12,24,60,180,500,700,900},160);
        Property prop11 = new Property("KC International Airport", "  KCI         ", 8, 15, new int[]{25,50,100,200},200);
        Property prop12 = new Property("Barry Road", " BARRY  ROAD  ", 3, 16, new int[]{14,28,70,200,550,750,950},180);
        Property prop13 = new Property("North Oak Trafficway", " N OAK  TFWY  ", 3, 18, new int[]{14,28,70,200,550,750,950},180);
        Property prop14 = new Property("Armour Road", "ARMOUR  ROAD  ", 3, 19, new int[]{16,32,80,220,600,800,1000},200);
        Property prop15 = new Property("Wornall Road", "WORNALL ROAD  ", 4, 21, new int[]{18,36,90,250,700,875,1050},220);
        Property prop16 = new Property("Gregory Boulevard", "GREGORY BLVD  ", 4, 23, new int[]{18,36,90,250,700,875,1050},220);
        Property prop17 = new Property("Seventy-Fifth Street", " 75TH  STREET ", 4, 24, new int[]{20,40,100,300,750,925,1100},240);
        Property prop18 = new Property("Union Station", " UNION STATION", 8, 25, new int[]{25,50,100,200},200);
        Property prop19 = new Property("Main Street", " MAIN  STREET ", 5, 26, new int[]{22,44,110,330,800,975,1150},260);
        Property prop20 = new Property("Thirty-Nineth Street", " 39TH  STREET ", 5, 27, new int[]{22,44,110,330,800,975,1150},260);
        Property prop21 = new Property("Kansas City Water", "  KC    WATER ", 9, 28, new int[]{4,10},150);
        Property prop22 = new Property("Wesport Road", " WEST- PORT RD", 5, 29, new int[]{24,48,120,360,850,1025,1200},280);
        Property prop23 = new Property("Grand Boulevard", " GRAND  BLVD  ", 6, 31, new int[]{26,52,130,390,900,1100,1275},300);
        Property prop24 = new Property("Eighteenth Street", " 18TH  STREET ", 6, 32, new int[]{26,52,130,390,900,1100,1275},300);
        Property prop25 = new Property("Southwest Boulevard", "  SW    BLVD  ", 6, 34, new int[]{28,56,150,450,1000,1200,1400},320);
        Property prop26 = new Property("RideKC", "RIDEKC        ", 8, 35, new int[]{25,50,100,200},200);
        Property prop27 = new Property("Broadway Boulevard", " BRDWY  BLVD  ", 7, 37, new int[]{35,70,175,500,1100,1300,1500},350);
        Property prop28 = new Property("Ward Parkway", " WARD   PKWY  ", 7, 39, new int[]{50,100,200,600,1400,1700,2000},400);
        ArrayList<Property> props = new ArrayList<Property>();
        props.add(prop1);props.add(prop2);props.add(prop3);props.add(prop4);props.add(prop5);props.add(prop6);props.add(prop7);props.add(prop8);props.add(prop9);props.add(prop10);props.add(prop11);props.add(prop12);props.add(prop13);props.add(prop14);props.add(prop15);props.add(prop16);props.add(prop17);props.add(prop18);props.add(prop19);props.add(prop20);props.add(prop21);props.add(prop22);props.add(prop23);props.add(prop24);props.add(prop25);props.add(prop26);props.add(prop27);props.add(prop28);
        
        ArrayList<Property> player1Props = new ArrayList<Property>();
        ArrayList<Property> player2Props = new ArrayList<Property>();
        ArrayList<Property> player3Props = new ArrayList<Property>();
        ArrayList<Property> player4Props = new ArrayList<Property>();
        ArrayList<Property> freeParkProps = new ArrayList<Property>();
        

        Player a = new Player(player1Props, 1);
        Player b = new Player(player2Props, 2);
        Player c = new Player(player3Props, 3);
        Player d = new Player(player4Props, 4);
        Player freeParking = new Player(freeParkProps, 0);


        ArrayList<Player> players = new ArrayList<Player>();

        freeParking.setMoney(0);
        //a.setMoney(200);
        //a.setPosition(19);
        /*a.addProperty(prop1);
        a.addProperty(prop2);
        a.addProperty(prop15);
        a.addProperty(prop16);
        a.addProperty(prop17);
        prop2.setHouses(1);
        prop17.setHouses(1);
        a.setMoney(-15);*/
        //a.setJailed(true);
        /*a.addProperty(prop1);
        a.addProperty(prop2);
        a.addProperty(prop3);
        a.addProperty(prop4);
        a.addProperty(prop5);
        a.addProperty(prop6);
        a.addProperty(prop7);
        a.addProperty(prop8);
        a.addProperty(prop9);
        a.addProperty(prop10);
        a.addProperty(prop21);*/

        int num = -1;
        while (num < 2 || num > 4) {
            clearScreen();
            System.out.println("How many players are there? (2-4)");
            String input = scan.nextLine();
            try { //check if input is an integer, if not, just make it 0
                Integer.parseInt(input);
            } catch (NumberFormatException e) {
                input = "-1";
            }
            num = Integer.parseInt(input);
        }
        players.add(freeParking);
        players.add(a);
        players.add(b);
        if(num > 2) players.add(c);
        if(num == 4) players.add(d);
        //a.setMoney(25);

        /*players arraylist
        0 - free parking
        1 - player 1
        2 - player 2
        3 - player 3 (if there)
        4 - player 4 (if there)
        */

        Grid model = new Grid(91, 91);
        GridView view = new GridView();
        GridController controller = new GridController(model, view);
        GameController game = new GameController(props, players, controller);

        while(players.size() > 1) {
            for(int i = 1; i < players.size(); i++) { //skip free parking
                game.takeTurn(players.get(i), 0);
            }
        }
        scan.close();

        //controller.setCell(2, 2, 'x');
        //controller.updateView();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}

// javac *.java && java Main
