package newproject.component;

public class Single extends Ticket{
    public Single(char row, int column, double price, Movie movie){
        super(row, column, price, movie);
    }
    public String toString(){
        return ""+getRow()+getColumn()+"(Single seat)";
    }
}
