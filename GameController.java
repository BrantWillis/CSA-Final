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
        die1 = 0;
        die2 = 1;

        ArrayList<Integer> playerOptions = new ArrayList<Integer>();
        int[] options = new int[]{0,1,2,3,4,5,6};
        String input1 = "1";
        String input2 = "-1";
        String input3 = "-1";
        String[]optionNames = new String[]{"Offer a trade","Manage properties","Buy a house","Sell a house","Pay $50 to get out of jail","Use a get out of jail free card","End turn"};
        /*options:
        0 - offer a trade
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
            if(currPlayer.getMoney() >= 50) {
                playerOptions.add(4);
            }
            if(currPlayer.getGOOJF() > 0) {
                playerOptions.add(5);
            }
        }
        if(currPlayer.getMoney() >= 0) { //if not in debt can end turn
            playerOptions.add(6);
        }
        if(playerOptions.size() == 0) {
            players.remove(currPlayer);
            return;
        }
        if(!currPlayer.getJailed()) { //if player is in normal state
            currPlayer.setPosition(currPlayer.getPosition() + die1 + die2);

            if(currPlayer.getGo()) { //check if passed go
                currPlayer.setGo(false);
                currPlayer.setMoney(currPlayer.getMoney() + 200);
            }
            controller.updateView();
            executePos(currPlayer, currPlayer.getPosition()); //do actions for current square
            while(playerOptions.get(Integer.parseInt(input1) - 1) != 6) { //give options until player ends turn
                input1 = "-1";
                playerOptions.clear();
                if(currPlayer.getPropertyCount() > 0 || currPlayer.getMoney() > 0 || currPlayer.getGOOJF() > 0) { //can trade if has some stuff
                    playerOptions.add(0);
                }
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
                    if(currPlayer.getMoney() >= 50) {
                        playerOptions.add(4);
                    }
                    if(currPlayer.getGOOJF() > 0) {
                        playerOptions.add(5);
                    }
                }
                if(currPlayer.getMoney() >= 0) { //if not in debt can end turn
                    playerOptions.add(6);
                }
                if(playerOptions.size() == 0) {
                    players.remove(currPlayer);
                    return;
                }
                while(Integer.parseInt(input1) < 1 || Integer.parseInt(input1) > playerOptions.size()) {
                    controller.updateView();
                    System.out.println("Player " + currPlayer.getID() + ": $" + currPlayer.getMoney() + ", die1:" + die1 + ", die2:" + die2 + ", position:" + currPlayer.getPosition());
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
            

        } else { //if player is jailed
            while(playerOptions.get(Integer.parseInt(input1) - 1) != 6) { //give options until player ends turn
                input1 = "-1";
                playerOptions.clear();
                if(currPlayer.getPropertyCount() > 0 || currPlayer.getMoney() > 0 || currPlayer.getGOOJF() > 0) { //can trade if has some stuff
                    playerOptions.add(0);
                }
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
                    if(currPlayer.getMoney() >= 50) {
                        playerOptions.add(4);
                    }
                    if(currPlayer.getGOOJF() > 0) {
                        playerOptions.add(5);
                    }
                }
                if(currPlayer.getMoney() >= 0) { //if not in debt can end turn
                    playerOptions.add(6);
                }
                if(playerOptions.size() == 0) {
                    players.remove(currPlayer);
                    return;
                }
                currPlayer.setPosition(10); //jail position
                controller.updateView();
                executePos(currPlayer, currPlayer.getPosition()); //do actions for current square
                while(Integer.parseInt(input1) < 1 || Integer.parseInt(input1) > playerOptions.size()) {
                    controller.updateView();
                    System.out.println("Player " + currPlayer.getID() + ": $" + currPlayer.getMoney() + ", die1:" + die1 + ", die2:" + die2 + " JAILED - " + currPlayer.getJailTurns() + " turn(s) left" + ", position:" + currPlayer.getPosition());
                    for(int i = 0; i < playerOptions.size(); i++) { //display each option
                        System.out.println(i + 1 + ". " + optionNames[playerOptions.get(i)]);
                    }
                    input1= scan.nextLine();
                    try { //check if input is an integer, if not, just make it 0
                            Integer.parseInt(input1);
                        } catch (NumberFormatException e) {
                            input1 = "-1";
                        }
                }
                executeOptions(playerOptions.get(Integer.parseInt(input1) - 1), currPlayer);
            }
            //System.out.println("JAIL");
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
            controller.updateView();
            ArrayList<Player> tradeOptions = new ArrayList<Player>();
            String input1 = "-1";
            while(Integer.parseInt(input1) < 1 || Integer.parseInt(input1) > tradeOptions.size() + 1) {
                controller.updateView();
                tradeOptions.clear();
                for(int i = 1; i < players.size(); i++) {
                    if(players.get(i).getID() != currPlayer.getID() && (players.get(i).getUnmortgagedPropertyCount() > 0 || players.get(i).getMoney() > 0 || players.get(i).getGOOJF() > 0)) {
                        tradeOptions.add(players.get(i));
                    }
                }
                System.out.println("Offer a trade to who?");
                for(int i = 0; i < tradeOptions.size(); i++) {
                    System.out.println((i+1) + ". Player " + tradeOptions.get(i).getID());
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
                    controller.updateView();
                    System.out.println("How much money would you like to offer? ($0-$" + currPlayer.getMoney() + ")");
                    input1 = scan.nextLine();
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
            ArrayList<Integer> allColors = orderedProperties.getAllColors();
            if(currPlayer.getPropertyCount() > 0) {
                Boolean responseInvalid = true;
                while(responseInvalid) {
                    validProperties.clear();
                    propertiesGive.clear();
                    input1 = "-1";
                    controller.updateView();
                    System.out.println("Would you like to offer any properties?");
                    System.out.println("(If offering multiple properties, separate them with commas, i.e. 2,4,7)");
                    for(int i = 0; i < currPlayer.getProperties().size(); i++) {
                        if(currPlayer.getMonopolyHouses(currPlayer.getProperties().get(i).getColor()) == 0) {
                            validProperties.add(currPlayer.getProperties().get(i));
                        }
                    }
                    int t = 1;
                    for(int i = 0; i < 10; i++) { //cycle all monopolies
                        if(allColors.contains(i)) {
                            System.out.println(mons[i]);
                        }
                        for(int j = 0; j < validProperties.size(); j++) { //order players properties by set
                            if (validProperties.get(j).getColor() == i) {
                                System.out.println("  " + t + ". " + currPlayer.getProperties().get(j).getName());
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
                            System.out.println("response valid");scan.nextLine();
                            responseInvalid = false;
                        }
                    } catch (NumberFormatException e) {
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
            } else {
                input1 = "0";
            }
            for(int i = 0; i < propertiesGive.size(); i++) {
                if(propertiesGive.get(i) - 1 >= 0 && propertiesGive.get(i) - 1 < currPlayer.getPropertyCount()) {
                    System.out.println(currPlayer.getProperties().get(propertiesGive.get(i) - 1).getName());
                }
            }
            scan.nextLine();
            if(input1 == "") {
                input1 = "-1";
            }
            if(Integer.parseInt(input1) != 0) {
                if(propertiesGive.size() > 0) {
                    while(propertiesGive.size() > 0) {
                        currPlayer.giveProperty(currPlayer.getProperties().get(propertiesGive.get(0) - 1), tradeOptions.get(player));
                        propertiesGive.remove(0);
                    }
                } else {
                    currPlayer.giveProperty(currPlayer.getProperties().get(Integer.parseInt(input1) - 1), tradeOptions.get(player));
                }
            }
            ArrayList<Integer> propertyGive = new ArrayList<Integer>();
            

        } else if (option == 1) { //view/mortgage properties
            System.out.println("Manage properties");
            String[] mons = new String[]{"Purple","Light Blue","Pink","Orange","Red","Yellow","Green","Dark Blue", "Public Transport", "Utilities"};
            ArrayList<Property> orderedProperties = new ArrayList<Property>();
            ArrayList<Integer> allColors = currPlayer.getAllColors();
            int e= 1;
            
            input = "-1";
            while(Integer.parseInt(input) <= 0 || Integer.parseInt(input) > orderedProperties.size()) {
                e = 1;
                clearScreen();
                System.out.println("Player " + currPlayer.getID() + ": $" + currPlayer.getMoney());
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
        } else if (option == 2) { //buy houses
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
                System.out.println("Player " + currPlayer.getID() + " ($" + currPlayer.getMoney() + ") buying for " + mons[chosenMonopoly] + " monopoly:");
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
        } else if (option == 3) { //sell houses
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
                System.out.println("Player " + currPlayer.getID() + " selling from " + mons[chosenMonopoly] + " monopoly:");
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


        } else if (option == 4) { //pay $50 GOOJ
            currPlayer.setJailed(false);
            currPlayer.setJailTurns(0);
            currPlayer.giveMoney(players.get(0), 50);
        } else if (option == 5) { //use GOOJF
            currPlayer.setJailed(false);
            currPlayer.setJailTurns(0);
            currPlayer.setGOOJF(currPlayer.getGOOJF() - 1);
        } else if (option == 6) { //end turn
            System.out.println("end turn");
            return;
        }
    }

    public void executePos(Player player, int pos) {
        String input = "-1";
        Scanner scan = new Scanner(System.in);
        if ((pos == 4 || pos == 38)) { //tax
            Vector<String> taxes = new Vector<String>();
            taxes.add("Property Tax");taxes.add("Chiefs Tickets");
            controller.updateView();
            System.out.println("Player " + player.getID() + " landed on " + taxes.get((-4+pos)/34) + ", costing $" + (7300 - (pos*125))/34);
            System.out.println("Press enter to continue.");
            scan.nextLine();
            player.giveMoney(players.get(0), (7300 - (pos*125))/34);
        } else if (pos == 20) { //free parking
            controller.updateView();
            System.out.println("Player " + player.getID() + " found free parking downtown on a Saturday!");
            System.out.println("Go out and splurge with those newfound $" + players.get(0).getMoney() + ".");
            System.out.println("Press enter to continue.");
            scan.nextLine();
            players.get(0).giveMoney(player, players.get(0).getMoney());
        } else if (pos == 2 || pos == 17 || pos == 33) {
            Chest();
        } else if (pos == 7 || pos == 22 || pos == 36) {
            Chance();
        } else if (pos == 0) { //regular go money plus 300
            player.setMoney(player.getMoney() + 300);
        } else if (pos == 30) { //go to miege
            controller.updateView();
            System.out.println("Player " + player.getID() + " has been sent to Miege! How disappointing.");
            player.setJailed(true);
        } else if (pos == 12 || pos == 28) { //utilities
            System.out.println(getByPos(pos).getName());
            Property prop = getByPos(pos);
            int status = whoOwns(prop);
            if (status == -1 && player.getMoney() >= prop.getCost()) { // unowned & can afford
                while (Integer.parseInt(input) < 1 || Integer.parseInt(input) > 2) {
                    controller.updateView();
                    System.out.println("Player " + player.getID() + " just landed on " + prop.getName() + " ($" + prop.getCost() + ")! Buy it?");
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
            } else if (status != player.getID() && status != -1 && !getByPos(pos).getMortgaged()) { //owned by other
                controller.updateView();
                System.out.println("Player " + player.getID() + " just landed on Player " + whoOwns(prop) + "'s " + prop.getName() + "!");
                System.out.println("Player " + player.getID() + " paid Player " + whoOwns(prop) + "$" + (prop.getCurrentRent() * (die1 + die2)));
                System.out.println("Press enter to continue.");
                scan.nextLine();
                player.giveMoney(players.get(whoOwns(prop)),(prop.getCurrentRent() * (die1 + die2)));
            }
        } else if (pos != 10) { //all properties (excluding just visiting)
            System.out.println(getByPos(pos).getName());
            Property prop = getByPos(pos);
            int status = whoOwns(prop);
            if (status == -1 && player.getMoney() >= prop.getCost()) { // unowned & can afford
                while (Integer.parseInt(input) < 1 || Integer.parseInt(input) > 2) {
                    controller.updateView();
                    System.out.println("Player " + player.getID() + " just landed on " + prop.getName() + " ($" + prop.getCost() + ")! Buy it?");
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
            } else if (status != player.getID() && status != -1 && !getByPos(pos).getMortgaged()) { //owned by other
                controller.updateView();
                System.out.println("Player " + player.getID() + " just landed on Player " + whoOwns(prop) + "'s " + prop.getName() + "!");
                System.out.println("Player " + player.getID() + " paid Player " + whoOwns(prop) + " $" + prop.getCurrentRent());
                System.out.println("Press enter to continue.");
                scan.nextLine();
                player.giveMoney(players.get(whoOwns(prop)),prop.getCurrentRent());
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

    public void Chest() {

    }

    public void Chance() {

    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}