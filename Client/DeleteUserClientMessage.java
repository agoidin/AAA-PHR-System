import java.io.Serializable;

public class DeleteUserClientMessage implements Serializable
{
    public int id;
    public DeleteUserClientMessage() {}
    public DeleteUserClientMessage(int id) {
        this.id = id;
    }
}
