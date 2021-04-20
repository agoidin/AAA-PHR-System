import java.io.Serializable;

public class UserRecord implements Serializable
{
    public int id;
    public String email;
    public Role role;
    public boolean shouldChangePassword;

    public UserRecord() {

    }

    public UserRecord(int id, String email, Role role, boolean shouldChangePassword) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.shouldChangePassword = shouldChangePassword;
    }
}
