package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Person;
import sample.MainApp;
import util.DateUtil;


public class PersonOverviewController {
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    private MainApp app;

    public PersonOverviewController() {
    }

    /* Инициализация класс-контроллера, вызывается автоматом после того как fxml
    связанный с этим контроллером будет загружен
     */
    @FXML
    public void initialize() {

        // Отрисовка данных таблицы
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        // Очистка персональный данных
        showPersonDetails(null);

        /* Слушаем изменения выбора строки в таблице, и при изменении
        * рисуем содержимое персональных данных
        * */
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    public void setMainApp (MainApp mainApp) {
        this.app = mainApp;

        // Добавление в таблицу данных из наблюдаемого списка

        personTable.setItems(mainApp.getPersonData());
    }

    /*
     * Этот метод заполняет все текстовые поля, отображая подробности об адресате.
     * Если указанный адресат = null, то все текстовые поля очищаются.
     */
    public void showPersonDetails (Person person) {
        if (person != null) {
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(DateUtil.format(person.getBirthday()));

        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");

        }

    }

    // Вызывается по кнопке удаления
    @FXML
    private void handleDeletePerson () {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Really?!");
            alert.setHeaderText("Are you sure?!");
            alert.setContentText("How are you going to live with it?");


            ButtonType buttonTypeOne = new ButtonType("Happily ever after!");
            alert.getButtonTypes().setAll(buttonTypeOne);
            alert.showAndWait();

            personTable.getItems().remove(selectedIndex);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(app.getPrimaryStage());
            alert.setTitle("No selection");
            alert.setHeaderText("No person selected");
            alert.setContentText("Please, choose victim in the table...");
            alert.showAndWait();
        }
    }

//    Вызывается по кнопке New
    @FXML
    private void handleNewPerson () {
        Person newPerson = new Person();
        boolean okClicked = app.showEditPersonOverview(newPerson);
        if (okClicked)
            app.getPersonData().add(newPerson);
    }

    //    Вызывается по кнопке Edit
    @FXML
    private void handleEditPerson () {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            if (app.showEditPersonOverview(selectedPerson)) showPersonDetails(selectedPerson);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("WTF?!");
            alert.setContentText("Pick a victim, dude...");

            alert.showAndWait();
        }
    }
}
