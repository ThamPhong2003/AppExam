package com.example.demo4;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
public class examController implements Initializable {
    @FXML
    private Label timerLabel;
    private Timeline countdownTimeline;
    private Duration countdownDuration;
    public void startCountdown() {
        /*// Set the initial duration to 60 minutes (60 * 60 seconds)
        long totalSeconds = 60 * 60;
        countdownDuration = Duration.seconds(totalSeconds);

        // Create a KeyValue for updating the timer label text
        KeyValue keyValue = new KeyValue(timerLabel.textProperty(),
                String.format("%02d:%02d", (int) countdownDuration.toMinutes(), (int) countdownDuration.toSeconds() % 60));

        // Create a KeyFrame for updating the timer label every second
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            // Decrement the countdown duration by 1 second
            countdownDuration = countdownDuration.subtract(Duration.seconds(1));

            // Update the timer label text with the remaining time
            timerLabel.setText(String.format("%02d:%02d", (int) countdownDuration.toMinutes(), (int) countdownDuration.toSeconds() % 60));

            // Check if the countdown has finished
            if (countdownDuration.compareTo(Duration.ZERO) <= 0) {
                // Stop the countdown timeline
                countdownTimeline.stop();

                // Handle the timer completion here
                // For example, show a message or trigger an action
            }
        });

        // Create a Timeline for the countdown timer
        countdownTimeline = new Timeline(keyFrame);
        countdownTimeline.setCycleCount(Timeline.INDEFINITE);

        // Start the countdown timer
        countdownTimeline.play();

         */
        DatabaseConnect connector = new DatabaseConnect();
        connector.connect();
        try {

            // Truy vấn thời gian từ bảng quiz dựa trên quizID
            String query = "SELECT time FROM QUIZ WHERE quizID = ?";
            PreparedStatement statement = connector.getConnection().prepareStatement(query);
            statement.setInt(1, quizID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int time = resultSet.getInt("time");
                long totalSeconds = time * 60;

                // Set the initial duration
                countdownDuration = Duration.seconds(totalSeconds);

                // Create a KeyValue for updating the timer label text
                KeyValue keyValue = new KeyValue(timerLabel.textProperty(),
                        String.format("%02d:%02d", (int) countdownDuration.toMinutes(), (int) countdownDuration.toSeconds() % 60));

                // Create a KeyFrame for updating the timer label every second
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
                    // Decrement the countdown duration by 1 second
                    countdownDuration = countdownDuration.subtract(Duration.seconds(1));

                    // Update the timer label text with the remaining time
                    timerLabel.setText(String.format("%02d:%02d", (int) countdownDuration.toMinutes(), (int) countdownDuration.toSeconds() % 60));

                    // Check if the countdown has finished
                    if (countdownDuration.compareTo(Duration.ZERO) <= 0) {
                        // Stop the countdown timeline
                        countdownTimeline.stop();

                        // Handle the timer completion here
                        // For example, show a message or trigger an action
                    }
                });

                // Create a Timeline for the countdown timer
                countdownTimeline = new Timeline(keyFrame);
                countdownTimeline.setCycleCount(Timeline.INDEFINITE);

                // Start the countdown timer
                countdownTimeline.play();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connector.disconnect();
    }


    public int quizID  ;
    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }
    @FXML
    private VBox examVBox;
    @FXML
    private VBox choiceContainer;
    private int currentQuesNumber = 0; // Số câu hỏi hiện tại, bắt đầu từ 1

    public void initData() {
        DatabaseConnect connector = new DatabaseConnect();
        connector.connect();
        try {
            // Get the question IDs for the specified quizID from the QUIZ_QUESTION table
            String quizQuestionSql = "SELECT QuestionID FROM QUIZ_QUESTION WHERE quizID = ?";
            PreparedStatement quizQuestionStatement = connector.getConnection().prepareStatement(quizQuestionSql);
            quizQuestionStatement.setInt(1, quizID);
            ResultSet quizQuestionResultSet = quizQuestionStatement.executeQuery();

            // Iterate over the question IDs and retrieve the corresponding questions from the Question table
            // Create an ObservableList to store the question UI elements
            ObservableList<Parent> questionUIList = FXCollections.observableArrayList();

// Iterate over the question IDs and retrieve the corresponding questions
            while (quizQuestionResultSet.next()) {
                int questionID = quizQuestionResultSet.getInt("QuestionID");

                String questionSql = "SELECT id, text, name, mark FROM Question WHERE id = ?";
                PreparedStatement questionStatement = connector.getConnection().prepareStatement(questionSql);
                questionStatement.setInt(1, questionID);
                ResultSet questionResultSet = questionStatement.executeQuery();
                if (questionResultSet.next()) {
                    int id = questionResultSet.getInt("id");
                    String questionText = questionResultSet.getString("text");
                    String questionName = questionResultSet.getString("name");
                    float questionMark = questionResultSet.getFloat("mark");

                    // Retrieve choices for the current question
                    String choiceSql = "SELECT id, text, grade FROM Choice WHERE question_id = ?";
                    PreparedStatement choiceStatement = connector.getConnection().prepareStatement(choiceSql);
                    choiceStatement.setInt(1, questionID);
                    ResultSet choiceResultSet = choiceStatement.executeQuery();

                    // Load the examBar.fxml file
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("examBar.fxml"));
                    Parent root = loader.load();

                    // Get the references to the nodes in the examBar.fxml file
                    Text quesName = (Text) root.lookup("#quesName");
                    Text quesText = (Text) root.lookup("#quesText");
                    VBox choiceContainer = (VBox) root.lookup("#choicesContainer");
                    Text quesNumber = (Text) root.lookup("#quesNumber");

                    // Set the values for the question name and question text
                    quesName.setText(questionName);
                    quesText.setText(questionText);
                    currentQuesNumber++;
                    quesNumber.setText(String.valueOf(currentQuesNumber));


                    // Create a ToggleGroup for the RadioButtons
                    ToggleGroup toggleGroup = new ToggleGroup();

                    // Populate the choiceContainer with choice nodes
                    while (choiceResultSet.next()) {
                        int choiceID = choiceResultSet.getInt("id");
                        String choiceText = choiceResultSet.getString("text");
                        float choiceGrade = choiceResultSet.getFloat("grade");

                        RadioButton radioButton = new RadioButton(choiceText);
                        radioButton.setToggleGroup(toggleGroup); // Set the ToggleGroup for the RadioButton
                        choiceContainer.getChildren().add(radioButton);
                    }


                    // Add the root node to the questionUIList
                    questionUIList.add(root);

                    choiceResultSet.close();
                    choiceStatement.close();
                }

                questionResultSet.close();
                questionStatement.close();
            }

// Clear the existing content of examVBox and add all question UI elements
            examVBox.getChildren().clear();
            examVBox.getChildren().addAll(questionUIList);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(quizID);
        connector.disconnect();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}