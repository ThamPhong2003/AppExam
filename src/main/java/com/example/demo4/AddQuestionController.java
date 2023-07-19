package com.example.demo4;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
public class AddQuestionController implements Initializable {
    @FXML
    public ChoiceBox<String> choiceBox3;
    @FXML
    private TreeVierwHandle treeView3;

    @FXML
    private TreeView<Category> treeViewQuestion;

    @FXML
    void seclectCateQuestion(MouseEvent event) {
        treeViewQuestion.setVisible(!treeViewQuestion.isVisible());

    }

    // CHon blank 3 choice---------------------------------------------------------------------------------------------------------------------------------------------


    @FXML
    private Button btback;
    @FXML
    private Button bt3choices;

    @FXML
    private GridPane startChoice;
    @FXML
    private GridPane moreChoices;

    public void more3choices(ActionEvent actionEvent) {
        startChoice.setVisible(false);
        moreChoices.setVisible(true);
        btback.setVisible(true);
        bt3choices.setVisible(false);
    }
    @FXML
    void btbackStart(ActionEvent event) {
        startChoice.setVisible(true);
        moreChoices.setVisible(false);
        btback.setVisible(false);
        bt3choices.setVisible(true);

    }
    ///// Add Question---------------------------------------------------------------------------------------------------------------------------------------------
    @FXML
    private TextField choice1;

    @FXML
    private TextField choice2;

    @FXML
    private TextField choice3;

    @FXML
    private TextField choice4;

    @FXML
    private TextField choice5;
    @FXML
    private TextField questionName;
    public int CateGoryID;
    public int questionId;
    @FXML
    private TextField questionMark;
    @FXML
    private TextField questionText;
    @FXML
    private ChoiceBox<String> grade1;
    @FXML
    private ChoiceBox<String> grade2;

    @FXML
    private ChoiceBox<String> grade3;

    @FXML
    private ChoiceBox<String> grade4;

    @FXML
    private ChoiceBox<String> grade5;
    /*float convertPercentage (String percentageString) {
        String valueString = percentageString.replace("%", "");

        // Parse the remaining string as a float
        float percentage = Float.parseFloat(valueString);

        // Convert the percentage to its decimal value

        return percentage / 100;
    } */
    float convertPercentage(String percentageString) {
        String valueString = percentageString.replace("%", "");

        if (valueString.equals("None")) {
            return 0.0f; // Trả về giá trị mặc định nếu là "None"
        } else {
            try {
                float percentage = Float.parseFloat(valueString);
                return percentage / 100;
            } catch (NumberFormatException e) {
                // Xử lý ngoại lệ khi không thể chuyển đổi thành số
                e.printStackTrace();
                return 0.0f; // Hoặc trả về giá trị mặc định khác
            }
        }
    }

