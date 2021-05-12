package util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/*
 * Адаптер (для JAXB) для преобразования между типом LocalDate и строковым
 * представлением даты в стандарте ISO 8601, например как '2012-12-03'.
 */
public class LocaleDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String s){
        return LocalDate.parse(s);
    }

    @Override
    public String marshal(LocalDate date) {
        return date.toString();
    }
}
