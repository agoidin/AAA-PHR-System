import java.io.Serializable;

public class CreateUserClientMessage implements Serializable
{
    public String email;
    public String password;
    public Role role;
}