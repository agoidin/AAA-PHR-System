import java.io.Serializable;

public class SearchClientMessage implements Serializable
{
    public String search;

    public SearchClientMessage(String search) {
        this.search = search;
    }
}
