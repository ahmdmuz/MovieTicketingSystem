import java.util.*;
import java.io.*;

import library.component.*;
import library.component.Customer;

interface Scan{
    Scanner app = new Scanner(System.in);
}

public class MovieTicketingSystem{
    public static void main(String[] args){
        ArrayList<Movie> movie = new ArrayList<Movie>();            
        ArrayList<Customer> customer = new ArrayList<Customer>();   
        ArrayList<Admin> admin = new ArrayList<Admin>();            
        readFileCustomer(customer);
        readFileAdmin(admin);       
        readFileMovie(movie);       
        readFileTicket(movie, customer);
        int mainMenuChoice = 0;
        int custMenuChoice = 0;
        int adminMenuChoice = 0;
        int userIndex = 0;
        int adminIndex = 0;

        startloop:
        do{
            mainMenuChoice = choice();//Welcome Menu : 0 - Exit, 1 - Existing customer log in, 2 - New user create account, 3 - Admin login
            switch(mainMenuChoice){
                case 0 -> mainMenuChoice = 9;
                case 1 ->{userIndex=existingCustomer(customer, loginName(), loginPass());
                    if (userIndex==-1)
                        continue startloop;
                    do{
                        switch(custMenuChoice=custMainMenu()){  //Main menu for Customer : 1. Book ticket, 2. Display movie information
                                                                //3. Dislay booked movie, 4. Back, 5. Exit the system
                            case 1 -> {custMenuChoice=chooseMovie(movie, customer, userIndex);}
                            case 2 -> displayMovieList(movie);
                            case 3 -> customer.get(userIndex).displayTicketInfo();
                            case 4 -> custMenuChoice = 9;
                            case 5 -> {exitSystem(customer, movie);System.exit(0);}
                        }
                    }while(custMenuChoice != 9);}
                case 2 -> newCustomer(customer);
                case 3 ->{adminIndex = adminLogin(admin, loginName(), loginPass());
                    if (adminIndex == -1)
                        continue startloop;
                    do{
                        adminMenuChoice = adminMenu();  //Main menu for Admin : 1. Add movie, 2. Delete movie, 3. Display customer report
                                                        //4. Display movie report, 5. Back, 6. Exit the system
                        switch(adminMenuChoice){
                            case 1 -> admin.get(adminIndex).createMovie(movie);
                            case 2 -> admin.get(adminIndex).deleteMovie(movie);
                            case 3 -> displayCustomerReport(customer);
                            case 4 -> displayMovieReport(movie);
                            case 5 -> adminMenuChoice = 9;
                            case 6 -> {exitSystem(customer, movie);System.exit(0);}
                        }
                    }while(adminMenuChoice != 9);}
            }
        }while(mainMenuChoice != 9);

        exitSystem(customer, movie);
    }

    private static void readFileCustomer(ArrayList<Customer> c){//FileNotFoundException
        Scanner file;

        try{
        file = new Scanner(new File("library/inputOutput/Customer.txt"));
        }catch (FileNotFoundException e) {return;}

        while(file.hasNext()){
            String firstName = file.next();
            c.add(new Customer((firstName+""+file.nextLine()),file.next(),file.next(),file.next(),file.next()));
        }

        file.close();
    }

    private static void readFileAdmin(ArrayList<Admin> a){
        Scanner file;
        
        try{
        file = new Scanner(new File("library/inputOutput/Admin.txt"));
        }catch (FileNotFoundException e){return;}

        while(file.hasNext()){
            a.add(new Admin(file.next(), file.next(), file.next(), file.next()));
        }

        file.close();
    }

    private static void readFileMovie(ArrayList<Movie> m){
        Scanner file;

        try{
        file = new Scanner(new File("library/inputOutput/Movie.txt"));
        }catch (FileNotFoundException e) {return;}

        while(file.hasNext()){
            m.add(new Movie(file.nextLine(), file.nextLine()));
        }

        file.close();
    }

