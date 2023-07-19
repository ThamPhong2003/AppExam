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
public class EditQuestionController implements Initializable {
    private int idQuesCreate = -1;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setIdQues(int id) {
        this.idQuesCreate = id;
    }
    private Question questionStage;
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
    float convertPercentage (String percentageString) {
        String valueString = percentageString.replace("%", "");

        // Parse the remaining string as a float
        float percentage = Float.parseFloat(valueString);

        // Convert the percentage to its decimal value

        return percentage / 100;
    }
    @FXML
    public void saveContinue(ActionEvent event) {
        DatabaseConnect connector = new DatabaseConnect();
        connector.connect();
        // Question
        String qtext = questionText.getText();
        String qName = questionName.getText();
        // Choice
        String cText1 = choice1.getText();
        String cText2 = choice2.getText();
        String cText3 = choice3.getText();
        String cText4 = choice4.getText();
        String cText5 = choice5.getText();
        questionId = connector.addQuestion(CateGoryID,qtext, qName,1);
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
        connector.addChoice(questionId,grade1,"",cText1);
        connector.addChoice(questionId,grade2,"",cText2);
        connector.addChoice(questionId,grade3,"",cText3);
        connector.addChoice(questionId,grade4,"",cText4);
        connector.addChoice(questionId,grade5,"",cText5);
        connector.disconnect();
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
        DatabaseConnect connector = new DatabaseConnect();
        connector.connect();
        connector.deleteQues(idQuesCreate);
        List<Choice> choices = connector.getChoicesFromQuestion(idQuesCreate);
        for (Choice choice : choices) {
            connector.deleteChoice(choice.getId());
        }
        // Question
        String qtext = questionText.getText();
        String qName = questionName.getText();
        // Choice
        String cText1 = choice1.getText();
        String cText2 = choice2.getText();
        String cText3 = choice3.getText();
        String cText4 = choice4.getText();
        String cText5 = choice5.getText();
        questionId = connector.addQuestion(CateGoryID,qtext, qName,1);
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
        connector.addChoice(questionId,grade1,"",cText1);
        connector.addChoice(questionId,grade2,"",cText2);
        connector.addChoice(questionId,grade3,"",cText3);
        connector.addChoice(questionId,grade4,"",cText4);
        connector.addChoice(questionId,grade5,"",cText5);
        //Delete old ques
        //Add new Ques
        connector.disconnect();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void run(Question question) {
        questionStage = question;
        questionText.setText(question.getText());
        questionName.setText(question.getName());
        questionMark.setText(Float.toString(question.getMark()));
       // addedit.setText("Editing Multiple choice question");
        setIdQues(question.getId());
        this.stage.setOnShown(e -> {
            DatabaseConnect connector = new DatabaseConnect();
            connector.connect();
            treeView3.setIdChoice(connector.getCategory(question.getCateGoryID()).getId());
            choiceBox3.setValue(connector.getCategory(question.getCateGoryID()).getName());
            CateGoryID = question.getCateGoryID();
            List<Choice> choices = connector.getChoicesFromQuestion(question.getId());
            int numChoices = 0;
            for (Choice choice : choices) {
                numChoices++;
                if (numChoices == 1) {
                    choice1.setText(choice.getText());
                    String stringGrade = Float.toString(choice.getGrade() * 100);
                    grade1.setValue(stringGrade + "%");
                }
                if (numChoices == 2) {
                    choice2.setText(choice.getText());
                    String stringGrade = Float.toString(choice.getGrade() * 100);
                    grade2.setValue(stringGrade + "%");
                }
                if (numChoices == 3) {
                    choice3.setText(choice.getText());
                    String stringGrade = Float.toString(choice.getGrade() * 100);
                    grade3.setValue(stringGrade + "%");
                }
                if (numChoices == 4) {
                    choice4.setText(choice.getText());
                    String stringGrade = Float.toString(choice.getGrade() * 100);
                    grade4.setValue(stringGrade + "%");
                }
                if (numChoices == 5) {
                    choice5.setText(choice.getText());
                    String stringGrade = Float.toString(choice.getGrade() * 100);
                    grade5.setValue(stringGrade + "%");
                }
            }
            connector.disconnect();

        });
    }

}
