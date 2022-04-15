package ch.helvetia.vmsolutions.dlj.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

public class PersonOut {

    @Getter @Setter
    private Integer id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String firstName;
    @Getter @Setter
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date birthday;
    @Getter @Setter
    private boolean active;

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