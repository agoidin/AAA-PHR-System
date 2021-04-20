import java.io.Serializable;

public class UpdatePasswordClientMessage implements Serializable
{
    public String newPassword;

    public UpdatePasswordClientMessage() {}
    public UpdatePasswordClientMessage(String newPassword) {
        this.newPassword = newPassword;
    }
}