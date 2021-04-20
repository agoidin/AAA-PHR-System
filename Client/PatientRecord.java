import java.io.Serializable;

public class PatientRecord implements Serializable
{
    public int id;
    public String firstname;
    public String surname;
    public String bloodGroup;
    public String dateOfBirth;
    public String allergies;
    public String disabilities;
    public String gp;

    public PatientRecord() {

    }

    public PatientRecord(int id,
                         String firstname,
                         String surname,
                         String bloodGroup,
                         String dateOfBirth,
                         String allergies,
                         String disabilities,
                         String gp) {
        this.id = id;
        this.firstname = firstname;
        this.surname = surname;
        this.bloodGroup = bloodGroup;
        this.dateOfBirth = dateOfBirth;
        this.allergies = allergies;
        this.disabilities = disabilities;
        this.gp = gp;
    }
}
