class Bus {

    String busType;
    char[][] seater;
    Sleeper sleeper;
     Bus(String busType){
         this.busType = busType;
         seater = new char[5][3];
         sleeper = new Sleeper();
     }
}
