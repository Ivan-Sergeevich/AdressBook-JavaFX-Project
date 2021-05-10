package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/*
 * Класс - обёртка для списка адресатов.
 * Используется для сохранения списка адресатов в XML.
 */

@XmlRootElement (name = "persons")
public class PersonListWrapper {

    private List<Person> persons;

    @XmlElement (name = "person")
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}
