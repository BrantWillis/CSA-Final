import java.util.*;

public class GameController {
    private String[] spaces = {"", ""};
    private ArrayList<Property> properties;
    private ArrayList<Player> players;
    private GridController controller;
    int die1;
    int die2;

    public GameController(ArrayList<Property> properties, ArrayList<Player> players, GridController controller) {
        this.properties = properties;
        this.players = players;
        this.controller = controller;
    }

    public void takeTurn(Player currPlayer, int doublesCount) {
        Scanner scan = new Scanner(System.in);
        die1 = (int)(Math.floor(Math.random() * 6) + 1);
        die2 = (int)(Math.floor(Math.random() * 6) + 1); //roll dice
        //die1 = 0;
        //die2 = 1;

        ArrayList<Integer> playerOptions = new ArrayList<Integer>();
        int[] options = new int[]{0,1,2,3,4,5,6};
        String input1 = "1";
        String input2 = "-1";
        String input3 = "-1";
        String[]optionNames = new String[]{"Offer a trade","Buy current property", "Manage properties","Buy houses","Sell a house","Pay $50 to get out of jail","Use a get out of jail free card","End turn"};
        /*options:
        0 - offer a trade
        1 - buy current property
        1 - view/mortgage properties
        2 - buy houses
        3 - sell houses
        4 - pay $50 to get out of jail
        5 - use get out of jail free
        6 - end turn
         */

        Boolean repeat = false;
        if (die1 == die2) {
            repeat = true;
            doublesCount++;
        }
        controller.updateView();
        System.out.println(currPlayer.getName() + " rolled a(n) " + die1 + " and a(n) " + die2 + ". Press enter to continue");
        scan.nextLine();
        if (doublesCount == 3) { //third doubles
            currPlayer.setJailed(true);
            doublesCount = 0;
            repeat = false;
        }
        currPlayer.setJailTurns(currPlayer.getJailTurns() - 1);
        if (currPlayer.getJailed() && (currPlayer.getJailTurns() == 0 || doublesCount > 0)) { //got doubles or no more turns in jail
            currPlayer.setJailed(false);
            if(doublesCount > 0) {
                repeat = false; //you can't go twice with doubles out of jail
            }
        }
        playerOptions.clear();
        if(currPlayer.getUnmortgagedPropertyCount() > 0 || currPlayer.getMoney() > 0 || currPlayer.getGOOJF() > 0) { //can trade if has some stuff
            playerOptions.add(0);
        }
        if(getByPos(currPlayer.getPosition()).getPosition() != -1 && currPlayer.getMoney() >= getByPos(currPlayer.getPosition()).getCost() && whoOwns(getByPos(currPlayer.getPosition())) == -1) {
            optionNames[1] = "Buy " + getByPos(currPlayer.getPosition()).getName() + " ($" + getByPos(currPlayer.getPosition()).getCost() + ")";
            playerOptions.add(1);
        }
        if(currPlayer.getPropertyCount() > 0) {
            playerOptions.add(2);
        }
        if(currPlayer.getMonopolies() > 0 && currPlayer.canBuyHouses()) {
            playerOptions.add(3);
        }
        if(currPlayer.getTotalHouses() > 0) {
            playerOptions.add(4);
        }
        if(currPlayer.getJailed()) {
            if(currPlayer.getMoney() >= 50) {
                playerOptions.add(5);
            }
            if(currPlayer.getGOOJF() > 0) {
                playerOptions.add(6);
            }
        }
        if(currPlayer.getMoney() >= 0) { //if not in debt can end turn
            playerOptions.add(7);
        }
        if(playerOptions.size() == 0) {
            players.remove(currPlayer);
            return;
        }
        
        
        //stuff
        if(!currPlayer.getJailed()) {
            currPlayer.setPosition(currPlayer.getPosition() + die1 + die2);

            if(currPlayer.getGo()) { //check if passed go
                currPlayer.setGo(false);
                currPlayer.setMoney(currPlayer.getMoney() + 200);
                controller.updateView();
                if (currPlayer.getPosition() != 0) {
                    System.out.print(currPlayer.getName() + " got $200 for passing Go!");
                } else {
                    System.out.print(currPlayer.getName() + " got $500 for lading on Go!");
                }
                System.out.println(" Press enter to continue.");
                scan.nextLine();
            }
        } else {
            currPlayer.setPosition(10);
        }
        controller.updateView();
        executePos(currPlayer, currPlayer.getPosition(), false, false); //do actions for current square
        if(currPlayer.getGo() && !currPlayer.getJailed()) { //check if passed go
            currPlayer.setGo(false);
            currPlayer.setMoney(currPlayer.getMoney() + 200);
            controller.updateView();
            int l = currPlayer.getLastPos();
            if (currPlayer.getPosition() != 0 && (l==2||l==7||l==17||l==33||l==22||l==36)) {//coming off chance or chest
                System.out.print(currPlayer.getName() + " got $200 for passing Go!");
            } else {
                System.out.print(currPlayer.getName() + " got $500 for lading on Go!");
            }
            System.out.println(" Press enter to continue.");
            scan.nextLine();
        }
        while(playerOptions.get(Integer.parseInt(input1) - 1) != 7) { //give options until player ends turn
            input1 = "-1";
            playerOptions.clear();
            if(currPlayer.getUnmortgagedPropertyCount() > 0 || currPlayer.getMoney() > 0 || currPlayer.getGOOJF() > 0) { //can trade if has some stuff
                playerOptions.add(0);
            }
            if(getByPos(currPlayer.getPosition()).getPosition() != -1 && currPlayer.getMoney() >= getByPos(currPlayer.getPosition()).getCost() && whoOwns(getByPos(currPlayer.getPosition())) == -1) {
                optionNames[1] = "Buy " + getByPos(currPlayer.getPosition()).getName() + " ($" + getByPos(currPlayer.getPosition()).getCost() + ")";
                playerOptions.add(1);
                //System.out.println("added");scan.nextLine();
            }
            if(currPlayer.getPropertyCount() > 0) {
                playerOptions.add(2);
            }
            if(currPlayer.getMonopolies() > 0 && currPlayer.canBuyHouses()) {
                playerOptions.add(3);
            }
            if(currPlayer.getTotalHouses() > 0) {
                playerOptions.add(4);
            }
            if(currPlayer.getJailed()) {
                if(currPlayer.getMoney() >= 50) {
                    playerOptions.add(5);
                }
                if(currPlayer.getGOOJF() > 0) {
                    playerOptions.add(6);
                }
            }
            if(currPlayer.getMoney() >= 0) { //if not in debt can end turn
                playerOptions.add(7);
            }
            if(playerOptions.size() == 0) {
                players.remove(currPlayer);
                return;
            }
            while(Integer.parseInt(input1) < 1 || Integer.parseInt(input1) > playerOptions.size()) {
                controller.updateView();
                System.out.print(currPlayer.getName() + " (" + currPlayer.getPiece().getName() + ")" + ": $" + currPlayer.getMoney());/*  die1:" + die1 + ", die2:" + die2 + ", position:" + currPlayer.getPosition());*/
                if(currPlayer.getJailed()) {
                    System.out.println(" - JAILED - " + currPlayer.getJailTurns() + " turn(s) left");
                } else {
                    System.out.println();
                }
                for(int i = 0; i < playerOptions.size(); i++) { //display each option
                    System.out.println(i + 1 + ". " + optionNames[playerOptions.get(i)]);
                }
                input1= scan.nextLine();
                try { //check if input is an integer, if not, just make it 0
                    Integer.parseInt(input1);
                } catch (NumberFormatException e) {
                    input1 = "-1";
                }
                /*if(input1 == null || input1 == "") {//if player just presses enter
                    input1 = "0";
                }*/
            }
            executeOptions(playerOptions.get(Integer.parseInt(input1) - 1), currPlayer);
        }
        if(repeat) {
            takeTurn(currPlayer, doublesCount);
        }
    }

