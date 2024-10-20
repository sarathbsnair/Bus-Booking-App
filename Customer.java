class Customer {
    String name;
    int age;
    String password;
    String gender;

    String seat;

    SleeperPair coordinates;

    Customer(String name, String password, int age,String gender){
        this.name = name;
        this.age = age;
        this.password = password;
        this.gender = gender;
    }
    Customer(String name, String gender, String seat){
        this.name = name;
        this.seat = seat;
        this.gender = gender;
    }
}
