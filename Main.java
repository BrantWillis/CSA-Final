import java.util.*;

class Grid {
    private char[][] grid;
    //private char[][] gridColorsChar; //encoded ANSI
    private String[][] gridColors; //decoded ANSI
    ArrayList<Property> properties;
    ArrayList<Player> players;

    public Grid(int rows, int cols, ArrayList<Property> props, ArrayList<Player> plays) {
        properties = props;
        players = plays;
        grid = new char[rows][cols];
        gridColors = new String[rows][cols];
        //gridColorsChar = new char[rows][cols];
        //change gridColorsChar from char to string here
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                //make full display with setCell
                //if()
                setCell(i,j,' ',"\u001B[48;5;255;38;5;16m");
            }
        }
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {

                if(i%5 == 4 && ((j >=0 && j < 5) || (j > cols - 6 && j < cols))) {
                    setCell(i, j, ' ', "\u001B[48;5;16;38;5;16m");
                }
                if(j%5 == 4 && ((i >=0 && i < 5) || (i > rows - 6 && i < rows))) {
                    setCell(i, j, ' ', "\u001B[48;5;16;38;5;16m");
                }
                if (i == 4 || i == rows - 5 || j == 4 || j == cols - 5) {
                    setCell(i, j, ' ', "\u001B[48;5;16;38;5;16m");
                }
                if(i >= 0 && i < 5 && j % 5 != 4) { //actions for top line
                    if(i == 0) { //text line 1
                        setCell(i,j,line1[(j/5) + 20].charAt(j%5), "\u001B[48;5;255;38;5;16m");
                    }
                    if(i == 1) { //text line 2
                        setCell(i,j,line2[(j/5) + 20].charAt(j%5), "\u001B[48;5;255;38;5;16m");
                    }
                    if(i == 3) { //prices
                        setCell(i,j,moneyStringify(getByPos((j/5) + 20).getCost()).charAt(j%5), "\u001B[48;5;255;38;5;16m");
                    }
                    if(i == 4) { //property colors
                        if(getByPos((j/5)+20).getColor() == 4) {
                            setCell(i, j, ' ', "\u001B[48;5;196;38;5;34m");
                        } else if(getByPos((j/5)+20).getColor() == 5) {
                            setCell(i, j, ' ', "\u001B[48;5;226;38;5;34m");
                        }
                    }
                }
                if(i < rows && i >= rows - 5 && j % 5 != 4) {//actions for bottom line
                    if(i == rows - 5) { //colors 
                        if(getByPos(10 - (j/5)).getColor() == 0 && getByPos(10 - (j/5)).getCost() > 0) {
                            setCell(i, j, ' ', "\u001B[48;5;5;38;5;34m");
                        } else if(getByPos(10 - (j/5)).getColor() == 1) {
                            setCell(i, j, ' ', "\u001B[48;5;51;38;5;34m");
                        }
                    }
                    if(i == rows - 4) { //text line 1
                        setCell(i,j,line1[10 - (j/5)].charAt(j%5), "\u001B[48;5;255;38;5;16m");
                        if(10 - (j/5) == 0) {
                            setCell(i, j, line1[10 - (j/5)].charAt(j%5), "\u001B[48;5;255;38;5;196m");
                        }
                    }
                    if(i == rows - 3) {//text line 2
                        setCell(i,j,line2[10 - (j/5)].charAt(j%5), "\u001B[48;5;255;38;5;16m");
                        if(10 - (j/5) == 0) {
                            setCell(i, j, line2[10 - (j/5)].charAt(j%5), "\u001B[48;5;255;38;5;196m");
                        }
                    }
                    if (i == rows - 1) {//prices
                        setCell(i,j,moneyStringify(getByPos(10 - (j/5)).getCost()).charAt(j%5), "\u001B[48;5;255;38;5;16m");
                    }
                }
                if(j >= 0 && j < 5 && i % 5 != 4) { //actions for left line
                    if(j != 4) { //keep text horizontal
                        if(i % 5 == 0) {
                            setCell(i,j,line1[20 - (i/5)].charAt(j%5), "\u001B[48;5;255;38;5;16m");
                        }
                        if(i % 5 == 1) {
                            setCell(i,j,line2[20 - (i/5)].charAt(j%5), "\u001B[48;5;255;38;5;16m");
                        }
                        if(i % 5 == 3) {
                            setCell(i,j,moneyStringify(getByPos(20 - (i/5)).getCost()).charAt(j%5), "\u001B[48;5;255;38;5;16m");
                        }
                    }
                    else if(j == 4) {//colors
                        if(getByPos(20 - (i/5)).getColor() == 3) {
                        setCell(i, j, ' ', "\u001B[48;5;202;38;5;34m");
                    } else if (getByPos(20 - (i/5)).getColor() == 2) {
                        setCell(i, j, ' ', "\u001B[48;5;201;38;5;34m");
                    }
                    }
                }
                if(j >= cols - 5 && j < cols && i%5 != 4) { //right line (excluding go)
                    if(j != cols - 5 && i < 50) {
                        if(i % 5 == 0) {
                            setCell(i,j,line1[(i/5) + 30].charAt(j%5), "\u001B[48;5;255;38;5;16m");
                        }
                        if(i % 5 == 1) {
                            setCell(i,j,line2[(i/5) + 30].charAt(j%5), "\u001B[48;5;255;38;5;16m");
                        }
                        if(i % 5 == 3) {
                            setCell(i,j,moneyStringify(getByPos((i/5) + 30).getCost()).charAt(j%5), "\u001B[48;5;255;38;5;16m");
                        }
                    } else if(j == cols - 5) {
                        if(getByPos((i/5) + 30).getColor() == 6) {
                            setCell(i, j, ' ', "\u001B[48;5;22;38;5;34m");
                        } else if (getByPos((i/5) + 30).getColor() == 7) {
                            setCell(i, j, ' ', "\u001B[48;5;4;38;5;34m");
                        }
                    }
                }
                if((j ==2 && (i == rows - 3 || i == rows - 4)) || (j == 3 && (i == rows - 3 || i == rows - 4))) {
                    setCell(i, j, ' ', "\u001B[48;5;202;38;5;16m");
                }
                /*if(j < 4 && i == 3) {
                    setCell(i, j, moneyStringify(players.get(0).getMoney()).charAt(j%5), "\u001B[48;5;255;38;5;16m");
                }*/
                if(10 - (j/5) == 4 && j % 5 != 4 && i == rows - 1) {
                    setCell(i, j, moneyStringify(200).charAt(j%5), "\u001B[48;5;255;38;5;16m");
                }
                if((i/5) + 30 == 38 && i % 5 == 3 && j >= cols - 4 && j < cols - 1) {
                    setCell(i, j, moneyStringify(75).charAt(j%5), "\u001B[48;5;255;38;5;16m");
                }
                if(j == 0 && i == rows - 4) setCell(i, j, 'S', "\u001B[48;5;255;38;5;16m");
                if(j == 0 && i == rows - 3) setCell(i, j, 'H', "\u001B[48;5;255;38;5;16m");
                if(j == 0 && i == rows - 2) setCell(i, j, 'A', "\u001B[48;5;255;38;5;16m");
                if(j == 0 && i == rows - 1) setCell(i, j, 'D', "\u001B[48;5;255;38;5;16m");
                if(j == 1 && i == rows - 1) setCell(i, j, 'O', "\u001B[48;5;255;38;5;16m");
                if(j == 2 && i == rows - 1) setCell(i, j, 'W', "\u001B[48;5;255;38;5;16m");
                if(j == 3 && i == rows - 1) setCell(i, j, 'S', "\u001B[48;5;255;38;5;16m");
                if(i >= 24 && i <= 28 && j > 15 && j < 38) {
                    if(j > 16 && j < 37 && i == 25) {
                        setCell(i,j,kc.charAt(j - 17),"\u001B[48;5;196;38;5;15m");
                    } else if (j > 19 && j < 33 && i == 27) {
                        setCell(i,j,bc.charAt(j - 20),"\u001B[48;5;196;38;5;15m");
                    } else {
                        setCell(i,j,' ', "\u001B[48;5;196;38;5;15m");
                    }
                }
            }
        }
    }
    private String kc = "KANSAS CITY MONOPOLY";
        private String bc = "BRANTCO, 2023";
    private String[] line1 = new String[]{" GO ","FRFX","COMM","FRNT","PROP","STRT","PROS","CHNC","LNWD","INDP","    ",
    "HCKM"," KC ","BLUE","RED ","KCI ","BRRY","COMM","nOAK","ARMR","FREE","WRNL","CHNC","GRGY","75TH","UNIN","MAIN",
    "39TH"," KC ","WSTP","GOTO","GRND","18TH","COMM"," SW ","RIDE","CHNC","BDWY","CHFS","WARD"};
    private String[] line2 = new String[]{"<<<<","TFWY","CHST"," ST ","TAX ","CAR ","PECT"," ?? ","BLVD","AVE ","    ",
    "MLLS","P&L ","RDGE","BRDG","    ","ROAD","CHST","TFWY","ROAD","PARK","ROAD"," ?? ","BLVD"," ST ","STN "," ST ",
    " ST ","WATR","ROAD","MIEG","BLVD"," ST ","CHST","BLVD"," KC "," ?? ","BLVD","TKTS","PKWY"};

    public int getRows() {
        return grid.length;
    }

    public int getColumns() {
        return grid[0].length;
    }

    public static String moneyStringify(int a) {
        if(a == 0) return "    ";
        String ret = "";
        if(Integer.toString(a).length() < 4) {
            ret += "$" + Integer.toString(a);
        } else {
            ret += Integer.toString(a);
        }
        if(ret.length() == 3) {
            ret += " ";
        }
        return ret;
    }

    public Property getByPos(int pos) {
        Property prop = new Property();
        for(int i = 0; i < properties.size(); i++) {
            if (properties.get(i).getPosition() == pos) {
                prop = properties.get(i);
                break;
            }
        }
        return prop;
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
        String toDisplay = "";
        //System.out.println(grid.length + "," + grid[0].length);
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                //System.out.print(j);
                toDisplay += gridColors[i][j] + grid[i][j] + " " + ANSI_RESET;
                //System.out.print(gridColors[i][j] + grid[i][j] + ANSI_RESET);
            }
            toDisplay+="\n";
            //System.out.println();
        }
        System.out.println(toDisplay);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}

