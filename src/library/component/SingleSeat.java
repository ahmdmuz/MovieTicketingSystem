package library.component;

public class SingleSeat extends Ticket{//inheritance
    public SingleSeat(char row, int column, double price, Movie movie){
        super(row, column, price, movie);
    }
    public String toString(){//polymorphic
        return ""+getRow()+getColumn()+"(Single seat)";
    }
}