    private static void readFileTicket(ArrayList<Movie> m, ArrayList<Customer> c){
        Scanner file;
        int movieSize = m.size();
        int custSize = c.size();
        for(int i=0;i < movieSize;i++){
            Ticket[] ticket = new Ticket[28];
            try{
            file = new Scanner(new File(String.format("library/inputOutput/%sTicket.txt", (m.get(i).getTitle()).replaceAll("\\s.*", ""))));
            }catch (FileNotFoundException e) {continue;}
            String s,n;
            for(int y=0;y<20;y++){
                s = file.next();
                n = file.nextLine().trim();
                if (n.equals("<available>"))
                    ticket[y] = new SingleSeat(s.charAt(0),Integer.parseInt(s.substring(1)),20.00, m.get(i));
                else {
                    ticket[y] = new SingleSeat(s.charAt(0),Integer.parseInt(s.substring(1)),20.00, m.get(i));
                    for(int x=0;x<custSize;x++)
                        if(c.get(x).getId().equals(n)){
                            ticket[y].setCustomer(c.get(x));
                            c.get(x).buyTicket(ticket[y]);
                        }
                }
            }
            for(int y=20;y<28;y++){
                s = file.next();
                n = file.nextLine().trim();
                if (n.equals("<available>"))
                    ticket[y] = new TwinSeat(s.charAt(0),Integer.parseInt(s.substring(1)),45.00, m.get(i));
                else {
                    ticket[y] = new TwinSeat(s.charAt(0),Integer.parseInt(s.substring(1)),45.00, m.get(i));
                    for(int x=0;x<custSize;x++)
                        if(c.get(x).getId().equals(n)){
                            ticket[y].setCustomer(c.get(x));
                            c.get(x).buyTicket(ticket[y]);
                        }
                }
            }
            m.get(i).setTicket(ticket);
            file.close();
        }
        
    }

    private static void writeFileCustomer(ArrayList<Customer> c){
        PrintWriter out;
        int custSize=c.size();
        try{
            out = new PrintWriter("library/inputOutput/Customer.txt");
            for (int i=0;i<custSize;i++)
                out.printf("%s\n%-5s%-15s%-25s%-15s\n",c.get(i).getName(),c.get(i).getId(),c.get(i).getPass(),
                c.get(i).getEmail(),c.get(i).getCustomerBirthDay());
            out.close();
        }catch(FileNotFoundException e){}

        double totalPrice=0;
        for(int i=0;i<custSize;i++){
            try{
                out = new PrintWriter(new FileWriter(String.format("library/inputOutput/customerOutput/%s.txt", c.get(i).getId())));
                out.printf("""
                    Customer name   : %s
                    Customer ID     : %s
                        """, c.get(i).getName(), c.get(i).getId());
                if(c.get(i).getCustTicket().size()==0){
                    out.println("No ticket booked yet.");
                    continue;
                }
                out.print("""
                    No  Movie title             Seat no             Price(RM)
                    --  -----------             -------             ---------
                        """);
                for(int y=0;y<c.get(i).getCustTicket().size();y++){
                    out.printf("%-4d%-24s%-20s%.2f\n", y+1, c.get(i).getCustTicket().get(y).getMovie().getTitle(), c.get(i).getCustTicket().get(y).toString(), 
                    c.get(i).getCustTicket().get(y).getPrice());
                    totalPrice += c.get(i).getCustTicket().get(y).getPrice();
                }
                out.printf("Total amount to be paid: RM%.2f\n", totalPrice);
                out.close();
            }catch(IOException e){}
        }
    }

    private static void writeFileMovie(ArrayList<Movie> m){
        try{
        PrintWriter out = new PrintWriter("library/inputOutput/Movie.txt");
        for (int i=0;i<m.size();i++){
            out.printf("%s\n", m.get(i).getTitle());
            out.printf("%s\n", m.get(i).getSynopsis());
        }
        out.close();
        }catch(FileNotFoundException e){}
    }

    private static void writeFileTicket(ArrayList<Movie> m){
        PrintWriter out;
        for (int i=0;i<m.size();i++){
            Ticket[] ticket = new Ticket[28];
            ticket = m.get(i).getTicket();
            try{
            out = new PrintWriter(String.format("library/inputOutput/%sTicket.txt", (m.get(i).getTitle()).replaceAll("\\s.*", "")));
            for(int y=0;y<28;y++){
                if (ticket[y].getCustomer()==null)
                    out.printf("%s %s\n", ticket[y].getRow()+""+ticket[y].getColumn(), "<available>");
                else out.printf("%s %s\n", ticket[y].getRow()+""+ticket[y].getColumn(), ticket[y].getCustomer().getId());
            }
            out.close();
            }catch(FileNotFoundException e){}
        }
    }

