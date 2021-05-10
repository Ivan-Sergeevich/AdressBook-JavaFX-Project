package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import sample.MainApp;

import java.io.File;

/*
 * Контроллер для корневого макета. Корневой макет предоставляет базовый
 * макет приложения, содержащий строку меню и место, где будут размещены
 * остальные элементы JavaFX.
 */
public class RootLayoutController {

    //  Ссылка на главное приложение
    private MainApp mainApp;

    //  Вызывается главным приложением, чтобы оставить ссылку на самого себя.
    public void setMainApp(MainApp mainApplication) {
        mainApp = mainApplication;
    }

    //  Клац по New в меню
    @FXML
    private void handleNew() {
        mainApp.getPersonData().clear();
        mainApp.setPersonFilePath(null);
    }

    /*
     *  Клац по Open в меню, так же открывает FileChooser
     *  для загрузки адресной книги из файла
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        //  Задаём фильтр расширений
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);

        //  Показываем диалог загрузки
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) mainApp.loadPersonDataFromFile(file);
    }

    /*
     * Сохраняет файл в файл адресатов, который в настоящее время открыт.
     * Если файл не открыт, то отображается диалог "save as".
     */
    @FXML
    private void handleSave() {
        File personFile = mainApp.getPersonFilePath();
        if (personFile != null) mainApp.savePersonDataToFile(personFile);
        else handleSaveAs();
    }

    /*
     *  Клац по Save as... в меню, так же открывает FileChooser
     *  для выбора пути сохранения файла
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        //  Задаём фильтр расширений
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "XML Files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);

        //  Показываем диалог загрузки
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            if (!file.getPath().endsWith(".xml"))
                file = new File(file.getPath() + ".xml");
            mainApp.savePersonDataToFile(file);
        }
    }

    //  Клац по About в меню
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("AddressApp");
        alert.setHeaderText("About");
        alert.setContentText("Author: Ivan Sergeevich");

        alert.showAndWait();
    }

    //  Клац по Close в меню
    @FXML
    private void handleClose() {
        System.exit(0);
    }

    // Клац по Show BD... в меню
    @FXML
    private void handleBDStatistics () {
        mainApp.showBDStatistic();
    }
}