    public void executeOptions(int option, Player currPlayer) {
        Scanner scan = new Scanner(System.in);
        String input = "-1";
        if(option == 0) { //trade
        
            System.out.println("trade");
            /*controller.updateView();*/controller.updateView();
            ArrayList<Player> tradeOptions = new ArrayList<Player>();
            String input1 = "-1";
            while(Integer.parseInt(input1) < 1 || Integer.parseInt(input1) > tradeOptions.size() + 1) {
                /*controller.updateView();*/controller.updateView();
                tradeOptions.clear();
                for(int i = 1; i < players.size(); i++) {
                    if(players.get(i).getID() != currPlayer.getID() && (players.get(i).getUnmortgagedPropertyCount() > 0 || players.get(i).getMoney() > 0 || players.get(i).getGOOJF() > 0)) {
                        tradeOptions.add(players.get(i));
                    }
                }
                System.out.println("Offer a trade to who?");
                for(int i = 0; i < tradeOptions.size(); i++) {
                    System.out.println((i+1) + ". " + tradeOptions.get(i).getName());
                }
                System.out.println((tradeOptions.size() + 1) + ". Go back");
                input1 = scan.nextLine();
                try { //check if input is an integer, if not, just make it 0
                    Integer.parseInt(input1);
                } catch (NumberFormatException e) {
                    input1 = "-1";
                }
            }
            if(Integer.parseInt(input1) == tradeOptions.size() + 1) {
                return;
            }
            int player = Integer.parseInt(input1) - 1;
            input1 = "-1";
            if(currPlayer.getMoney() > 0) {
                while(Integer.parseInt(input1) < 0 || Integer.parseInt(input1) > currPlayer.getMoney()) {
                    /*controller.updateView();*/controller.updateView();
                    System.out.println("How much money would you like to offer? (you have $" + currPlayer.getMoney() + ")");
                    input1 = scan.nextLine();
                    if(input1 == "") {
                        input1 = "-1";
                    }
                    if(input1.substring(0,1).equals("$")) {
                        input1 = input1.substring(1,input1.length());
                    }
                    try { //check if input is an integer, if not, just make it 0
                        Integer.parseInt(input1);
                    } catch (NumberFormatException e) {
                        input1 = "-1";
                    }
                }
            } else {
                input1 = "0";
            }
            int moneyGive = Integer.parseInt(input1);
            ArrayList<Integer> propertiesGive = new ArrayList<Integer>();
            ArrayList<Property> validProperties = new ArrayList<Property>();
            ArrayList<Property> orderedProperties = new ArrayList<Property>();
            String[] mons = new String[]{"Purple","Light Blue","Pink","Orange","Red","Yellow","Green","Dark Blue", "Public Transport", "Utilities"};
            ArrayList<Integer> allColors = new ArrayList<Integer>();
            ArrayList<Property> propertyGive = new ArrayList<Property>();
            if(currPlayer.getPropertyCount() > 0) {
                Boolean responseInvalid = true;
                while(responseInvalid) {
                    validProperties.clear();
                    propertiesGive.clear();
                    input1 = "-1";
                    /*controller.updateView();*/controller.updateView();
                    System.out.println("Would you like to offer any properties? If not, just press enter.");
                    System.out.println("(If offering multiple properties, separate them with commas, i.e. 2,4,7)");
                    for(int i = 0; i < currPlayer.getProperties().size(); i++) {
                        if(currPlayer.getMonopolyHouses(currPlayer.getProperties().get(i).getColor()) == 0) {
                            validProperties.add(currPlayer.getProperties().get(i));
                        }
                    }
                    for(int i = 0; i < validProperties.size(); i++) {
                        if(!allColors.contains(validProperties.get(i).getColor())) {
                            allColors.add(validProperties.get(i).getColor());
                        }
                    }
                    int t = 1;
                    for(int i = 0; i < 10; i++) { //cycle all monopolies
                        if(allColors.contains(i)) {
                            System.out.println(mons[i]);
                        }
                        for(int j = 0; j < validProperties.size(); j++) { //order players properties by set
                            if (validProperties.get(j).getColor() == i) {
                                System.out.print("  " + t + ". " + currPlayer.getProperties().get(j).getName());
                                if(validProperties.get(j).getMortgaged()) {
                                    System.out.println(" - mortgaged");
                                } else {
                                    System.out.println();
                                }
                                /*if(orderedProperties.get(j).getMortgaged()) {
                                    System.out.println("Mortgaged (unmortgage for $" + (int)((currPlayer.getProperties().get(j).getCost()/2)*((double)11/10)) + ")?");
                                } else {
                                    if(currPlayer.getProperties().get(j).getColor() != 9) {
                                        System.out.print("$" + currPlayer.getProperties().get(j).getCurrentRent() + " rent");
                                    } else {
                                        System.out.print(currPlayer.getProperties().get(j).getCurrentRent() + "x roll rent");
                                    }
                                    if(currPlayer.getProperties().get(j).getHouses() > 1 && currPlayer.getProperties().get(j).getHouses() != 5) {
                                        System.out.print(", " + currPlayer.getProperties().get(j).getHouses() + " houses");
                                    } else if (currPlayer.getProperties().get(j).getHouses() == 1) {
                                        System.out.print(", 1 house");
                                    }else if (currPlayer.getProperties().get(j).getHouses() == 5) {
                                        System.out.print(", 1 hotel");
                                    }
                                    System.out.println(" (mortgage for $" + (currPlayer.getProperties().get(j).getCost()/2) + "?)");
                                }*/
                                t++;
                                orderedProperties.add(validProperties.get(j));
                            }
                        }
                    }
                    input1 = scan.nextLine();
                    try { //see if there's only one number
                        if(Integer.parseInt(input1) > 0 && Integer.parseInt(input1) - 1 < currPlayer.getPropertyCount()) {
                            //System.out.println("response valid");scan.nextLine();
                            responseInvalid = false;
                        }
                    } catch (NumberFormatException e) {
                        if(input1 == "") {
                            responseInvalid = false;
                        }
                        /*System.out.println("response might be invalid");
                        scan.nextLine();*/
                        if(input1.contains(",")) {
                            /*System.out.println("response contains ,");
                            scan.nextLine();*/
                            while(input1.length() > 0) {
                                /*System.out.println("input: " + input1);
                                scan.nextLine();*/
                                if(input1.indexOf(",") != -1) { //still commas left
                                    String temp = input1.substring(0, input1.indexOf(","));
                                    try {
                                        Integer.parseInt(temp);
                                    } catch (NumberFormatException u) {
                                        temp = "-1";
                                    }
                                    input1 = input1.substring(input1.indexOf(",") + 1);
                                    propertiesGive.add(Integer.parseInt(temp));
                                } else {
                                    try {
                                        Integer.parseInt(input1);
                                    } catch (NumberFormatException u) {
                                        input1 = "-1";
                                    }
                                    propertiesGive.add(Integer.parseInt(input1));
                                    input1 = "";
                                }
                            }
                            //responseInvalid = false;
                        } else {
                            input1 = "0";
                        }
                    }
                    if(propertiesGive.size() > 0) {
                        int validCount = 0;
                        for(int i = 0; i < propertiesGive.size(); i++) { //check validity of propertiesGive
                            if(propertiesGive.get(i) > 0 && propertiesGive.get(i) - 1 < currPlayer.getPropertyCount() && propertiesGive.indexOf(propertiesGive.get(i)) == i) {
                                validCount++;
                            } else {
                                validCount = -25;
                            }
                        }
                        //System.out.println("validity " + validCount);scan.nextLine();
                        if(validCount == propertiesGive.size()) {
                            responseInvalid = false;
                        }
                    }
                }
                if(input1 == "") {input1 = "-1";}
                if(propertiesGive.size() == 0 && Integer.parseInt(input1) > 0 && Integer.parseInt(input1) -1 < validProperties.size()) {
                    propertiesGive.add(Integer.parseInt(input1));
                }
                for(int i = 0; i < propertiesGive.size(); i++) {
                    propertyGive.add(orderedProperties.get(propertiesGive.get(i) - 1));
                    //System.out.println("added " + propertyGive.get(i).getName());
                }

                /*if(propertyGive.size() > 0) {
                    currPlayer.giveProperties(propertyGive, tradeOptions.get(player));
                }*/
            } else {
                input1 = "0";
            }
            int giveGOOJF = 0;
            input1 = "-1";
            if(currPlayer.getGOOJF() > 0) {
                while(Integer.parseInt(input1) < 0 || Integer.parseInt(input1) > currPlayer.getGOOJF()) {
                    /*controller.updateView();*/controller.updateView();
                    System.out.println("You have " + currPlayer.getGOOJF() + " Get Out of Miege Free card(s). How many would you like to offer?");
                    input1 = scan.nextLine();
                    try {
                        Integer.parseInt(input1);
                    } catch (NumberFormatException f) {
                        input1 = "-1";
                    }
                }
                giveGOOJF = Integer.parseInt(input1);
            }

            //deal with request portion
            input1 = "-1";
            Player other = tradeOptions.get(player);
            if(other.getMoney() > 0) {
                while(Integer.parseInt(input1) < 0 || Integer.parseInt(input1) > other.getMoney()) {
                    /*controller.updateView();*/controller.updateView();
                    System.out.println("How much money would you like to request? (" + other.getName() + " has $" + other.getMoney() + ")");
                    input1 = scan.nextLine();
                    if(input1 == "") {
                        input1 = "-1";
                    }
                    if(input1.substring(0,1).equals("$")) {
                        input1 = input1.substring(1,input1.length());
                    }
                    try { //check if input is an integer, if not, just make it 0
                        Integer.parseInt(input1);
                    } catch (NumberFormatException e) {
                        input1 = "-1";
                    }
                }
            } else {
                input1 = "0";
            }
            int moneyRequest = Integer.parseInt(input1);
            ArrayList<Integer> propertiesRequest = new ArrayList<Integer>();
            ArrayList<Property> validPropertiesOther = new ArrayList<Property>();
            ArrayList<Property> orderedPropertiesOther = new ArrayList<Property>();
            //String[] mons = new String[]{"Purple","Light Blue","Pink","Orange","Red","Yellow","Green","Dark Blue", "Public Transport", "Utilities"};
            ArrayList<Integer> allColorsOther = new ArrayList<Integer>();
            ArrayList<Property> propertyRequest = new ArrayList<Property>();
            if(other.getPropertyCount() > 0) {
                Boolean responseInvalid = true;
                while(responseInvalid) {
                    validPropertiesOther.clear();
                    propertiesRequest.clear();
                    input1 = "-1";
                    /*controller.updateView();*/controller.updateView();
                    System.out.println("Would you like to request any properties? If not, just press enter.");
                    System.out.println("(If requesting multiple properties, separate them with commas, i.e. 2,4,7)");
                    for(int i = 0; i < other.getProperties().size(); i++) {
                        if(other.getMonopolyHouses(other.getProperties().get(i).getColor()) == 0) {
                            validPropertiesOther.add(other.getProperties().get(i));
                        }
                    }
                    for(int i = 0; i < validPropertiesOther.size(); i++) {
                        if(!allColorsOther.contains(validPropertiesOther.get(i).getColor())) {
                            allColorsOther.add(validPropertiesOther.get(i).getColor());
                        }
                    }
                    int t = 1;
                    for(int i = 0; i < 10; i++) { //cycle all monopolies
                        if(allColorsOther.contains(i)) {
                            System.out.println(mons[i]);
                        }
                        for(int j = 0; j < validPropertiesOther.size(); j++) { //order players properties by set
                            if (validPropertiesOther.get(j).getColor() == i) {
                                System.out.print("  " + t + ". " + other.getProperties().get(j).getName());
                                if(validPropertiesOther.get(j).getMortgaged()) {
                                    System.out.println(" - mortgaged");
                                } else {
                                    System.out.println();
                                }
                                /*if(orderedProperties.get(j).getMortgaged()) {
                                    System.out.println("Mortgaged (unmortgage for $" + (int)((currPlayer.getProperties().get(j).getCost()/2)*((double)11/10)) + ")?");
                                } else {
                                    if(currPlayer.getProperties().get(j).getColor() != 9) {
                                        System.out.print("$" + currPlayer.getProperties().get(j).getCurrentRent() + " rent");
                                    } else {
                                        System.out.print(currPlayer.getProperties().get(j).getCurrentRent() + "x roll rent");
                                    }
                                    if(currPlayer.getProperties().get(j).getHouses() > 1 && currPlayer.getProperties().get(j).getHouses() != 5) {
                                        System.out.print(", " + currPlayer.getProperties().get(j).getHouses() + " houses");
                                    } else if (currPlayer.getProperties().get(j).getHouses() == 1) {
                                        System.out.print(", 1 house");
                                    }else if (currPlayer.getProperties().get(j).getHouses() == 5) {
                                        System.out.print(", 1 hotel");
                                    }
                                    System.out.println(" (mortgage for $" + (currPlayer.getProperties().get(j).getCost()/2) + "?)");
                                }*/
                                t++;
                                orderedPropertiesOther.add(validPropertiesOther.get(j));
                            }
                        }
                    }
                    input1 = scan.nextLine();
                    try { //see if there's only one number
                        if(Integer.parseInt(input1) > 0 && Integer.parseInt(input1) - 1 < other.getPropertyCount()) {
                            //System.out.println("response valid");scan.nextLine();
                            responseInvalid = false;
                        }
                    } catch (NumberFormatException e) {
                        if(input1 == "") {
                            responseInvalid = false;
                        }
                        /*System.out.println("response might be invalid");
                        scan.nextLine();*/
                        if(input1.contains(",")) {
                            /*System.out.println("response contains ,");
                            scan.nextLine();*/
                            while(input1.length() > 0) {
                                /*System.out.println("input: " + input1);
                                scan.nextLine();*/
                                if(input1.indexOf(",") != -1) { //still commas left
                                    String temp = input1.substring(0, input1.indexOf(","));
                                    try {
                                        Integer.parseInt(temp);
                                    } catch (NumberFormatException u) {
                                        temp = "-1";
                                    }
                                    input1 = input1.substring(input1.indexOf(",") + 1);
                                    propertiesRequest.add(Integer.parseInt(temp));
                                } else {
                                    try {
                                        Integer.parseInt(input1);
                                    } catch (NumberFormatException u) {
                                        input1 = "-1";
                                    }
                                    propertiesRequest.add(Integer.parseInt(input1));
                                    input1 = "";
                                }
                            }
                            //responseInvalid = false;
                        } else {
                            input1 = "0";
                        }
                    }
                    if(propertiesRequest.size() > 0) {
                        int validCount = 0;
                        for(int i = 0; i < propertiesRequest.size(); i++) { //check validity of propertiesGive
                            if(propertiesRequest.get(i) > 0 && propertiesRequest.get(i) - 1 < other.getPropertyCount() && propertiesRequest.indexOf(propertiesRequest.get(i)) == i) {
                                validCount++;
                            } else {
                                validCount = -25;
                            }
                        }
                        //System.out.println("validity " + validCount);scan.nextLine();
                        if(validCount == propertiesRequest.size()) {
                            responseInvalid = false;
                        }
                    }
                }
                if(input1 == "") {input1 = "-1";}
                if(propertiesRequest.size() == 0 && Integer.parseInt(input1) > 0 && Integer.parseInt(input1) - 1 < validPropertiesOther.size()) {
                    propertiesRequest.add(Integer.parseInt(input1));
                }
                for(int i = 0; i < propertiesRequest.size(); i++) {
                    propertyRequest.add(orderedPropertiesOther.get(propertiesRequest.get(i) - 1));
                    //System.out.println("added " + propertyGive.get(i).getName());
                }

                /*if(propertyRequest.size() > 0) {
                    currPlayer.giveProperties(propertyRequest, tradeOptions.get(player));
                }*/
            } else {
                input1 = "0";
            }
            int requestGOOJF = 0;
            input1 = "-1";
            if(other.getGOOJF() > 0) {
                while(Integer.parseInt(input1) < 0 || Integer.parseInt(input1) > currPlayer.getGOOJF()) {
                    /*controller.updateView();*/controller.updateView();
                    System.out.println(other.getName() + " has " + other.getGOOJF() + " Get Out of Miege Free card(s). How many would you like to request?");
                    input1 = scan.nextLine();
                    try {
                        Integer.parseInt(input1);
                    } catch (NumberFormatException f) {
                        input1 = "-1";
                    }
                }
                requestGOOJF = Integer.parseInt(input1);
            }

            /*for(int i = 0; i < propertiesGive.size(); i++) {
                if(propertiesGive.get(i) - 1 >= 0 && propertiesGive.get(i) - 1 < currPlayer.getPropertyCount()) {
                    System.out.println(currPlayer.getProperties().get(propertiesGive.get(i) - 1).getName());
                    System.out.println("giving " + currPlayer.getProperties().get(propertiesGive.get(i) - 1).getName());scan.nextLine();
                    
                }
            }*/
            //scan.nextLine();
            
            /*if(Integer.parseInt(input1) != 0) {
                if(propertiesGive.size() > 0false) {
                    for(int i = 0; i < propertiesGive.size(); i++) {
                        System.out.println("giving " + currPlayer.getProperties().get(propertiesGive.get(i) - 1).getName());scan.nextLine();
                        currPlayer.giveProperty(currPlayer.getProperties().get(propertiesGive.get(i) - 1), tradeOptions.get(player));
                        //propertiesGive.remove(0);
                    }
                } else {
                    currPlayer.giveProperty(currPlayer.getProperties().get(Integer.parseInt(input1) - 1), tradeOptions.get(player));
                }
            }*/
            
            //ArrayList<Integer> propertyGive = new ArrayList<Integer>();


            //enact everything
            input1 = "-1";
            Boolean thisAgree = false;
            Boolean otherAgree = false;
            while(Integer.parseInt(input1) != 1 && Integer.parseInt(input1) != 2) {
                /*controller.updateView();*/controller.updateView();
                System.out.println(currPlayer.getName() + ", how does this look?");
                System.out.println("You give:");
                if(moneyGive > 0) {
                    System.out.println("  $" + moneyGive);
                }
                for(int i = 0; i < propertyGive.size(); i++) {
                    System.out.println("  " + propertyGive.get(i).getName());
                }
                if(giveGOOJF > 0) {
                    System.out.println("  " + giveGOOJF + " Get Out of Miege Free cards");
                }
                if(moneyGive == 0 && propertyGive.size() == 0 && giveGOOJF == 0) {
                    System.out.println("  nothing (strange...)");
                }
                System.out.println("You receive:");
                if(moneyRequest > 0) {
                    System.out.println("  $" + moneyRequest);
                }
                for(int i = 0; i < propertyRequest.size(); i++) {
                    System.out.println("  " + propertyRequest.get(i).getName());
                }
                if(requestGOOJF > 0) {
                    System.out.println("  " + requestGOOJF + " Get Out of Miege Free cards");
                }
                if(moneyRequest == 0 && propertyRequest.size() == 0 && requestGOOJF == 0) {
                    System.out.println("  nothing (hey, there's no charity in Monopoly)");
                }
                System.out.println("1. Agree\n2. Disagree");
                input1 = scan.nextLine();
                try {
                    Integer.parseInt(input1);
                } catch(NumberFormatException f) {
                    input1 = "-1";
                }
            }
            if(Integer.parseInt(input1) == 1) thisAgree = true;
            input1 = "-1";
            if(thisAgree) {
                while(Integer.parseInt(input1) != 1 && Integer.parseInt(input1) != 2) {
                    /*controller.updateView();*/controller.updateView();
                    System.out.println(other.getName() + ", how does this look?");
                    System.out.println("You give:");
                    if(moneyRequest > 0) {
                        System.out.println("  $" + moneyRequest);
                    }
                    for(int i = 0; i < propertyRequest.size(); i++) {
                        System.out.println("  " + propertyRequest.get(i).getName());
                    }
                    if(requestGOOJF > 0) {
                        System.out.println("  " + requestGOOJF + " Get Out of Miege Free cards");
                    }
                    if(moneyRequest == 0 && propertyRequest.size() == 0 && requestGOOJF == 0) {
                        System.out.println("  nothing (that's pretty selfish)");
                    }
                    System.out.println("You receive:");
                    if(moneyGive > 0) {
                        System.out.println("  $" + moneyGive);
                    }
                    for(int i = 0; i < propertyGive.size(); i++) {
                        System.out.println("  " + propertyGive.get(i).getName());
                    }
                    if(giveGOOJF > 0) {
                        System.out.println("  " + giveGOOJF + " Get Out of Miege Free cards");
                    }
                    if(moneyGive == 0 && propertyGive.size() == 0 && giveGOOJF == 0) {
                        System.out.println("  nothing (why?)");
                    }
                    System.out.println("1. Agree\n2. Disagree");
                    input1 = scan.nextLine();
                    try {
                        Integer.parseInt(input1);
                    } catch(NumberFormatException f) {
                        input1 = "-1";
                    }
                }
                if(Integer.parseInt(input1) == 1) otherAgree = true;
            }

            
            if(thisAgree && otherAgree) {
                if(propertyRequest.size() > 0) {
                    other.giveProperties(propertyRequest, currPlayer);
                }
                if(propertyGive.size() > 0) {
                    currPlayer.giveProperties(propertyGive, other);
                }
                currPlayer.giveMoney(other, moneyGive);
                other.giveMoney(currPlayer, moneyRequest);
                currPlayer.setGOOJF(currPlayer.getGOOJF() - giveGOOJF + requestGOOJF);
                other.setGOOJF(other.getGOOJF() + giveGOOJF - requestGOOJF);
            }
            

        } else if (option == 1) { //buy current property
            currPlayer.addProperty(getByPos(currPlayer.getPosition()));
            currPlayer.setMoney(currPlayer.getMoney() - getByPos(currPlayer.getPosition()).getCost());
        } else if (option == 2) { //view/mortgage properties
            System.out.println("Manage properties");
            String[] mons = new String[]{"Purple","Light Blue","Pink","Orange","Red","Yellow","Green","Dark Blue", "Public Transport", "Utilities"};
            ArrayList<Property> orderedProperties = new ArrayList<Property>();
            ArrayList<Integer> allColors = currPlayer.getAllColors();
            int e= 1;
            
            input = "-1";
            while(Integer.parseInt(input) <= 0 || Integer.parseInt(input) > orderedProperties.size()) {
                e = 1;
                controller.updateView();
                System.out.println(currPlayer.getName() + ": $" + currPlayer.getMoney());
                System.out.println("Select a property to mortgage or unmortgage, or press enter to go back.");
                System.out.println("");
                orderedProperties.clear();
                for(int i = 0; i < 10; i++) { //cycle all monopolies
                    if(allColors.contains(i)) {
                        System.out.println(mons[i]);
                    }
                    for(int j = 0; j < currPlayer.getPropertyCount(); j++) { //order players properties by set
                        if (currPlayer.getProperties().get(j).getColor() == i) {
                            System.out.print("  " + e + ". " + currPlayer.getProperties().get(j).getName() + " - ");
                            if(currPlayer.getProperties().get(j).getMortgaged()) {
                                System.out.println("Mortgaged (unmortgage for $" + (int)((currPlayer.getProperties().get(j).getCost()/2)*((double)11/10)) + ")?");
                            } else {
                                if(currPlayer.getProperties().get(j).getColor() != 9) {
                                    System.out.print("$" + currPlayer.getProperties().get(j).getCurrentRent() + " rent");
                                } else {
                                    System.out.print(currPlayer.getProperties().get(j).getCurrentRent() + "x roll rent");
                                }
                                if(currPlayer.getProperties().get(j).getHouses() > 1 && currPlayer.getProperties().get(j).getHouses() != 5) {
                                    System.out.print(", " + currPlayer.getProperties().get(j).getHouses() + " houses");
                                } else if (currPlayer.getProperties().get(j).getHouses() == 1) {
                                    System.out.print(", 1 house");
                                }else if (currPlayer.getProperties().get(j).getHouses() == 5) {
                                    System.out.print(", 1 hotel");
                                }
                                System.out.println(" (mortgage for $" + (currPlayer.getProperties().get(j).getCost()/2) + "?)");
                            }
                            e++;
                            orderedProperties.add(currPlayer.getProperties().get(j));
                        }
                    }
                }
                input = scan.nextLine();
                try { //check if input is an integer, if not, just make it 0
                    Integer.parseInt(input);
                } catch (NumberFormatException a) {
                    return;
                }
                //if(Integer.parseInt(input) == 0) input = "-1";
            }
            if(orderedProperties.get(Integer.parseInt(input) - 1).getMortgaged() && currPlayer.getMoney() >= (orderedProperties.get(Integer.parseInt(input) - 1).getCost()/2)*((double)11/10)) {
                currPlayer.unmortgageProperty(orderedProperties.get(Integer.parseInt(input) - 1));
                currPlayer.setMoney(currPlayer.getMoney() - (int)((orderedProperties.get(Integer.parseInt(input) - 1).getCost()/2)*((double)11/10)));
            } else if (!orderedProperties.get(Integer.parseInt(input) - 1).getMortgaged()) {
                currPlayer.mortgageProperty(orderedProperties.get(Integer.parseInt(input) - 1));
                currPlayer.setMoney(currPlayer.getMoney() + ((orderedProperties.get(Integer.parseInt(input) - 1).getCost())/2));
            }
            //for(int i = 0; i < orderedProperties.size(); i++) {
                //System.out.println((i + 1) + ". " + orderedProperties.get(i).getName());
            //}
            //scan.nextLine();
        } else if (option == 3) { //buy houses
            int j = 100;
            String[] mons = new String[]{"Purple","Light Blue","Pink","Orange","Red","Yellow","Green","Dark Blue"};
            ArrayList<Integer> availMonopolies = new ArrayList<Integer>();
            while (Integer.parseInt(input) < 1 || Integer.parseInt(input) > j) {
                controller.updateView();
                System.out.println("Buy houses for which monopoly?");
                j = 1;
                for(int i = 0; i < currPlayer.getMonopolies(); i++) {
                    if(((currPlayer.getAllMonopolies().get(i) > 0 && currPlayer.getAllMonopolies().get(i) < 7) && currPlayer.getMonopolyHouses(currPlayer.getAllMonopolies().get(i)) < 15 && currPlayer.getMoney() >= (((currPlayer.getAllMonopolies().get(i)) + 2)/2)*50) || ((currPlayer.getAllMonopolies().get(i) == 0 || currPlayer.getAllMonopolies().get(i) == 7) && currPlayer.getMonopolyHouses(currPlayer.getAllMonopolies().get(i)) < 10 && currPlayer.getMoney() >= (((currPlayer.getAllMonopolies().get(i)) + 2)/2)*50)) { //if monopoly has open houses make it an option
                        System.out.println(j + ". " + mons[currPlayer.getAllMonopolies().get(i)]);
                        availMonopolies.add(currPlayer.getAllMonopolies().get(i));
                        j++;
                    }
                }
                System.out.println(j + ". Go back");
                input = scan.nextLine();
                try { //check if input is an integer, if not, just make it 0
                        Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        input = "-1";
                    }
            }
            if(Integer.parseInt(input) == j) {
                return;
            }
            //int chosenMonopoly = currPlayer.getAllMonopolies().get(Integer.parseInt(input) - 1);
            int chosenMonopoly = availMonopolies.get(Integer.parseInt(input) - 1);
            //currPlayer.getAllMonopolies().get(chosenMonopoly);
            input = "-1";
            int maxAfford = (currPlayer.getMaxMonopolyHouses(chosenMonopoly) - currPlayer.getMonopolyHouses(option))*(((chosenMonopoly + 2)/2)*50); //start at all available houses
            while (currPlayer.getMoney() < maxAfford) {
                maxAfford -= ((chosenMonopoly + 2)/2)*50;
            }
            maxAfford /= 50;
            maxAfford /= (chosenMonopoly + 2)/2;
            while (Integer.parseInt(input) < 0 || Integer.parseInt(input) > maxAfford) {
                controller.updateView();
                System.out.println(currPlayer.getName() + " ($" + currPlayer.getMoney() + ") buying for " + mons[chosenMonopoly] + " monopoly:");
                System.out.println("How many houses would you like to buy? It will cost $" + ((chosenMonopoly + 2)/2)*50 + " for each house bought.");
                input = scan.nextLine();
                try { //check if input is an integer, if not, just make it 0
                        Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        input = "-1";
                    }
            }
            for (int i = Integer.parseInt(input) - 1; i >= 0 ; i--) {
                currPlayer.addHouse(chosenMonopoly);
                currPlayer.setMoney(currPlayer.getMoney() - (((chosenMonopoly + 2)/2)*50));
            }
        } else if (option == 4) { //sell houses
            int j = 100;
            String[] mons = new String[]{"Purple","Light Blue","Pink","Orange","Red","Yellow","Green","Dark Blue"};
            ArrayList<Integer> availMonopolies = new ArrayList<Integer>();
            while (Integer.parseInt(input) < 1 || Integer.parseInt(input) > j) {
                controller.updateView();
                System.out.println("Sell houses from which monopoly?");
                j = 1;
                for(int i = 0; i < currPlayer.getMonopolies(); i++) {
                    if(currPlayer.getMonopolyHouses(currPlayer.getAllMonopolies().get(i)) > 0) { //if monopoly has houses make it an option
                        System.out.println(j + ". " + mons[currPlayer.getAllMonopolies().get(i)]);
                        availMonopolies.add(currPlayer.getAllMonopolies().get(i));
                        j++;
                    }
                }
                System.out.println(j + ". Go back");
                input = scan.nextLine();
                try { //check if input is an integer, if not, just make it 0
                        Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        input = "-1";
                    }
            }
            if(Integer.parseInt(input) == j) {
                return;
            }
            //int chosenMonopoly = currPlayer.getAllMonopolies().get(Integer.parseInt(input) - 1);
            int chosenMonopoly = availMonopolies.get(Integer.parseInt(input) - 1);
            //currPlayer.getAllMonopolies().get(chosenMonopoly);
            input = "-1";
            while (Integer.parseInt(input) < 0 || Integer.parseInt(input) > currPlayer.getMonopolyHouses(chosenMonopoly)) {
                controller.updateView();
                System.out.println(currPlayer.getName() + " selling from " + mons[chosenMonopoly] + " monopoly:");
                System.out.println("How many houses would you like to sell? You will earn $" + ((chosenMonopoly + 2)/2)*25 + " for each house sold.");
                input = scan.nextLine();
                try { //check if input is an integer, if not, just make it 0
                        Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        input = "-1";
                    }
            }
            for (int i = 0; i < Integer.parseInt(input); i++) {
                currPlayer.removeHouse(chosenMonopoly);
            }


        } else if (option == 5) { //pay $50 GOOJ
            currPlayer.setJailed(false);
            currPlayer.setJailTurns(0);
            currPlayer.giveMoney(players.get(0), 50);
        } else if (option == 6) { //use GOOJF
            currPlayer.setJailed(false);
            currPlayer.setJailTurns(0);
            currPlayer.setGOOJF(currPlayer.getGOOJF() - 1);
        } else if (option == 7) { //end turn
            //System.out.println("end turn");scan.nextLine();
            return;
        }
    }

