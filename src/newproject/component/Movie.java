package newproject.component;

public class Movie {
    private final char[] row = {'A','B','C','D','E','F'};
    private String title, synopsis;
    private Ticket[] ticket = new Ticket[28];
    private static int movieCount;
    private double sale;

    public Movie(String title, String synopsis){
        int x = 0;
        for (int i=0;i<4;i++)
            for (int y=1;y<=5;y++){
                ticket[x]= new SingleSeat(row[i],y,20.00, this);//composition
                x++;
            }
        for (int i=4;i<6;i++)
            for (int z=1;z<=4;z++){
                ticket[x]=new TwinSeat(row[i], z, 45.00, this);//composition
                x++;
            }
        this.title = title;
        this.synopsis = synopsis;
        movieCount++;
    }

    public static int getMovieCount(){
        return movieCount;
    }

    public static void setMovieCount(int movieCount){
        Movie.movieCount = movieCount;
    }

    public void setTicket(Ticket[] ticket){
        this.ticket = ticket;
    }

    public String getTitle(){
        return title;
    }

    public String getSynopsis(){
        return synopsis;
    }

    public Ticket[] getTicket(){
        return ticket;
    }

    public double getSale(){
        return sale;
    }

    private String tV(Ticket t){//to validate ticket whether sold or not
        if(t.getCustomer() == null)
            return ""+t.getRow()+t.getColumn();
        else
            return "XX";
    }

    public void displayHall(){
        System.out.printf("""
            
            Movie name: %s""",title);
        System.out.printf("""

             __________________________
            |\\_________Screen_________/|
            |                          |
            | [%s] [%s] [%s] [%s] [%s] |
            | [%s] [%s] [%s] [%s] [%s] |
            | [%s] [%s] [%s] [%s] [%s] |
            | [%s] [%s] [%s] [%s] [%s] |
            | [ %s ][ %s ][ %s ][ %s ] |
            | [ %s ][ %s ][ %s ][ %s ] |
            |__________________________|

            Legend:
            XX - Sold
            [  ] - Single seat
            [    ] - Twin seat

            Please insert seat number or 0 to cancel(eg. A5):""",
            tV(ticket[0]), tV(ticket[1]), tV(ticket[2]), tV(ticket[3]), tV(ticket[4]), 
            tV(ticket[5]), tV(ticket[6]), tV(ticket[7]), tV(ticket[8]), tV(ticket[9]), 
            tV(ticket[10]), tV(ticket[11]), tV(ticket[12]), tV(ticket[13]), tV(ticket[14]), 
            tV(ticket[15]), tV(ticket[16]), tV(ticket[17]), tV(ticket[18]), tV(ticket[19]), 
            tV(ticket[20]), tV(ticket[21]), tV(ticket[22]), tV(ticket[23]), tV(ticket[24]), 
            tV(ticket[25]), tV(ticket[26]), tV(ticket[27]));
    }

    public int countTicketAvailable(){
        int ticketAvail = 0;
        for(int i=0;i<28;i++){
            boolean TICKET_AVAILABLE=ticket[i].getCustomer()==null;
            if(TICKET_AVAILABLE)
                ticketAvail++;
        }
        return ticketAvail;
    }

    public int countTicketSold(){
        int ticketSold = 0;
        for(int i=0;i<28;i++){
            boolean TICKET_SOLD=ticket[i].getCustomer()!=null;
            if(TICKET_SOLD)
                ticketSold++;
        }
        return ticketSold;
    }

    public void printReport(){
        System.out.printf("""
            Movie title         : %s
            Ticket sold         : %d
            Ticket available    : %d
                """, title, countTicketSold(),countTicketAvailable());
        if(countTicketSold()==0)
            return;
        System.out.print("""
            No  Customer Name           Customer ID Seat no.
            --  -------------           ----------- --------
                """);
        int y=1;
        for(int i=0;i<28;i++){
            if(ticket[i].getCustomer()!=null){
                System.out.printf("""
                    %d)  %-24s%-12s%s
                        """, y, ticket[i].getCustomer().getName(), ticket[i].getCustomer().getId(),ticket[i].toString());
            sale+=ticket[i].getPrice();
            y++;
            }
        }
        System.out.printf("Total sale: RM%.2f\n", sale);
    }
}
