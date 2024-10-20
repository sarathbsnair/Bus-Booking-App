import Model.Constants;

import java.util.*;

class Utils {
    static Customer createCustomer(){
        Scanner sc = new Scanner(System.in);
        System.out.println("SIGN UP!");
        System.out.println("Enter your name");
        String name = sc.nextLine();
        System.out.println("Enter your password");
        String password = sc.nextLine();
        System.out.println("Enter the age");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the Gender - M or F");
        String gender = sc.nextLine();
        return new Customer(name, password,age,gender);
    }

    static boolean verifyLogin(List<Customer> customerList, String name, String password){
        if(customerList.size() == 0){
            return false;
        }
        for(Customer customer : customerList){
            if(customer.name.equals(name) && customer.password.equals(password)){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    static void handleLogin(List<Customer> customerList, Map<String,Bus> map){
        Customer customer = customerList.get(0);
        Scanner sc = new Scanner (System.in);
        System.out.println("Landed Login Zone!");
        System.out.println("Enter the name");
        String name = sc.nextLine();
        System.out.println("Enter the password");
        String password = sc.next();
        if(name.equals(customer.name) && password.equals(customer.password)){
            System.out.println("Login Success");
            bookTickets(customerList, name, map, sc);
        }else{
            System.out.println("Invalid login Credentials !!!");
        }
    }

    static boolean verifyEnteredSeatsWithSelectedOption(Integer totalSeats, Integer option, Map<String,Bus> map){
        if(option == 1){
            if(totalSeats <= determineVacantSeats(map.get("AC").seater)) {
                return true;
            }
        }else if (option == 2) {
            if(totalSeats <= determineVacantSeats(map.get("NAC").seater)) {
                return true;
            }
        }else if(option == 3){
            if(totalSeats <= determineSleeperVacantSeats(map.get("AC").sleeper)) {
                return true;
            }
        }else{
            if(totalSeats <= determineSleeperVacantSeats(map.get("NAC").sleeper)) {
                return true;
            }
        }
        return false;
    }

    static void addCustomersAndBookSeat(List<Customer> list, Integer seats, Map<String,Bus>map, Integer option, Scanner sc){
        System.out.println("Enter the "+ seats + " passenger details");
        Integer seatsCopy = seats;
        sc.nextLine();
        while(seatsCopy>0){
            System.out.println("Enter the passenger name");
                String name = sc.nextLine();
            System.out.println("Enter the passenger gender");
                String gender = sc.nextLine();
            System.out.println("Enter the desired vacant seat (Do refer the layout for available seats)");
                String seat = sc.nextLine();
            SeaterPair coordinates = selectedSeaterSeatTransformation(seat);
            if(option == 1){
                boolean seatEligible = checkSeaterEligibility(coordinates.row,coordinates.col,map.get("AC").seater);
                while(!seatEligible){
                    System.out.println();
                    System.out.println("Not Eligible for the seat. Choose another...");
                    printSeaterBus(map.get("AC"));
                    String chosenSeat = sc.nextLine();
                    SeaterPair newCoordinates = selectedSeaterSeatTransformation(chosenSeat);
                    seatEligible = checkSeaterEligibility(newCoordinates.row,newCoordinates.col,map.get("AC").seater);
                }
                if(seatEligible){
                    Customer newCustomer = new Customer(name, gender, seat);
                    list.add(newCustomer);
                    bookSeat(coordinates.row,coordinates.col,map.get("AC").seater,newCustomer);
                }
            }else{
                boolean seatEligible = checkSeaterEligibility(coordinates.row,coordinates.col,map.get("NAC").seater);
                while(!seatEligible){
                    System.out.println("Not Eligible for the seat. Choose another...");
                    printSeaterBus(map.get("AC"));
                    String chosenSeat = sc.nextLine();
                    SeaterPair newCoordinates = selectedSeaterSeatTransformation(chosenSeat);
                    seatEligible = checkSeaterEligibility(newCoordinates.row,newCoordinates.col,map.get("NAC").seater);
                }
                if(seatEligible){
                    Customer newCustomer = new Customer(name, gender, seat);
                    list.add(newCustomer);
                    bookSeat(coordinates.row,coordinates.col,map.get("NAC").seater,newCustomer);
                }
            }
            seatsCopy--;
        }
        sc.close();
    }

    static void bookTickets(List<Customer> customerList, String name, Map<String,Bus> map, Scanner sc){
        List<Customer> newCustomerList = new ArrayList<>();
        System.out.println("Welcome to the tickets booking zone!");
        System.out.println();
        Bus acBus =  map.get("AC");
        Bus nonAcBus =  map.get("NAC");
        System.out.println("Loading Available Seat Status...");
        System.out.println();
        System.out.println("Select desired option from beneath - ");
        System.out.println();
        displaySeatOptions(map);
        String selectedOption = sc.nextLine();
        while(!selectedOption.equals("1") && !selectedOption.equals("2") && !selectedOption.equals("3") && !selectedOption.equals("4")){
            System.out.println("Invalid Option Selected!! Try again");
            selectedOption = sc.nextLine();
        }
        System.out.println("Enter the number of seats");
        Integer seats = sc.nextInt();
        boolean checkVerification = verifyEnteredSeatsWithSelectedOption(seats, Integer.valueOf(selectedOption),map);
        while(!checkVerification){
            System.out.println("Total Seats is more than what exists");
            System.out.println("Got to select another option from the ones beneath");
            displaySeatOptions(map);
            Integer chosenOption = sc.nextInt();
            sc.nextLine();
            System.out.println("How many Seats Required?");
            seats = sc.nextInt();
            sc.nextLine();
            checkVerification = verifyEnteredSeatsWithSelectedOption(seats, chosenOption,map);
        }
        if((selectedOption.equals("1")) || (selectedOption.equals("2"))){
            displaySeaterStructure(map, Integer.valueOf(selectedOption), sc);
            if(selectedOption.equals("1")){
                addCustomersAndBookSeat(newCustomerList, seats, map, 1,sc);
            }else{
                addCustomersAndBookSeat(newCustomerList, seats, map, 2,sc);
            }
            displaySeaterStructure(map, Integer.valueOf(selectedOption),sc);
        }else{
            //Sleeper
            displaySleeperStructure(map, selectedOption);
            if(selectedOption.equals("3")){
                addCustomerAndBookSleeperSeat(newCustomerList,seats,map, 3, sc);
            }else{
                addCustomerAndBookSleeperSeat(newCustomerList,seats,map, 4, sc);
            }
        }
        sc.close();
    }

    static void addCustomerAndBookSleeperSeat(List<Customer>customerList,Integer seats, Map<String,Bus> map, Integer option ,Scanner sc){
        System.out.println();
        System.out.println("Add the " + seats + " Passenger Details");
        //this is to close any previous buffer
        sc.nextLine();
        for(int i=0;i<seats;i++){
            System.out.println("Enter the passenger name");
            String name = sc.nextLine();
            System.out.println("Enter the passenger gender - M or F");
            String gender = sc.nextLine();
            System.out.println("Enter the seat");
            String seat = sc.nextLine();
            if(option == 3){
                mapSleeperSeatToCustomer(map.get("AC") ,new Customer(name,gender, seat), customerList, sc);
            }else{
                mapSleeperSeatToCustomer(map.get("NAC") ,new Customer(name,gender, seat), customerList, sc);
            }
        }
        constructSleeperLayout(customerList,map.get("AC"));
    }

    static void constructSleeperLayout(List<Customer> list, Bus bus){
        char [][] arr;
        for(Customer customer : list){
            SleeperPair coordinates = customer.coordinates;
            int row = coordinates.row;
            int col = coordinates.col;
            String deck = coordinates.deck;
            if(deck.equals("l")){
                arr = bus.sleeper.lower;
            }else{
                arr = bus.sleeper.upper;
            }
            arr[row][col] = customer.gender.toCharArray()[0];
        }
        printSleeperSeats(bus.sleeper);
    }

    static void displaySleeperStructure(Map<String, Bus> map, String option){
        Bus ins;
        switch(option){
        case "3":{
           //AC Sleeper
            ins = map.get("AC");
            printSleeperSeats(ins.sleeper);
           break;
        }
        case "4":{
           //NAC Sleeper
            ins = map.get("NAC");
            printSleeperSeats(ins.sleeper);
           break;
        }
        }
    }

    static void printSleeperSeats(Sleeper sleeper){
        char [][] upperDeck = sleeper.upper;
        char [][] lowerDeck = sleeper.lower;
        System.out.println();
        System.out.println("Vacant Seats Available = "+determineSleeperVacantSeats(sleeper));
        System.out.println();
        System.out.println("-upper-        -lower-");
        System.out.print("  A B");
        System.out.print("          ");
        System.out.println("  A B");
        for(int i =0; i< upperDeck.length;i++){
            if(i==2){
                System.out.print("-");
            }else{
                if(i==3){
                    System.out.print(i);
                }else{
                    System.out.print(i+1);
                }
            }
            for(int j =0;j<upperDeck[i].length;j++){
                if(i==2){
                    System.out.print(" " + "-");
                }else{
                    System.out.print(" " + upperDeck[i][j]);
                }
            }
            System.out.print("          ");
            if(i==2){
                System.out.print("-");
            }else{
                if(i==3){
                    System.out.print(i * 2);
                }else{
                    System.out.print(i+4);
                }
            }
            for(int j =0;j<lowerDeck[i].length;j++){
                if(i==2){
                    System.out.print(" " + "-");
                }else{
                    System.out.print(" " + lowerDeck[i][j]);
                }
            }
            System.out.println();
        }
    }

    static int determineSleeperVacantSeats(Sleeper sleeper){
        char[][] upperDeck = sleeper.upper;
        char[][] lowerDeck = sleeper.lower;
        return deckVaccantSeatsCount(upperDeck) + deckVaccantSeatsCount(lowerDeck);
    }

    static int deckVaccantSeatsCount(char[][] arr){
        int count = 0;
        for(int i =0;i<arr.length;i++){
            if(i==2){
                continue;
            }
            for(int j=0;j<arr[i].length;j++){
                if(arr[i][j] == 0){
                    count++;
                }
            }
        }
        return count;
    }

    static void printSeaterBus(Bus ins){
        System.out.println("Printing the Seater Bus Arrangement");
        System.out.println();
        char[][] arr = ins.seater;
        for(int i =0;i<arr.length;i++){
            if(i == 3){
                System.out.println();
                continue;
            }
            for(int j =0;j<arr[i].length;j++){
                System.out.print("|");
                System.out.print(arr[i][j]);
            }
            System.out.print("|");
            System.out.println();
        }
    }

    static void bookSeat(int row, int col, char [][] arr, Customer customer){
        arr[row][col] = customer.gender.toCharArray()[0];
    }

    static boolean validateCustomer(Customer customer, List<Customer> list){
        String name = customer.name;
        for(Customer c : list){
           if(c.name.equals(name)){
              return true;
           }
        }
        return false;
    }

    static Map<String,Bus> constructBus(){
        Map<String, Bus> map = new HashMap<>();
        map.put("AC", new Bus("AC"));
        map.put("NAC", new Bus("NAC"));
        return map;
    }

    static int determineVacantSeats(char[][] arr){
        int count =0;
        for(int i=0;i<arr.length;i++){
            if(i == 2){
                continue;
            }
            for(int j =0;j<arr[i].length;j++){
                if((arr[i][j] == 0)){
                    count ++;
                }
            }
        }
        return count;
    }

    static boolean checkSleeperEligibility(Sleeper sleeperBus){
        char[][] upper = sleeperBus.upper;
        char[][] lower = sleeperBus.lower;
        return false;
        //Remainder to be completed
    }

    static boolean checkSeaterEligibility(int row, int col, char [][] arr){
        if(row == 2){
            return false;
        }
        if(col<0 || col >2){
            System.out.println("Illogical !! Column does not exist");
            return  false;
        }
        if((row <= 4 && row >=0) && (row == 0 || row == 3)){
            if(arr[row+1][col] == 'F'){
               return false;
            }else{
                return true;
            }
        }else if((row <= 4 && row >=0) && (row == 1 || row == 4)){
            if(arr[row-1][col] == 'F'){
                return false;
            }else{
                return true;
            }
        }
        return false;
    }

    static Integer validateSeaterOption(Integer option){
        // only 1 or 2 returned
        Scanner sc = new Scanner(System.in);
        while(option != 1 && option != 2){
            System.out.println("Invalid Option Selected - Select valid option");
            option = sc.nextInt();
        }
        //sc.close();
        return option;
    }
    static void displaySeaterStructure(Map<String, Bus> map, Integer option, Scanner sc){
        //Scanner sc = new Scanner(System.in);
        int availableSeats;
        option = validateSeaterOption(option);
        while(option == 1 || option ==2){
            if(option == 1){
                Bus acBus = map.get("AC");
                availableSeats = determineVacantSeats(acBus.seater);
                if(availableSeats == 0){
                    System.out.println("This option is Invalid");
                    System.out.println("Choose Another Option");
                    System.out.println();
                    displaySeatOptions(map);
                    System.out.println("Select another option");
                    option = sc.nextInt();
                    option = validateSeaterOption(option);
                }else{
                    System.out.println();
                    System.out.println("Current Seat Stats = "+availableSeats);
                    printSeaterSeats(acBus);
                    option = 3;
                }
            }else{
                Bus nacBus = map.get("NAC");
                availableSeats = determineVacantSeats(nacBus.seater);
                if(availableSeats == 0){
                    System.out.println("This option is Invalid");
                    System.out.println("Choose Another Option");
                    System.out.println();
                    displaySeatOptions(map);
                    System.out.println("Select another option");
                    option = sc.nextInt();
                    option = validateSeaterOption(option);
                }else{
                    System.out.println();
                    System.out.println("Current Seat Stats = "+availableSeats);
                    printSeaterSeats(nacBus);
                    option = 3;
                }
            }
        }
    }

    static void displaySeatOptions(Map<String,Bus> map) {
        int availableSeatsACSeater = 0;
        int availableSeatsNonACSeater = 0;
        int availableSeatsACSleeper = 0;
        int availableSeatsNonACSleeper= 0;
        for (Map.Entry<String, Bus> entry : map.entrySet()) {
            Bus bus = map.get(entry.getKey());
            if (entry.getKey().equals("AC")) {
                availableSeatsACSeater = determineVacantSeats(bus.seater);
                availableSeatsACSleeper = determineSleeperVacantSeats(bus.sleeper);
                System.out.println("1.) AC - Seater = " + availableSeatsACSeater);
                System.out.println("3.) AC - Sleeper = "+ availableSeatsACSleeper);
            } else {
                availableSeatsNonACSeater = determineVacantSeats(bus.seater);
                availableSeatsNonACSleeper = determineSleeperVacantSeats(bus.sleeper);
                System.out.println("2.) Non AC - Seater = " + availableSeatsNonACSeater);
                System.out.println("4.) Non AC - Sleeper = "+availableSeatsNonACSleeper);
            }
        }
    }

    static boolean validateSeaterSeat(String seat){
        char [] arr = seat.toCharArray();
        char fChar = arr[0];
        char sChar = arr[1];
        if(fChar != 'a' && fChar != 'A' && fChar != 'b' && fChar != 'B' && fChar != 'c' && fChar != 'C'){
            return false;
        }else {
            Integer secondCharacter = Integer.valueOf(String.valueOf(sChar));
            if(secondCharacter != 1 && secondCharacter != 2 && secondCharacter != 3 && secondCharacter != 4){
                return false;
            }
            return true;
        }
    }

    static SeaterPair selectedSeaterSeatTransformation(String selectedSeat){
        while(!validateSeaterSeat(selectedSeat)){
            System.out.println("The selected seat is wrong!!! - Pick a valid one");
            Scanner sc = new Scanner(System.in);
            selectedSeat = sc.nextLine();
        }
        return new SeaterPair(selectedSeat);
    }

    static boolean validateSelectedSleeperSeat(String seat, Sleeper sleeper, Scanner sc){
        char [] arr = seat.toCharArray();
        if(arr.length != 2){
            return false;
        }
        char fChar = arr[0];
        char sChar = arr[1];
        if(!String.valueOf(fChar).equals("A") && !String.valueOf(fChar).equals("B") && !String.valueOf(fChar).equals("a") && !String.valueOf(fChar).equals("b")){
            return false;
        }
        Integer secondCharacter = Integer.valueOf(String.valueOf(sChar));
        if(secondCharacter>6 || secondCharacter<0){
            return false;
        }
        if(!secondaryAdjacentSeatValidation(seat, sleeper, sc)){
            return false;
        }
        return true;
    }

    static SleeperPair generateSleeperSeatCoordinates(String seat, Sleeper sleeper, Scanner sc){
        String deck;
        boolean validateSeat = validateSelectedSleeperSeat(seat, sleeper, sc);
        while(!validateSeat){
            System.out.println("Entered Seat Invalid - Try again from layout beneath");
            printSleeperSeats(sleeper);
            System.out.println();
            seat = sc.nextLine();
            validateSeat = validateSelectedSleeperSeat(seat,sleeper, sc);
        }
        Integer secondCharacter = Integer.valueOf(String.valueOf(seat.toCharArray()[1]));
        if(secondCharacter <=3 && secondCharacter >=1){
            deck = "u";
        }else{
            deck = "l";
        }
        return new SleeperPair(seat, deck);
    }

    static boolean secondaryAdjacentSeatValidation(String seat, Sleeper sleeper, Scanner sc){
        //control comes here only if it is a genuine seat
        String deck = determineDeck(seat);
        SleeperPair coordinates = new SleeperPair(seat, deck);
        int row = coordinates.row;
        int col = coordinates.col;
        return verifyAdjSeats(row, col, sleeper, deck);
    }

    static boolean verifyAdjSeats(int row, int col, Sleeper sleeper, String deck){
        char [][] arr;
        if(deck.toLowerCase().equals("u")){
            arr = sleeper.upper;
        }else{
            arr = sleeper.lower;
        }
        if(row == 3 && arr[row][col] != 0){
            return false;
        }
        if(row == 1 && arr[row-1][col] == 'F'){
            return false;
        }
        if(row == 0 && arr[row+1][col] == 'F'){
            return false;
        }
        return true;
    }

    static String determineDeck(String seat){
        String deck;
        Integer secondCharacter = Integer.valueOf(String.valueOf(seat.toCharArray()[1]));
        if(secondCharacter <=3 && secondCharacter >=1){
            deck = "u";
        }else{
            deck = "l";
        }
        return deck;
    }

    static void mapSleeperSeatToCustomer(Bus bus, Customer customer, List<Customer> list, Scanner sc){
        Sleeper sleeper = bus.sleeper;
        SleeperPair coordinates = generateSleeperSeatCoordinates(customer.seat,sleeper, sc);
        customer.coordinates = coordinates;
        list.add(customer);
    }

    static void printSeaterSeats(Bus bus){
        char [][] seaterBus = bus.seater;
        System.out.println("  A B C");
        for(int i =0;i<seaterBus.length;i++){
            if(i<2){
                System.out.print(i+1);
            }else if(i==2){
                System.out.print("-");
                for(int j =0;j<seaterBus[i].length;j++){
                    seaterBus[i][j] = '-';
                    System.out.print(" "+seaterBus[i][j]);
                }
                System.out.println();
                continue;
            }else{
                System.out.print(i);
            }
            for(int j =0;j<seaterBus[i].length;j++){
                System.out.print(" "+seaterBus[i][j]);
            }
            System.out.println();
        }
    }
}
