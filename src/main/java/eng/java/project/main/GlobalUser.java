package eng.java.project.main;

import java.io.Serializable;

public class GlobalUser implements Serializable {
    private static GlobalUser instance;
    private String username;
    private String role;

    private GlobalUser() {}

    public static GlobalUser getInstance() {
        if(instance == null) {
            instance = new GlobalUser();
        }
        return instance;
    }

    public void setCredentials(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