class GridController {
    private Grid model;
    private GridView view;
    private static int macOrOther;
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Property> properties = new ArrayList<Property>();

    public GridController(Grid model, GridView view, ArrayList<Player> players, ArrayList<Property> properties) {
        this.model = model;
        this.view = view;
        this.players = players;
        this.properties = properties;
    }

    public void setMOO(int val) {
        macOrOther = val;
    }
    
    public static int getMOO() {
        return macOrOther;
    }

    public void updateView() {
        //check for houses
        for(int i = 0; i < 40; i++) {
            Property prop = getByPos(i);
            if(prop.getCost() > 0) {
                int startingRow = -1;
                int startingColumn = -1;
                String color = "";
                int col = -1;
                int incrementRow = -10;
                int incrementColumn = -10;
                if(i > 0 && i < 10) { //bottom row
                    startingRow = model.getRows() - 5;
                    startingColumn = ((10 - i) * 5);
                    incrementColumn = 1;
                    incrementRow = 0;
                }
                if(i > 10 && i < 20) { //left column
                    startingRow = (20 - i) * 5;
                    startingColumn = 4;
                    incrementColumn = 0;
                    incrementRow = 1;
                }
                if(i > 20 && i < 30) { //top row
                    startingColumn = (i - 20) * 5;
                    startingRow = 4;
                    incrementColumn = 1;
                    incrementRow = 0;
                }
                if(i > 30) { //right column
                    startingRow = (i - 30) * 5;
                    startingColumn = model.getColumns() - 5;
                    incrementColumn = 0;
                    incrementRow = 1;
                }
                col = prop.getColor();

                if(prop.getHouses() < 5) {
                    switch(col) {
                        case 0: color = "\u001B[48;5;5;38;5;34m";break;
                        case 1: color = "\u001B[48;5;51;38;5;34m";break;
                        case 2: color = "\u001B[48;5;201;38;5;34m";break;
                        case 3: color = "\u001B[48;5;202;38;5;34m";break;
                        case 4: color = "\u001B[48;5;196;38;5;34m";break;
                        case 5: color = "\u001B[48;5;226;38;5;34m";break;
                        case 6: color = "\u001B[48;5;22;38;5;34m";break;
                        case 7: color = "\u001B[48;5;4;38;5;34m";break;
                        case 8: color = "\u001B[48;5;16;38;5;16m";break;
                        case 9: color = "\u001B[48;5;16;38;5;16m";break;
                        default: color = "";break;
                    }
                } else {
                    switch(col) {
                        case 0: color = "\u001B[48;5;5;38;5;88m";break;
                        case 1: color = "\u001B[48;5;51;38;5;88m";break;
                        case 2: color = "\u001B[48;5;201;38;5;88m";break;
                        case 3: color = "\u001B[48;5;202;38;5;88m";break;
                        case 4: color = "\u001B[48;5;196;38;5;88m";break;
                        case 5: color = "\u001B[48;5;226;38;5;88m";break;
                        case 6: color = "\u001B[48;5;22;38;5;88m";break;
                        case 7: color = "\u001B[48;5;4;38;5;88m";break;
                        case 8: color = "\u001B[48;5;16;38;5;16m";break;
                        case 9: color = "\u001B[48;5;16;38;5;16m";break;
                        default: color = "";break;
                    }
                }
                for(int j = 0; j < 4; j++) {
                    setCell(startingRow + (j * incrementRow), startingColumn + (j * incrementColumn), ' ', color);
                }
                if(prop.getHouses() != 5) {
                    for(int j = 0; j < prop.getHouses(); j++) {
                        if(GridController.getMOO() == 2) {
                            setCell(startingRow + (j * incrementRow), startingColumn + (j * incrementColumn), 'A', color);
                        } else {
                            setCell(startingRow + (j * incrementRow), startingColumn + (j * incrementColumn), 'በ', color);
                        }
                    }
                } else {
                    if(GridController.getMOO() == 2) {
                        setCell(startingRow + incrementRow, startingColumn + incrementColumn, 'H', color);
                    } else {
                        setCell(startingRow + incrementRow, startingColumn + incrementColumn, 'ቨ', color);
                    }
                }
            }
        }
        for(int i = 1; i < players.size(); i++) { //player positions
            Player p = players.get(i);
            int pos = p.getLastPos();
            int startingRow = -1;
            int startingColumn = -1;

            //erase previous
            if(pos < 10) { //bottom row
                startingRow = model.getRows() - 2;
                startingColumn = ((10 - pos) * 5);
            }
            if(pos == 10) {
                if(!p.getLastJail()) {
                    switch(p.getID()) {
                        case 1: setCell(model.getRows() - 4, 1, ' ', "\u001B[48;5;255;38;5;16m");break;
                        case 2: setCell(model.getRows() - 3, 1, ' ', "\u001B[48;5;255;38;5;16m");break;
                        case 3: setCell(model.getRows() - 2, 2, ' ', "\u001B[48;5;255;38;5;16m");break;
                        case 4: setCell(model.getRows() - 2, 2, ' ', "\u001B[48;5;255;38;5;16m");break;
                        default: break;
                    }
                } else {
                    switch(p.getID()) {
                        case 1: setCell(model.getRows() - 4, 2, ' ', "\u001B[48;5;202;38;5;16m");break;
                        case 2: setCell(model.getRows() - 3, 2, ' ', "\u001B[48;5;202;38;5;16m");break;
                        case 3: setCell(model.getRows() - 4, 3, ' ', "\u001B[48;5;202;38;5;16m");break;
                        case 4: setCell(model.getRows() - 3, 3, ' ', "\u001B[48;5;202;38;5;16m");break;
                        default: break;
                    }
                }
            }
            if(pos > 10 && pos < 20) { //left column
                startingRow = ((20 - pos) * 5) + 2;
                startingColumn = 0;
            }
            if(pos > 20 && pos < 30) { //top row
                startingColumn = (pos - 20) * 5;
                startingRow = 2;
            }
            if(pos > 30) { //right column
                startingRow = ((pos - 30) * 5) + 2;
                startingColumn = model.getColumns() - 4;
            }
            if(pos != 10) {
                setCell(startingRow, startingColumn + p.getID() - 1, ' ', "\u001B[48;5;255;38;5;16m");
            }

            //display current position
            pos = p.getPosition();
            if(pos < 10) { //bottom row
                startingRow = model.getRows() - 2;
                startingColumn = ((10 - pos) * 5);
            }
            if(pos == 10) {
                if(!p.getJailed()) {
                    switch(p.getID()) {
                        case 1: setCell(model.getRows() - 4, 1, p.getPiece().getSymbol(), "\u001B[48;5;248;38;5;16m");break;
                        case 2: setCell(model.getRows() - 3, 1, p.getPiece().getSymbol(), "\u001B[48;5;248;38;5;16m");break;
                        case 3: setCell(model.getRows() - 2, 2, p.getPiece().getSymbol(), "\u001B[48;5;248;38;5;16m");break;
                        case 4: setCell(model.getRows() - 2, 2, p.getPiece().getSymbol(), "\u001B[48;5;248;38;5;16m");break;
                        default: break;
                    }
                } else {
                    switch(p.getID()) {
                        case 1: setCell(model.getRows() - 4, 2, p.getPiece().getSymbol(), "\u001B[48;5;202;38;5;16m");break;
                        case 2: setCell(model.getRows() - 3, 2, p.getPiece().getSymbol(), "\u001B[48;5;202;38;5;16m");break;
                        case 3: setCell(model.getRows() - 4, 3, p.getPiece().getSymbol(), "\u001B[48;5;202;38;5;16m");break;
                        case 4: setCell(model.getRows() - 3, 3, p.getPiece().getSymbol(), "\u001B[48;5;202;38;5;16m");break;
                        default: break;
                    }
                }
            }
            if(pos > 10 && pos < 20) { //left column
                startingRow = ((20 - pos) * 5) + 2;
                startingColumn = 0;
            }
            if(pos > 20 && pos < 30) { //top row
                startingColumn = (pos - 20) * 5;
                startingRow = 2;
            }
            if(pos > 30) { //right column
                startingRow = ((pos - 30) * 5) + 2;
                startingColumn = model.getColumns() - 4;
            }
            if(pos != 10) {
                if(players.size() > 3) {
                    setCell(startingRow, startingColumn + p.getID() - 1, p.getPiece().getSymbol(), "\u001B[48;5;248;38;5;16m");
                } else if(players.size() == 3) {
                    setCell(startingRow, startingColumn + p.getID(), p.getPiece().getSymbol(), "\u001B[48;5;248;38;5;16m");
                }
            }

            
        }
        for(int i = 0; i < 4; i++) { //free parking
            setCell(3, i, Grid.moneyStringify(players.get(0).getMoney()).charAt(i), "\u001B[48;5;255;38;5;16m");
        }
        //set free parking
        /*setCell(5,1,'$','a');
        for(int i = 2; i < Integer.toString(players.get(0).getMoney()).length(); i++) {
            setCell(5,i, Integer.toString(getPlace(players.get(0).getMoney(), i-2)).charAt(0),'a');
        }*/
        view.displayGrid(model.getGrid(), model.getGridColors());
    }

