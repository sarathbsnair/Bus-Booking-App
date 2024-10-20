import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Customer> list = new ArrayList<>();
        list.add(new Customer("aaa", "111",25,"F"));
        list.add(new Customer("bbb", "222",61,"M"));
        list.add(new Customer("ccc", "333",22,"M"));
        list.add(new Customer("ddd", "444",36,"F"));
        Map<String, Bus> map = Utils.constructBus();
        System.out.println("Hello, Welcome to BUS-APP");
        System.out.println("Do you already have an account ?");
        System.out.println("If Yes, tap Y, else N");
        String res = sc.nextLine();
        boolean invalidString = true;
        while(invalidString){
            if(res.toLowerCase().equals("y")){
                //login
                System.out.println("Enter your name");
                String name = sc.nextLine();
                System.out.println("Enter your password");
                String password = sc.nextLine();
                if(Utils.verifyLogin(list, name, password)){
                  System.out.println("Successfully Logged in");
                  Utils.bookTickets(list, name,map, sc);
                }else{
                    if(list.size() == 0){
                        System.out.println("Invalid User - You got to Sign Up first");
                        list.add(Utils.createCustomer());
                        System.out.println("Successfully Signed up!");
                        Utils.handleLogin(list,map);
                    }else{
                        System.out.println("Invalid User - Try Again - If you have never Signed up - Do Sign up first");
                        System.out.println("Tap N for Signing Up or Y for continuing login");
                        res = sc.nextLine();
                        continue;
                    }
                }
                invalidString = false;
            }else if(res.toLowerCase().equals("n")){
                //signup
                Customer newCustomer = Utils.createCustomer();
                if(!Utils.validateCustomer(newCustomer,list)){
                    list.add(newCustomer);
                    System.out.println("Successfully Signed up!");
                    System.out.println();
                    Utils.bookTickets(list,newCustomer.name,map, sc);
                }else{
                    System.out.println("Already Signed Up!! - Tap Y for Login");
                    res = sc.nextLine();
                    continue;
                }
                  invalidString = false;
            }else{
                System.out.println("Invalid Prompt - Try again");
                res = sc.nextLine();
            }
        }
        sc.close();
    }
}
