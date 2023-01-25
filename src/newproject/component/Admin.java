package newproject.component;

import java.util.*;

public class Admin extends User{//inheritance
    Scanner app = new Scanner(System.in);

    public Admin(String name, String id, String pass, String email){
        super(name, id, pass, email);
    }

    public void createMovie(ArrayList<Movie> m){
        System.out.print("Insert movie title: ");
        String title = app.nextLine();
        System.out.print("Insert movie synopsis: ");
        String synopsis = app.nextLine();
        m.add(new Movie(title, synopsis));
    }

    public void deleteMovie(ArrayList<Movie> m){
        String yesNo="";
        int movieSize=m.size();
        if(movieSize>0){
            int i;
            for (i = 0;i<m.size();i++){
                System.out.printf("%d. %s\n", i+1, m.get(i).getTitle());
            }
            System.out.print("\nEnter your choice(0 to cancel): ");
            try{
            i = app.nextInt()-1;
            if(i==-1)
                return;
            }catch(InputMismatchException e){
                app.nextLine();
                System.out.println("Please enter integer only!Returning to previous menu...");
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException f){}
                return;
            }
            if(i<0||i>movieSize){
                System.out.println("Wrong choice!Returning to previous menu...");
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){}
                return;
            }
            int ticketSold=m.get(i).countTicketSold();
            if(ticketSold!=0){
                System.out.printf("They are %d ticket sold for this movie. Are you sure want to delete this movie(%s)(Y/N):", ticketSold, m.get(i).getTitle());
                yesNo=app.next();
            }
            if(yesNo.equalsIgnoreCase("N")){
                System.out.println("Returning to previous menu...");
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){}
                return;
            }
            m.remove(i);
            Movie.setMovieCount(Movie.getMovieCount()-1);
        }
        else System.out.println("No movie available!");
    }
}
