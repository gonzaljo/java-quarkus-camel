package ch.helvetia.vmsolutions.dlj.mapper;

import ch.helvetia.vmsolutions.dlj.dto.PersonIn;
import ch.helvetia.vmsolutions.dlj.dto.PersonOut;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface PersonMapper {

    @Mapping(target = "active", expression = "java(person.getActive().equals(\"Ja\") ? true : false)")
    PersonOut toPersonOut(PersonIn person);
}
