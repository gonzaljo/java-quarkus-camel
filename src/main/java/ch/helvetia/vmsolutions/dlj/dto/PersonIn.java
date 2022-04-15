package ch.helvetia.vmsolutions.dlj.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.util.Date;

@CsvRecord(separator = ";", skipFirstLine = true)
public class PersonIn {

    @Getter @Setter
    @DataField(pos = 1)
    private Integer id;
    @Getter @Setter
    @DataField(pos = 2)
    private String name;
    @Getter @Setter
    @DataField(pos = 3)
    private String firstName;
    @Getter @Setter
    @DataField(pos = 4, pattern = "dd.mm.yyyy")
    private Date birthday;
    @Getter @Setter
    @DataField(pos = 5)
    private String active;

    @Override
    public String toString() {
        return "PersonIn{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", birthday=" + birthday +
                ", active='" + active + '\'' +
                '}';
    }
}
