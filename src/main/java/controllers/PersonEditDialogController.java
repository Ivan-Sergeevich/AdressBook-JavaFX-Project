package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Person;
import util.DateUtil;


// Контроллер окна редактирования данных об адресате
public class PersonEditDialogController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField birthdayField;

    private Stage dialogStage;
    private Person person;
    private boolean okClicked = false;

    @FXML
    private void initialize() {}

    public void setDialogStage (Stage stage) {
        this.dialogStage = stage;
    }

//    Задает адресата, которого будем менять
    public void setPerson (Person person1) {
        this.person = person1;

        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        streetField.setText(person.getStreet());
        postalCodeField.setText(Integer.toString(person.getPostalCode()));
        cityField.setText(person.getCity());
        birthdayField.setText(DateUtil.format(person.getBirthday()));
        birthdayField.setPromptText("dd.mm.yyyy");
    }

//    Возвращает true, если ткнули Ок
    public boolean isOkClicked() {
        return okClicked;
    }

//    Вызывается если ткнули Ок
    @FXML
    private void handleOk () {
        if (isInputValid()) {
            person.setFirstName(firstNameField.getText());
            person.setLastName(lastNameField.getText());
            person.setStreet(streetField.getText());
            person.setCity(cityField.getText());
            person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
            person.setBirthday(DateUtil.parse(birthdayField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

//    Вызывается если ткнули Cancel
    @FXML
    private void handleCancel () {
        dialogStage.close();
    }

//    Проверка валидности вводимых данных
    private boolean isInputValid () {
        StringBuilder errorMessage = new StringBuilder();
        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            System.out.println("something bad");
            errorMessage.append("First name is not valid! \n");
        }

        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage.append("Last name is not valid! \n");
        }

        if (streetField.getText() == null || streetField.getText().length() == 0) {
            errorMessage.append("Street is not valid! \n");
        }

        if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0) {
            errorMessage.append("Postal code is not valid! \n");
        } else {
            try {
                Integer.parseInt(postalCodeField.getText());
            } catch (NumberFormatException n) {
                errorMessage.append("Postal code must be Integer!\n");
            }
        }

        if (cityField.getText() == null || cityField.getText().length() == 0) {
            errorMessage.append("City is not valid! \n");
        }

        if (birthdayField.getText() == null || birthdayField.getText().length() == 0) {
            errorMessage.append("Birthday is not valid! \n");
        } else if (!DateUtil.validString(birthdayField.getText()))
            errorMessage.append("Use format dd.mm.yyyy, asshole!\n");

        if (errorMessage.length() == 0) return true;
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Try again");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage.toString());

            alert.showAndWait();

            return false;
        }
    }
}
