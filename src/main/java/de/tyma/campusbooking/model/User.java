package de.tyma.campusbooking.model;

public class User {
    private String Name;
    private String EMail;
    private final int User_id;

    public User(String Name, String EMail, int User_id) {
        setName(Name);
        setEmail(EMail);
        this.User_id = User_id;
    }

    public String getName() {
        return this.Name;
    }

    public String getEmail() {
        return this.EMail;
    }

    public int getUser_id() {
        return this.User_id;
    }

    public void setName(String NewName) throws IllegalArgumentException {
        if(NewName == null || NewName.isBlank()) {
            throw new IllegalArgumentException("Invalid Name");
        }
        this.Name = NewName;
    }

    public void setEmail(String NewEmail) throws IllegalArgumentException {
        if(NewEmail == null || !NewEmail.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Invalid email adress");
        }
        this.EMail = NewEmail;
    }
}
