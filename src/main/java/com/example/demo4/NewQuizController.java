package com.example.demo4;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NewQuizController implements Initializable {
    @FXML
         public CheckBox   enable1;
    @FXML
          public CheckBox  enable2;
    @FXML
    private ImageView canhbao1;

    @FXML
    private HBox closehbox;

    @FXML
    private ComboBox<?> closequiz1;

    @FXML
    private ComboBox<?> closequiz2;

    @FXML
    private ComboBox<?> closequiz3;

    @FXML
    private ComboBox<?> closequiz4;

    @FXML
    private ComboBox<?> closequiz5;

    @FXML
    private CheckBox enable11;

    @FXML
    private CheckBox enable22;

    @FXML
    private CheckBox enableTime;

    @FXML
    private HBox openhbox;

    @FXML
    private ComboBox<?> openquiz1;

    @FXML
    private ComboBox<?> openquiz2;

    @FXML
    private ComboBox<?> openquiz3;

    @FXML
    private ComboBox<?> openquiz4;

    @FXML
    private ComboBox<?> openquiz5;

    @FXML
    private TextField quizNameField;

    @FXML
    private ComboBox<?> timelimit1;

    @FXML
    private TextField timelimit2;

    @FXML
    private ComboBox<?> whentim1;
    private Stage stage;

    @FXML
    private TextField timeField;


    @FXML
    void enableTime(ActionEvent event) {
        boolean enabled = enableTime.isSelected();
        timeField.setDisable(!enabled);
        timeField.clear(); // Xóa nội dung của TextField nếu CheckBox không được chọn
    }
    @FXML
    void createQuiz(MouseEvent event) {
        DatabaseConnect connector = new DatabaseConnect();
        connector.connect();
        String quizName = quizNameField.getText();
        if (quizName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Vui long điền tên Quiz");
            alert.showAndWait();
        } else {
            if (enableTime.isSelected()) {
                String timeLimit = timeField.getText(); // Lấy giá trị từ TextField
                float timeLimitFloat = Float.parseFloat(timeLimit); // Ép kiểu thành float
                boolean quizShuffle = false;
                connector.addQuiz(quizName, timeLimitFloat, quizShuffle, 1);
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.close(); // Đóng Stage
                connector.disconnect();
                setIsClose(true);
            } else {
                boolean quizShuffle = false;
                connector.addQuiz(quizName, 0, quizShuffle, 1);
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.close(); // Đóng Stage
                connector.disconnect();
                setIsClose(true);

            }
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    private BooleanProperty isCloseProperty = new SimpleBooleanProperty(false);
    public BooleanProperty isCloseProperty() {
        return isCloseProperty;
    }
    public void setIsClose(boolean value) {
        isCloseProperty.set(value);
    }
    @FXML
    void cancelQuiz(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close(); // Đóng Stage khi nút Close được nhấn
    }
    @FXML
    void quizSetting (MouseEvent event){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boolean enabled = enableTime.isSelected();
        timeField.setDisable(!enabled);
    }
}
