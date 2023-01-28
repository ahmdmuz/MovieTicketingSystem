package newproject.component;

import java.util.ArrayList;

public class Customer extends User{//inheritance
    private String bDay;
    protected static int count;
    private ArrayList<Ticket> custTicket = new ArrayList<Ticket>();

    public Customer(String n, String id, String pass, String email, String bDay){//for existing user
        super(n, id, pass, email);
        this.bDay = bDay;
    }

    public Customer(String name, String pass, String email, String bDay){//for new user
        super.name = name;
        super.pass = pass;
        super.email = email;
        this.bDay = bDay;
        if (count < 10)
            super.id = "000" + Integer.toString(count);
            else if (count >= 10)
                super.id = "00" + Integer.toString(count);
                else super.id = "0" + Integer.toString(count);
        count++;
    }

    public ArrayList<Ticket> getCustTicket(){
        return custTicket;
    }

    public String getCustomerBirthDay(){
        return bDay;
    }

    public static int getCustomerCount(){
        return count;
    }

    public void buyTicket(Ticket ticket){
        custTicket.add(ticket);
    }

    public void displayTicketInfo(){
        double totalPrice=0;
        System.out.printf("""
            Customer name   : %s
            Customer ID     : %s
                """, name, id);
        if(custTicket.size()==0){
            System.out.println("No ticket booked yet.");
            return;
        }
        System.out.print("""
            No  Movie title             Seat no             Price(RM)
            --  -----------             -------             ---------
                """);
        for(int i=0;i<custTicket.size();i++){
            System.out.printf("%-4d%-24s%-20s%.2f\n", i+1, custTicket.get(i).getMovie().getTitle(), custTicket.get(i).toString(), 
            custTicket.get(i).getPrice());
            totalPrice += custTicket.get(i).getPrice();
        }
        System.out.printf("Total amount to be paid: RM%.2f\n", totalPrice);
    }
}