package library.component;

public abstract class Ticket {
    private final char row;
    private final int column;
    private double price;
    private Customer customer;
    private Movie movie;

    public Ticket(char row, int column, double price, Movie movie){
        this.row = row;
        this.column = column;
        this.price = price;
        this.movie = movie;
    }

    public char getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }
    public double getPrice(){
        return price;
    }

    public Customer getCustomer(){
        return customer;
    }

    public Movie getMovie(){
        return movie;
    }

    public void setCustomer(Customer c){
        customer = c;
    }

    public void setMovie(Movie m){
        movie = m;
    }

    public abstract String toString();
}
