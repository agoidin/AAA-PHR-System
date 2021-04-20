import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogWriter
{
    String lastLogHash = "---";

    public void WriteExceptionToLog(Exception ex) {
        String dateString = LocalDate.now().toString();
        String dateTimeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try
        {
            File file = new File("logs/" + dateString + ".txt");
            file.createNewFile();

            Writer output;
            output = new BufferedWriter(new FileWriter("logs/" + dateString + ".txt", true));
            String log = dateTimeString + "|" + lastLogHash + "|ERROR|" + ex.toString() + "\n";
            output.append(log);
            lastLogHash = Serializer.getStringFromBytes(Hasher.getMessageHash(log));
            output.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void WriteEventToLog(String eventName, String data) {
        String dateString = LocalDate.now().toString();
        String dateTimeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try
        {
            File file = new File("logs/" + dateString + ".txt");
            file.createNewFile();

            Writer output;
            output = new BufferedWriter(new FileWriter("logs/" + dateString + ".txt", true));
            String log = dateTimeString + "|" + lastLogHash + "|EVENT|" + eventName + ":" + data + "\n";
            output.append(log);
            lastLogHash = Serializer.getStringFromBytes(Hasher.getMessageHash(log));
            output.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void WriteWarningToLog(String message) {
        String dateString = LocalDate.now().toString();
        String dateTimeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try
        {
            File file = new File("logs/" + dateString + ".txt");
            file.createNewFile();

            Writer output;
            output = new BufferedWriter(new FileWriter("logs/" + dateString + ".txt", true));
            String log = dateTimeString + "|" + lastLogHash + "|WARNING|" + message + "\n";
            output.append(log);
            lastLogHash = Serializer.getStringFromBytes(Hasher.getMessageHash(log));
            output.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
