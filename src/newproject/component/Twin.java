package newproject.component;

public class Twin extends Ticket{
    public Twin(char row, int column, double price, Movie movie){
        super(row, column, price, movie);
    }
    public String toString(){
        return ""+getRow()+getColumn()+"(Twin seat)";
    }
}
