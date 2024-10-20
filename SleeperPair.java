class SleeperPair {
    int row;
    int col;

    String seat;
    String deck;

    SleeperPair(String seat, String deck){
        this.seat = seat;
        this.deck = deck;
       char [] arr =  seat.toCharArray();
       char firstCharacter = arr[0];
       char secondCharacter = arr[1];
       String fChar = String.valueOf(firstCharacter);
       Integer sChar = Integer.valueOf(String.valueOf(secondCharacter));
        if(fChar.toLowerCase().equals("a")){
            this.col = 0;
        }
        if(fChar.toLowerCase().equals("b")){
            this.col = 1;
        }
        if(fChar.toLowerCase().equals("c")){
            this.col = 2;
        }
       if(deck.toLowerCase().equals("l")){
           this.deck = "l";
           if(sChar == 4){
               this.row = 0;
           }if(sChar == 5){
               this.row = 1;
           }if(sChar == 6){
               this.row = 3;
           }
       }else{
           this.deck = "u";
           if(sChar == 1){
                this.row = 0;
           }if(sChar == 2){
                this.row = 1;
           }if(sChar == 3){
                this.row = 3;
           }
       }
    }
}