    public void executePos(Player player, int pos, Boolean doubleRent, Boolean card) {
        String input = "-1";
        Scanner scan = new Scanner(System.in);
        if ((pos == 4 || pos == 38)) { //tax
            Vector<String> taxes = new Vector<String>();
            taxes.add("Property Tax");taxes.add("Chiefs Tickets");
            controller.updateView();
            System.out.println(player.getName() + " landed on " + taxes.get((-4+pos)/34) + ", costing $" + (7300 - (pos*125))/34);
            System.out.println("Press enter to continue.");
            scan.nextLine();
            player.giveMoney(players.get(0), (7300 - (pos*125))/34);
        } else if (pos == 20 && players.get(0).getMoney() > 0) { //free parking
            controller.updateView();
            System.out.println(player.getName() + " found free parking downtown on a Saturday!");
            System.out.println("Go out and splurge with those newfound $" + players.get(0).getMoney() + ".");
            System.out.println("Press enter to continue.");
            scan.nextLine();
            players.get(0).giveMoney(player, players.get(0).getMoney());
        } else if (pos == 2 || pos == 17 || pos == 33 || pos == 7 || pos == 22 || pos == 36) {
            ChestOrChance(player);
        } /*else if (pos == 7 || pos == 22 || pos == 36) {
            Chance(player);
        } */else if (pos == 0 && !card) { //regular go money plus 300
            player.setMoney(player.getMoney() + 300);
        } else if (pos == 30) { //go to miege
            controller.updateView();
            System.out.println(player.getName() + " has been sent to Miege... Press enter to continue.");
            player.setJailed(true);
            scan.nextLine();
        } else if (pos == 12 || pos == 28) { //utilities
            System.out.println(getByPos(pos).getName());
            Property prop = getByPos(pos);
            int status = whoOwns(prop);
            /*if (status == -1 && player.getMoney() >= prop.getCost()) { // unowned & can afford
                while (Integer.parseInt(input) < 1 || Integer.parseInt(input) > 2) {
                    controller.updateView();
                    System.out.println(player.getName() + " just landed on " + prop.getName() + " ($" + prop.getCost() + ")! Buy it?");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    input = scan.nextLine();
                    try { //check if input is an integer, if not, just make it 0
                        Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        input = "-1";
                    }
                }
                if(Integer.parseInt(input) == 1) {
                    player.addProperty(prop);
                    player.setMoney(player.getMoney() - prop.getCost());
                }
            } else */if (status != player.getID() && status != -1 && !getByPos(pos).getMortgaged() && !doubleRent) { //owned by other
                controller.updateView();
                System.out.println(player.getName() + " just landed on " + players.get(whoOwns(prop)).getName() + "'s " + prop.getName() + "!");
                System.out.println(player.getName() + " paid " + players.get(whoOwns(prop)).getName() + " $" + (prop.getCurrentRent() * (die1 + die2)));
                System.out.println("Press enter to continue.");
                scan.nextLine();
                player.giveMoney(players.get(whoOwns(prop)),(prop.getCurrentRent() * (die1 + die2)));
            } else if(status != player.getID() && status != -1 && !getByPos(pos).getMortgaged() && doubleRent) {
                controller.updateView();
                System.out.println(player.getName() + " just landed on " + players.get(whoOwns(prop)).getName() + "'s " + prop.getName() + "!");
                System.out.println(player.getName() + " paid " + players.get(whoOwns(prop)).getName() + " $" + (10 * (die1 + die2)));
                System.out.println("Press enter to continue.");
                scan.nextLine();
                player.giveMoney(players.get(whoOwns(prop)),(10 * (die1 + die2)));
            }
        } else if (pos != 10) { //all properties (excluding just visiting)
            System.out.println(getByPos(pos).getName());
            Property prop = getByPos(pos);
            int status = whoOwns(prop);
            /*if (status == -1 && player.getMoney() >= prop.getCost()) { // unowned & can afford
                while (Integer.parseInt(input) < 1 || Integer.parseInt(input) > 2) {
                    controller.updateView();
                    System.out.println(player.getName() + " just landed on " + prop.getName() + " ($" + prop.getCost() + ")! Buy it?");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    input = scan.nextLine();
                    try { //check if input is an integer, if not, just make it 0
                        Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        input = "-1";
                    }
                }
                if(Integer.parseInt(input) == 1) {
                    player.addProperty(prop);
                    player.setMoney(player.getMoney() - prop.getCost());
                }
            } else */if (status != player.getID() && status != -1 && !getByPos(pos).getMortgaged()) { //owned by other
                controller.updateView();
                if(!doubleRent) {
                    System.out.println(player.getName() + " just landed on " + players.get(whoOwns(prop)).getName() + "'s " + prop.getName() + "!");
                    System.out.println(player.getName() + " paid " + players.get(whoOwns(prop)).getName() + " $" + prop.getCurrentRent());
                    System.out.println("Press enter to continue.");
                    scan.nextLine();
                    player.giveMoney(players.get(whoOwns(prop)),prop.getCurrentRent());
                } else {
                    System.out.println(player.getName() + " just landed on " + players.get(whoOwns(prop)).getName() + "'s " + prop.getName() + "!");
                    System.out.println(player.getName() + " paid " + players.get(whoOwns(prop)).getName() + " $" + prop.getCurrentRent() * 2);
                    System.out.println("Press enter to continue.");
                    scan.nextLine();
                    player.giveMoney(players.get(whoOwns(prop)),prop.getCurrentRent() * 2);
                }
            }
        }
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

    public void ChestOrChance(Player p) {
        Scanner scan = new Scanner(System.in);
        int card = (int)(Math.floor(Math.random() * 16));
        Card chosen;
        int pose = p.getPosition();
        if(pose == 2 || pose == 17 || pose == 33) {
            chosen = chests.get(card);
        } else  {
            chosen = chances.get(card);
        }
        controller.updateView();
        //System.out.println(p.getName() + " - $" + p.getMoney());
        if(chosen.getCoc() == 0) {
            System.out.print("Community Chest - ");
        } else {
            System.out.print("Chance - ");
        }
        System.out.println(chosen.getName());
        System.out.println(chosen.getDescription());
        System.out.println("Press enter to continue.");
        scan.nextLine();
        switch(chosen.getType()) {
            case 0: //gain money
                p.setMoney(p.getMoney() + chosen.getAmc());
                break;
            case 1: //go to jail
                p.setPosition(10);
                p.setJailed(true);
                break;
            case 2: //goojf
                p.setGOOJF(p.getGOOJF() + 1);
                break;
            case 3: //go to spot
                if(p.getPosition() < chosen.getAmc()) {
                    p.setPosition(chosen.getAmc());
                } else { //if they would pass go
                    p.setPosition(chosen.getAmc() + 40);
                }
                executePos(p, p.getPosition(), false, true);
                //p.setPosition(chosen.getAmc());
                break;
            case 4: //go to certain color property
                int pos = p.getPosition();
                for(int i = p.getPosition(); i < 41; i++) {
                    if(i == 40) i = 0;
                    if(getByPos(i).getColor() == chosen.getAmc()) {
                        pos = i;
                        break;
                    }
                }
                if(p.getPosition() < pos) {
                    p.setPosition(pos);
                } else {
                    p.setPosition(pos + 40);
                }
                executePos(p, p.getPosition(), true, false);
                break;
            case 5://back 3 spaces
                p.setPosition(p.getPosition() - 3);
                executePos(p, p.getPosition(), false, false);
                break;
            case 6: //pay money
                p.giveMoney(players.get(0), chosen.getAmc());
                break;
            case 7://pay each 50
                for(int i = 1; i < players.size(); i++) {
                    if(players.get(i).getID() != p.getID()) {
                        p.giveMoney(players.get(i), 50);
                    }
                }
                break;
            case 8://receive 50 from each
                for(int i = 1; i < players.size(); i++) {
                    if(players.get(i).getID() != p.getID()) {
                        players.get(i).giveMoney(p, 50);
                    }
                }
                break;
            case 9://street repairs (25/100 if chance, 40/115 if chest)
                int totalGiven = 0;
                for(int i = 0; i < p.getPropertyCount(); i++) {
                    if(p.getProperties().get(i).getHouses() == 5) { //property has hotel
                        if(chosen.getCoc() == 0) { //115 if chest
                            p.giveMoney(players.get(0), 115);
                            totalGiven += 115;
                        } else {
                            p.giveMoney(players.get(0), 100);
                        }
                    } else {
                        if(chosen.getCoc() == 0) { //115 if chest
                            p.giveMoney(players.get(0), p.getProperties().get(i).getHouses()*40);
                            totalGiven += p.getProperties().get(i).getHouses()*40;
                        } else {
                            p.giveMoney(players.get(0), p.getProperties().get(i).getHouses()*25);
                            totalGiven += p.getProperties().get(i).getHouses()*25;
                        }
                    }
                }
                controller.updateView();
                System.out.println("You gave $" + totalGiven + "! Press enter to continue");
                scan.nextLine();
                break;
            default:
                break;
        }

    }

    /*public void Chance(Player p) {
        int card = (int)(Math.floor(Math.random() * 16));
        Card chosen = chests.get(card);
    }*/

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
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

    Card cc1 = new Card(0,"Go to Miege", "You got expelled from Rockhurst.\nGo to Miege.\nDo not pass Go.\nDo not collect $200.", 1, -1);
    Card cc2 = new Card(0,"Advance to Go", "Collect $200!", 3, 0);
    Card cc3 = new Card(0,"Get Out of Miege Free", "This card may be kept (in case of Rockhurst expulsion) or traded.", 2, -1);
    Card cc4 = new Card(0,"Grand Opera Opening", "Each player pays you $50.", 8, -1);
    Card cc5 = new Card(0,"Street Repairs", "Pay $40 for each house, $115 for each hotel you own.", 9, -1);
    Card cc6 = new Card(0,"Stolen Goods", "Your car got broken into in Westport. Pay $50 to replace your lost goods.", 6, 50);
    Card cc7 = new Card(0,"Hospital Fees", "Pay $100 to the hospital.", 6, 100);
    Card cc8 = new Card(0,"School Tax", "Pay the $150 school tax.", 6, 150);
    Card cc9 = new Card(0,"Inheritance", "Your cool uncle left you $100 in his will!", 0, 100);
    Card cc10 = new Card(0,"Beauty Contest, 2nd Prize", "Receive $10 for your great looks.", 0, 10);
    Card cc11 = new Card(0,"Life Insurance", "Get $100 from your life insurance maturing.", 0, 100);
    Card cc12 = new Card(0,"Income Tax Refund", "Get $20 from their mistake.", 0, 20);
    Card cc13 = new Card(0,"Services", "Receive $25 for your services. What services? Who knows.", 0, 25);
    Card cc14 = new Card(0,"Stocks", "You gamed the market and got $45!", 0, 45);
    Card cc15 = new Card(0,"Bank Error", "Get $200 from the bank's mistake (just don't tell anyone).", 0, 200);
    Card cc16 = new Card(0,"Thanks Grandma", "Your grandma gave you $100 for Christmas! But it's summer...", 0, 100);
    Card c1 = new Card(1,"Go to Miege", "You got expelled from Rockhurst.\nGo to Miege.\nDo not pass Go.\nDo not collect $200.", 1, -1);
    Card c2 = new Card(1,"Chairman of the Board", "Pay each player $50 to get their vote.", 7, -1);
    Card c3 = new Card(1,"Get Out of Miege Free", "This card may be kept (in case of Rockhurst expulsion) or traded.", 2, -1);
    Card c4 = new Card(1,"General Repairs", "Pay $25 for each house, $100 for each hotel you own.", 9, -1);
    Card c5 = new Card(1,"Poor Tax", "Pay the $15 poor tax.", 6, 15);
    Card c6 = new Card(1,"Advance to Hickman Mills Drive", "If you pass Go, get $200.", 3, 11);
    Card c7 = new Card(1,"Take a ride on the Streetcar", "If you pass Go, get $200.", 3, 5);
    Card c8 = new Card(1,"Take a stroll on Ward Parkway", "Good luck...", 3, 39);
    Card c9 = new Card(1,"Advance to Go", "Collect $200!", 3, 0);
    Card c10 = new Card(1,"Advance to 75th Street", "Woo hoo!", 3, 24);
    Card c11 = new Card(1,"Bank Pays You Dividend of $50", "Congrats!", 0, 50);
    Card c12 = new Card(1,"Building Loan", "Collect $150 from your matured building loan.", 0, 150);
    Card c13 = new Card(1,"Advance to Nearest Utility", "Let's hope nobody owns it...", 4, 9);
    Card c14 = new Card(1,"Advance to the Nearest Public Transit", "If owned, pay double the rent.", 4, 8);
    Card c15 = new Card(1,"Advance to the Nearest Public Transit", "If owned, pay double the rent.", 4, 8);
    Card c16 = new Card(1,"Go Back 3 Spaces", "Good luck!", 5, -1);
    ArrayList<Card> chests = new ArrayList<Card>(Arrays.asList(cc1, cc2, cc3, cc4, cc5,cc6, cc7, cc8, cc9, cc10,cc11, cc12, cc13, cc14, cc15,cc16));
    ArrayList<Card> chances = new ArrayList<Card>(Arrays.asList(c1, c2, c3, c4, c5,c6, c7, c8, c9, c10,c11, c12, c13, c14, c15,c16));
    //chests.add(cc1);

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