    private static int choice(){//NumberFormatException
        int i;
        System.out.print("""

        ***** Welcome to Movie Ticketing System *****

        1. Existing user
        2. New user
        3. Admin

        Please insert your choice(0 to exit):""");
        try{
            i=Integer.parseInt(Scan.app.next());
        }catch(NumberFormatException e){
            System.out.println("Please enter integer only!Try again...");
            try{
                Thread.sleep(1000);
            }catch(InterruptedException f){}
            return -1;
            }
            if(i<0||i>3){
                System.out.println("Wrong input!Please enter number within range shown.Try again...");
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException f){}
            }
        Scan.app.nextLine();
        return i;
    }

    private static boolean validateInputString(String input){
        try{
            Integer.parseInt(input);
            return true;
        }catch (NumberFormatException e){
            System.out.println(input + " is not an integer number! Please insert integer number only. Try again...");
            try{
            Thread.sleep(1000);
        }catch (InterruptedException f){}
            return false;
        }
    }

    private static boolean promptExit(int input){
            System.out.print("Are you sure want to exit? All change(s) will be saved.(Y/N): ");
        if (Scan.app.next().equalsIgnoreCase("Y"))//if user type y or Y, the system will exit
            return true;
        else {                                                  //if user type other than y or Y, the system will resume
            System.out.print("System will return to main menu...");
            try{
                Thread.sleep(1000);
            }catch (InterruptedException f){}
            System.out.println();
            return false;
        }
    }

    private static String loginName(){
        System.out.print("Please insert name: ");
        return Scan.app.nextLine();
    }

    private static String loginPass(){
        System.out.print("Please insert password: ");
        return Scan.app.nextLine();
    }

    private static int existingCustomer(ArrayList<Customer> c, String name, String pass){
        for (int i=0;i<c.size();i++)
            if (name.equals(c.get(i).getName()) && pass.equals(c.get(i).getPass())){
                System.out.println("Login successful!");
                return i;
            }
        System.out.println("Login failed! Try again...");
        return -1;
    }

    private static int adminLogin(ArrayList<Admin> a, String name, String pass){
        for (int i=0;i<a.size();i++)
            if (name.equals(a.get(i).getName()) && pass.equals(a.get(i).getPass())){
                System.out.println("Login successful!");
                return i;
            }
        System.out.println("Login failed! Try again...");
        return -1;
    }

    private static void newCustomer(ArrayList<Customer> c){
        System.out.print("Please insert name: ");
        String name = Scan.app.nextLine();
        System.out.print("Please insert password: ");
        String pass = Scan.app.nextLine();
        System.out.print("Please insert email address: ");
        String email = Scan.app.nextLine();
        System.out.print("Please insert Birth date: ");
        String bDay = Scan.app.nextLine();

        c.add(new Customer(name,pass,email,bDay));//polymorphic

        System.out.println("New user added!");
    }

    private static int custMainMenu(){
        int i;
        boolean exit=false;
        System.out.print("""

            *-------------------------------*
            |           MAIN MENU           |
            *-------------------------------*
            1. Book ticket
            2. Display movie available
            3. Dislay booked movie
            4. Back
            5. Exit the system

            Please insert your choice:""");
            try{
            i = Integer.parseInt(Scan.app.next());
            }catch(NumberFormatException e){
                System.out.println("Please enter integer only!Try again...");
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException f){}
                return -1;
            }
            if(i==5){
                exit=promptExit(i);
                if(exit==false)
                    return -1;}
            if(i<1||i>5){
                System.out.println("Wrong input!Please enter number within range shown.Try again...");
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){}
            }
        return i;
    }

    private static int adminMenu(){
        int i;
        System.out.print("""

            1. Add movie
            2. Delete movie
            3. Display customer report
            4. Display movie report
            5. Back
            6. Exit the system

            Choice:""");
        i = Scan.app.nextInt();
        Scan.app.nextLine();
        return i;
    }

    private static void displayMovieList(ArrayList<Movie> m){
        System.out.println("\n***** MOVIE TITLE *****\n");
        if(m.size()>0){
            int i;
            for (i = 0;i<m.size();i++){
                System.out.printf("%d. %s\n", i+1, m.get(i).getTitle());
                System.out.printf("%s\n\n", m.get(i).getSynopsis());
            }
        }
        else System.out.println("No movie available!");
    }

    private static int validateMovieChoice(int i, ArrayList<Movie> o){
        try{
            i = Scan.app.nextInt()-1;
            }catch(InputMismatchException e){
                Scan.app.next();
                System.out.printf("Please enter integer only!Returning to previous menu...");
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException f){}
                return -1;
            }
            if(i<o.size())
                return i;
            else{
                System.out.printf("Please choose within number display only!Returning to previous menu...");
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException f){}
                return -1;
            }
    }

    private static int chooseMovie(ArrayList<Movie> m, ArrayList<Customer> c, int userIndex){
        int movieSize=m.size();
        System.out.println("\n***** MOVIE TITLE *****\n");
        if(movieSize>0){
            int movieIndex=0;
            for (int i = 0;i<movieSize;i++){
                System.out.printf("%d. %s\n", i+1, m.get(i).getTitle());
            }
            System.out.print("\nEnter your choice(eg. 1, 0 to cancel): ");
            movieIndex = validateMovieChoice(movieIndex, m);
            if(movieIndex>-1)
                buyTicket(m, c, userIndex, movieIndex);
        }
        else System.out.println("No movie available!");
        return -1;
    }

    private static void buyTicket(ArrayList<Movie> m, ArrayList<Customer> c, int userIndex, int movieIndex){
        Movie movie=m.get(movieIndex);
        Customer customer=c.get(userIndex);
        if(movie.countTicketAvailable()==0)
            return;
        String choice = null;
        movie.displayHall();
        choice = Scan.app.next();
        while(!choice.equals("0")){
        for(int i=0;i<28;i++){
            boolean CHOICE_EQUAL_TICKET_AVAILABLE=(""+movie.getTicket()[i].getRow()+movie.getTicket()[i].getColumn()).equalsIgnoreCase(choice);
            if(CHOICE_EQUAL_TICKET_AVAILABLE){
                movie.getTicket()[i].setCustomer(customer);
                customer.buyTicket(movie.getTicket()[i]);
                System.out.println("Ticket added to checkout");
                return;
            }
        }
        System.out.println("Wrong choice! Please try again...");
        System.out.print("Please insert seat number or 0 to cancel(eg. A5):");
        choice = Scan.app.next();
        }
    }

    private static void exitSystem(ArrayList<Customer> customer, ArrayList<Movie> movie){
        try{
        writeFileCustomer(customer); 
        writeFileMovie(movie); 
        writeFileTicket(movie);
        }catch(Exception e){
            System.out.println(e);
            System.exit(0);
        }
    }

    private static void displayMovieReport(ArrayList<Movie> m){
        String choice = "-1";
        int movieSize = m.size();
        if(movieSize>0){
            loop:
            do{
        System.out.print("""

            Please select which movie you want to display the report:

                """);
        for (int i=0;i<movieSize;i++){
            System.out.printf("""
            %d) %s
                    """, i+1, m.get(i).getTitle());
        }
        System.out.print("\nYour choice(0 to cancel, type 'all' to print all movie report): ");
        choice = Scan.app.next();
        if(choice.equals("0"))
            return;
        if(choice.equals("all")){
            double totalSale=0;
            System.out.printf("""

                    ----------------------------------
                        Sale report for all Movies  
                    ----------------------------------
                    No of movie: %d
                    """, Movie.getMovieCount());
            for(int i=0;i<movieSize;i++){
                System.out.printf("""

                    %d.
                    """, i+1);
                m.get(i).printReport();
                totalSale+=m.get(i).getSale();
            }
            System.out.printf("\nGrand total sale: RM%,.2f\n", totalSale);
            return;
        }
        if(validateInputString(choice)==false)
            continue loop;
        if(!choice.equals("0")&&Integer.parseInt(choice)<=movieSize){
            m.get(Integer.parseInt(choice)-1).printReport();
            return;
        }
        else{
            System.out.println("Wrong choice, please try again");
            choice = "-1";
        }
    }while(!choice.equals("0"));
        }else System.out.println("No movie available!");
    }

    private static void displayCustomerReport(ArrayList<Customer> c){
        String choice="-1";
        int custSize=c.size();
        int ticketBought=0;
        if(custSize>0){
            loop:
            do{
        System.out.print("""

            Please select which customer you want to display the report:
            No Name                 ID
            -- ----                 ----
                """);
        for (int i=0;i<custSize;i++){
            System.out.printf("""
            %d) %-21s%s
                    """, i+1, c.get(i).getName(), c.get(i).getId());
        }
        System.out.print("\nYour choice(0 to cancel, type 'all' to print all customer report): ");
        choice = Scan.app.next();
        if(choice.equals("0"))
            return;
        if(choice.equals("all")){
            double totalSale=0;
            System.out.printf("""

                    ------------------------------------
                        Sale report for all Customer  
                    ------------------------------------
                    No of customer: %d
                    """, Customer.getCustomerCount()-1);
            for(int i=0;i<custSize;i++){
                System.out.printf("""

                    %d.
                    """, i+1);
                c.get(i).displayTicketInfo();;
                ticketBought=c.get(i).getCustTicket().size();
                for(int y=0;y<ticketBought;y++){
                    totalSale+=c.get(i).getCustTicket().get(y).getPrice();
                }
            }
            System.out.printf("\nGrand total sale: RM%,.2f\n", totalSale);
            return;
        }
        if(validateInputString(choice)==false)
            continue loop;
        if(!choice.equals("0")&&Integer.parseInt(choice)<=custSize){
            c.get(Integer.parseInt(choice)-1).displayTicketInfo();
            return;
        }
        else{
            System.out.println("Wrong choice, please try again");
            choice = "-1";
        }
    }while(!choice.equals("0"));
        }else System.out.println("No customer available!");
    }
}