package newproject.component;

public class TwinSeat extends Ticket{//inheritance
    public TwinSeat(char row, int column, double price, Movie movie){
        super(row, column, price, movie);
    }
    public String toString(){//polymorphic
        return ""+getRow()+getColumn()+"(Twin seat)";
    }
}