    public Property getByPos(int pos) {
        Property prop = new Property();
        for(int i = 0; i < properties.size(); i++) {
            if (properties.get(i).getPosition() == pos) {
                prop = properties.get(i);
                break;
            }
        }
        return prop;
    }

    private int getPlace(int val, int pos) {
        String ret = Integer.toString(val);
        return Integer.parseInt(ret.substring(pos, pos+1));
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


        String input3 = "-1";
        while(Integer.parseInt(input3) != 1 && Integer.parseInt(input3) != 2) {
            clearScreen();
            System.out.println("Are you playing on Mac Terminal or a different terminal? This will affect the graphics.\n1. Mac Terminal\n2. Other Terminal");
            input3 = scan.nextLine();
            try {
                Integer.parseInt(input3);
            } catch(NumberFormatException t) {
                input3 = "-1";
            }
        }
        //controller.setMOO(Integer.parseInt(input3));

        ArrayList<Piece> pieces = new ArrayList<Piece>();
        if(Integer.parseInt(input3) == 1) {
            Piece p1 = new Piece("Wedding Ring", 'Ŏ');
            Piece p2 = new Piece("Horseshoe", 'ʊ');
            Piece p3 = new Piece("Nose", 'ረ');
            Piece p4 = new Piece("Devil", 'ቾ');
            Piece p5 = new Piece("Goal Posts", 'Џ');
            Piece p6 = new Piece("Trident", 'Ψ');
            pieces.add(p1);pieces.add(p2);pieces.add(p3);pieces.add(p4);pieces.add(p5);pieces.add(p6);
        } else {
            Piece p1 = new Piece("Wedding Ring", 'o');
            Piece p2 = new Piece("Horseshoe", 'U');
            Piece p3 = new Piece("Nose", 'L');
            Piece p4 = new Piece("Bling", '$');
            Piece p5 = new Piece("Goal Posts", 'Y');
            Piece p6 = new Piece("Snake", 'S');
            pieces.add(p1);pieces.add(p2);pieces.add(p3);pieces.add(p4);pieces.add(p5);pieces.add(p6);
        }
        /*person U+127E
       arrow U+1445
       goal post U+040F
       trident U+03A8*/
        

        freeParking.setMoney(0);
        //a.setGOOJF(2);
        //a.setMoney(10000);
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
        //a.addProperty(prop1);
        /*a.addProperty(prop2);
        a.addProperty(prop3);
        a.addProperty(prop4);
        a.addProperty(prop5);
        a.addProperty(prop6);
        a.addProperty(prop7);
        a.addProperty(prop8);
        a.addProperty(prop9);
        a.addProperty(prop10);
        a.addProperty(prop21);
        a.addProperty(prop11);
        a.addProperty(prop18);
        a.addProperty(prop26);
        a.addProperty(prop27);
        a.addProperty(prop28);*/


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

        Grid model = new Grid(54, 54, props, players);
        GridView view = new GridView();
        GridController controller = new GridController(model, view, players, props);
        controller.setMOO(Integer.parseInt(input3));


        //picking names
        for(int i = 1; i < players.size(); i++) {
            input3 = "ijhasoeriiiiiiiifjoiasjelkfjasoi";
            while(input3.length() < 3 || input3.length() > 10) {
                clearScreen();
                System.out.println("Player " + i + ", Pick a name (3-10 characters, no spaces)");
                input3 = scan.nextLine();
                if(input3.contains(" ")) {
                    input3 = "oisjaoeifjioas";
                }
            }
            players.get(i).setName(input3);
        }

        //picking pieces
        String input7 = "-1";
        int choice = -1;
        int playerCount = 1;
        while(playerCount < players.size() ) {
            while(Integer.parseInt(input7) < 1 || Integer.parseInt(input7) > pieces.size()) {
                clearScreen();
                System.out.println(players.get(playerCount).getName() + ", pick a game piece:");
                for(int i = 0; i < pieces.size(); i++) {
                    System.out.println((i + 1) + ". " + pieces.get(i).getName() + " (" + pieces.get(i).getSymbol() + ")");
                }
                input7 = scan.nextLine();
                try {
                    choice = Integer.parseInt(input7);
                } catch (NumberFormatException r) {
                    input7 = "-1";
                }
            }
            players.get(playerCount).addPiece(pieces.get(choice - 1));
            pieces.remove(pieces.get(choice - 1));
            input7 = "-1";
            playerCount++;
        }
        clearScreen();
        for(int i = 1; i < players.size(); i++) {
            System.out.println("Player " + i + " - " + players.get(i).getName() + " - " + players.get(i).getPiece().getName());
        }
        System.out.println("Press enter to start game.");
        scan.nextLine();

        //a.setMoney(25);

        /*players arraylist
        0 - free parking
        1 - player 1
        2 - player 2
        3 - player 3 (if there)
        4 - player 4 (if there)
        */

        GameController game = new GameController(props, players, controller);

        while(players.size() > 2) {
            for(int i = 1; i < players.size(); i++) { //skip free parking
                game.takeTurn(players.get(i), 0);
            }
        }
        scan.close();
        System.out.println("Congrats on winning, " + players.get(1).getName() + "!");

        //controller.setCell(2, 2, 'x');
        //controller.updateView();
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}

class Piece {
    private String name;
    private char symbol;

    Piece(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }
    Piece() {}

    public String getName() {
        return name;
    }
    public char getSymbol() {
        return symbol;
    }
}

// javac *.java && java Main
