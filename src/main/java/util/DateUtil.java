package util;

// Вспомогательные методы для LocalDate

import javafx.scene.control.Alert;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    // Шаблон к которому хотим привести
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);


    // Формат в String из LocalDate по заданному шаблону DATE_PATTERN
    public static String format(LocalDate date) {
        if (date == null) return null;
        return DATE_TIME_FORMATTER.format(date);
    }

    // Формат в LocalDate из String
    public static LocalDate parse (String str) {
        try {
            return (DATE_TIME_FORMATTER.parse(str, LocalDate::from));
        } catch (DateTimeException dte) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Wrong date dude");
            alert.setContentText("Try again");
            alert.showAndWait();
            return null;
        }
    }

    // Проверка строки на valid
    public static boolean validString (String str) {
        return DateUtil.parse(str) != null;
    }
}
