import javax.crypto.SecretKey;
import java.io.Serializable;

public class ClientKeyExchangeMessage implements Serializable
{
    public SecretKey secretKey;
}
