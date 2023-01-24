package newproject.component;

public class User {
    protected String name, id, pass, email;

    public User(String n, String i, String p, String e){
        name = n;
        id = i;
        pass = p;
        email = e;
        Customer.count++;
    }

    public User(){}

    public String getName(){
        return name;
    }

    public String getId(){
        return id;
    }

    public String getPass(){
        return pass;
    }

    public String getEmail(){
        return email;
    }
}