    @FXML
    public void saveContinue(ActionEvent event) {
        String qtext = questionText.getText();
        String qName = questionName.getText();
        String cText1 = choice1.getText();
        String cText2 = choice2.getText();

        if (qtext.isEmpty() ||  qName.isEmpty() ||  cText1.isEmpty() || cText2.isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Vui long điền đầy đủ tên câu hỏi, tên đề bài và ít nhất 2 lựa chọn");
            alert.showAndWait();
        } else {
            DatabaseConnect connector = new DatabaseConnect();
            connector.connect();
            // Question
            //String qtext = questionText.getText();
            //String qName = questionName.getText();
            // Choice
            //String cText1 = choice1.getText();
           // String cText2 = choice1.getText();
            String cText3 = choice3.getText();
            String cText4 = choice4.getText();
            String cText5 = choice5.getText();
            questionId = connector.addQuestion(CateGoryID, qtext, qName, 1);
            String cGrade1 = grade1.getValue();
            String cGrade2 = grade2.getValue();
            String cGrade3 = grade3.getValue();
            String cGrade4 = grade4.getValue();
            String cGrade5 = grade5.getValue();
            float grade1 = convertPercentage(cGrade1);
            float grade2 = convertPercentage(cGrade2);
            float grade3 = convertPercentage(cGrade3);
            float grade4 = convertPercentage(cGrade4);
            float grade5 = convertPercentage(cGrade5);
            connector.addChoice(questionId, grade1, "", cText1);
            connector.addChoice(questionId, grade2, "", cText2);
            connector.addChoice(questionId, grade3, "", cText3);
            connector.addChoice(questionId, grade4, "", cText4);
            connector.addChoice(questionId, grade5, "", cText5);
            connector.disconnect();
        }
        /*questionText.clear();
        questionName.clear();
        choice1.clear();
        choice2.clear();
        choice3.clear();
        choice4.clear();
        choice5.clear();
        grade1.setValue(null);
        grade2.setValue(null);
        grade3.setValue(null);
        grade4.setValue(null);
        grade5.setValue(null);

         */

    }
    ///// INITIALIZE ================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /// Cate hiển thị danh sách câu hỏi
        treeViewQuestion.setVisible(false);
        treeView3 = new TreeVierwHandle(treeViewQuestion,choiceBox3);
        treeView3.start();
        ////////////// Chọn ra 3 choices
        moreChoices.setVisible(false);
        btback.setVisible(false);
        ////////////
        treeViewQuestion.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                CateGoryID = treeView3.getIdChoice();
                System.out.println(CateGoryID);
            }
        });
        /////
        List<String> gradeList = new ArrayList<>(Arrays.asList("None", "100%", "90%", "83.33333%", "80%", "75%", "70%", "66.66667%", "60%", "50%", "40%", "33.3333%", "30%", "25%", "20%", "16.66667%", "14.28571%", "12.5%", "11.11111%", "10%", "5%","-5%", "-10%", "-11.11111%", "-12.5%", "-14.28571%", "-16.66667%", "-20%", "-25%", "-30%", "-33.3333%", "-40%", "-50%", "-60%", "-66.66667%", "-70%", "-75%", "-80%", "-83.33333%"));
        grade1.getItems().addAll(gradeList);
        grade1.setValue("None");
        grade2.getItems().addAll(gradeList);
        grade2.setValue("None");
        grade3.getItems().addAll(gradeList);
        grade3.setValue("None");
        grade4.getItems().addAll(gradeList);
        grade4.setValue("None");
        grade5.getItems().addAll(gradeList);
        grade5.setValue("None");


    }
    //////// Ấn cancel thì đóng---------------------------------------------------------------------------------------------------------------------------------------------
    @FXML
    public void closeStageAddQs(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close(); // Đóng Stage khi nút Close được nhấn
    }
    //////////// Ấn save thì vừa lưu vừa đóng
    @FXML
    public void saveOut(ActionEvent event) {
        String qtext = questionText.getText();
        String qName = questionName.getText();
        // Choice
        String cText1 = choice1.getText();
        String cText2 = choice2.getText();
        if (qtext.isEmpty() ||  qName.isEmpty() ||  cText1.isEmpty() || cText2.isEmpty() ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Vui long điền đầy đủ tên câu hỏi, tên đề bài và ít nhất 2 lựa chọn");
            alert.showAndWait();
        }
        else {
            DatabaseConnect connector = new DatabaseConnect();
            connector.connect();
            // Question

            String cText3 = choice3.getText();
            String cText4 = choice4.getText();
            String cText5 = choice5.getText();

            questionId = connector.addQuestion(CateGoryID, qtext, qName, 1);

            connector.addChoice(questionId, 1, "", cText1);
            connector.addChoice(questionId, 1, "", cText2);
            connector.addChoice(questionId, 1, "", cText3);
            connector.addChoice(questionId, 1, "", cText4);
            connector.addChoice(questionId, 1, "", cText5);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
            connector.disconnect();
        }
    }

}
