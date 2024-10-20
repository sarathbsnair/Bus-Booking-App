class SeaterPair {

    int row;
    int col;
    String seat;
    SeaterPair(int row, int col){
        this.row = row;
        this.col = col;
    }
    SeaterPair(String str){
        char [] arr = str.toCharArray();
        char char1 = arr[0];
        char char2 = arr[1];
        String character1 = String.valueOf(char1).toLowerCase();
        if(character1.equals("a")){
            this.col = 0;
        }else if(character1.equals("b") ){
            this.col = 1;
        } else if (character1.equals("c") ) {
            this.col = 2;
        }
        Integer character2 = Integer.valueOf(String.valueOf(char2));
        if(character2 == 1){
            this.row = 0;
        }
        if(character2 == 2){
            this.row = 1;
        }
        if(character2 == 3){
            this.row = 3;
        }
        if(character2 == 4){
            this.row = 4;
        }
    }
}
