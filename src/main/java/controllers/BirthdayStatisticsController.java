package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import model.Person;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

//  Контроллер для представления статистики дней рождений.
public class BirthdayStatisticsController {
    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private CategoryAxis xAxis;

    private ObservableList<String> monthsNames = FXCollections.observableArrayList();

    /*
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл был загружен.
     */

    @FXML
    private void initialize() {
        // Получаем массив с английскими именами месяцев и добавляем в лист.
        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();
        monthsNames.addAll(Arrays.asList(months));

        // Назначаем имена месяцев категориями для горизонтальной оси.
        xAxis.setCategories(monthsNames);
    }

    // Задаёт адресатов, о которых будет показана статистика.
    public void setPersonsData (List<Person> persons) {
        int[] monthCounter = new int[12];
        for (Person person : persons) {
            int month = person.getBirthday().getMonthValue()-1;
            monthCounter[month]++;
        }

        XYChart.Series <String, Integer> series = new XYChart.Series<>();

        // Создаём объект XYChart.Data для каждого месяца.
        // Добавляем его в серии.
        for (int i = 0; i < monthCounter.length; i++) {
            series.getData().add(new XYChart.Data<>(monthsNames.get(i), monthCounter[i]));
        }

        barChart.getData().add(series);
    }
}
