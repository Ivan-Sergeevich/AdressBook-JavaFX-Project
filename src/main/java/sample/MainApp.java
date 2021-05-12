package sample;

import controllers.PersonEditDialogController;
import controllers.PersonOverviewController;
import controllers.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Person;
import model.PersonListWrapper;
import controllers.BirthdayStatisticsController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private final ObservableList<Person> personData = FXCollections.observableArrayList();

    public MainApp() {
        personData.add(new Person("Hans", "Muster", "Street 1", "Moscow", 1532, 1990,5,25));
        personData.add(new Person("Ruth", "Mueller", "Street 1", "Moscow", 1532, 1992,3,14));
        personData.add(new Person("Heinz", "Kurz", "Street 1", "Moscow", 1532, 1991,7,18));
        personData.add(new Person("Cornelia", "Meier", "Street 1", "Moscow", 1532,  1990,5,25));
        personData.add(new Person("Werner", "Meyer", "Street 1", "Moscow", 1532, 1992,3,14));
        personData.add(new Person("Lydia", "Kunz", "Street 1", "Moscow", 1532, 1990,5,25));
        personData.add(new Person("Anna", "Best", "Street 1", "Moscow",1532, 1991,7,18));
        personData.add(new Person("Stefan", "Meier", "Street 1", "Moscow", 1532, 1992,3,14));
        personData.add(new Person("Martin", "Mueller", "Street 1", "Moscow",1532, 1990,5,25));
    }

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    // Инициализация корневого макета, пробует загрузить последний файл с адресатами
    // отрисовка сцены
    public void initRootLayout() {
        try {

            // Загружаем корневой макет
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/rootLayout.fxml"));
            rootLayout = loader.load();

            // Рисуем сцену
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            InputStream iconStream = getClass().getResourceAsStream("/images/orig1.jpg");
            Image image = new Image(Objects.requireNonNull(iconStream));
            primaryStage.getIcons().add(image);

            // Даем доступ контроллера к приложению
            RootLayoutController rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this);

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Пробуем загрузить последний открытый файл
//        File file = getPersonFilePath();
//        if (file != null) loadPersonDataFromFile(file);
    }

    // Показывает в корневом макете сведения об адресатах.
    public void showPersonOverview() {
        try {

            // Загружаем сведения об адресатах.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/personOverview.fxml"));
            AnchorPane personOverview = loader.load();

            // Помещаем сведения об адресатах в центр корневого макета.
            rootLayout.setCenter(personOverview);

            // Даем контроллеру доступ к главному приложению
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    Открывает диалоговое окно для редактирования адресата
    public boolean showEditPersonOverview(Person person) {
        try {
//            Загружаем фхмл, создаем новую сцену для диалогового окна
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/PersonEditDialog.fxml"));
            AnchorPane anchorPane = loader.load();

//            Создаем диалоговое окно Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(anchorPane);
            dialogStage.setScene(scene);

//            Передаем адресата в контроллер
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

//            Отображаем окно и ждем что будет
            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {

        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Nahodka AIS");

        initRootLayout();
        showPersonOverview();

        primaryStage.show();
    }

    /*
     * Возвращает preference файла адресатов, то есть, последний открытый файл.
     * Этот preference считывается из реестра, специфичного для конкретной
     * операционной системы.
     */
    public File getPersonFilePath () {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) return new File(filePath);
        else return null;
    }

    /*
     * Задаёт путь к текущему загруженному файлу. Этот путь сохраняется
     * в реестре.
     * null - чтобы удалить путь.
     */
    public void setPersonFilePath (File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (prefs != null) {
            prefs.put("filePath", file.getPath());

            // Обновление заглавия сцены.
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            // Обновление заглавия сцены.
            primaryStage.setTitle("AddressApp");
        }
    }

    /*
     * Загружает информацию об адресатах из указанного файла.
     * Текущая информация об адресатах будет заменена.
     */
    public void loadPersonDataFromFile (File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

        //  Чтение из файла и анмаршаллинг
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

        //  Сохранение пути к файлу в реестр
            setPersonFilePath(file);

        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    //  Сохраняет текущую информацию об адресатах в указанном файле.
    public void savePersonDataToFile (File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller marsh = context.createMarshaller();

            // Обёртываем наши данные об адресатах.
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            // Маршаллим и сохраняем XML в файл.
            marsh.marshal(wrapper, file);

            // Сохраняем путь к файлу в реестре.
            setPersonFilePath(file);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("wtf dude");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }

    }

    public void showBDStatistic () {
        try {

            // Загружает fxml-файл и создаёт новую сцену для всплывающего окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/BirthdayStatistics.fxml"));
            AnchorPane anchorPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthdays statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            Scene scene = new Scene(anchorPane);
            dialogStage.setScene(scene);

            // Передаёт адресатов в контроллер.
            BirthdayStatisticsController bdControl = loader.getController();
            bdControl.setPersonsData(personData);

            dialogStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
