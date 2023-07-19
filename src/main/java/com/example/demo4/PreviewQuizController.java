    package com.example.demo4;

    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.fxml.Initializable;
    import javafx.scene.Node;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.CheckBox;
    import javafx.scene.control.Label;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.AnchorPane;
    import javafx.scene.layout.VBox;
    import javafx.scene.text.Text;
    import javafx.stage.Stage;

    import java.io.IOException;
    import java.net.URL;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.text.DecimalFormat;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.ResourceBundle;

    import static java.sql.Types.NULL;

    public class PreviewQuizController implements Initializable {
        private int quizID;

        public void setQuizID(int quizID) {
            this.quizID = quizID;
        }

        int id;
        String name = "Quiz 1";
        double time = 60.0;
        boolean shuffle = true;
        double totalmark = 100.0;
        Quiz quiz = new Quiz(id, name, time, shuffle, totalmark);


        @FXML
        public CheckBox shuffleCheckbox;


        public List<Question> selectedQuestion = new ArrayList<>();

        @FXML
        public VBox vboxQuizBar;

        @FXML
        public void addQuesFromQB(ActionEvent event) {
            try {
                // Tạo một Stage mới
                Stage stage = new Stage();

                // Tải file FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("questionFromQB.fxml"));
                Parent root = loader.load();
                questionFromQBController controller = questionFromQBController.getInstance();
                controller.setParentCountQues(countQues2);
                controller.setParentVBox(vboxQuizBar);
                controller.isCloseProperty().addListener((observable1, oldValue1, newValue1) -> {
                    if (newValue1) {
                        selectedQuestion = controller.getQuesList();
                        for (Question ques : selectedQuestion) {
                            System.out.println(ques.getId());
                        }
                    }
                });
                // T&#x1EA1;o m&#x1ED9;t Scene t&#x1EEB; Parent v&agrave; &#x111;&#x1EB7;t n&oacute; cho Stage
                Scene scene = new Scene(root);
                stage.setScene(scene);

                // Hiển thị cửa sổ mới
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @FXML
        public void aRandomQues(ActionEvent event) {
            try {
                // Tạo một Stage mới
                Stage stage = new Stage();

                // Tải file FXML mới
                FXMLLoader loader = new FXMLLoader(getClass().getResource("randomQuestion.fxml"));
                Parent root = loader.load();
                //questionFromQBController view = loader.getController();
                //view.setParentVBox(vboxQuizBar);

                // T&#x1EA1;o m&#x1ED9;t Scene t&#x1EEB; Parent v&agrave; &#x111;&#x1EB7;t n&oacute; cho Stage
                Scene scene = new Scene(root);
                stage.setScene(scene);

                // Hiển thị cửa sổ mới
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @FXML
        private Label countQues;

        public void setQuiz(Quiz quiz) {
            this.quiz = quiz;
        }

        @FXML
        void saveQuesQuiz(ActionEvent event) throws SQLException {
            DatabaseConnect connector = new DatabaseConnect();
            connector.connect();
            //questionFromQBController controller = new questionFromQBController();
            //controller.setParentCountQues(countQues);
            for (Question ques : selectedQuestion) {
                connector.addQuizQues(quizID, ques.getId());
                //
            }
            connector.disconnect();
            updateQuestionList();



        }


        @FXML
        public AnchorPane gui62pane;
        @FXML
        private Label countQues2;

        @FXML
        public void backToGui61(ActionEvent event) throws IOException {

        }

        private questionFromQBController questionfromQBController;

        @FXML
        private VBox parentVBox;

        public void setQuestionfromQBController(questionFromQBController controller) {
            this.questionfromQBController = controller;
            this.questionfromQBController.setParentVBox(parentVBox);
        }
        public static int questionCount = 0;

        @FXML
        void shuffleQues(ActionEvent event) {

        }

        public void initData() {

            DatabaseConnect connector = new DatabaseConnect();
            connector.connect();
            int questionCount = 0;


            try {
                // Thực hiện truy vấn để lấy danh sách câu hỏi từ bảng QUIZ_QUESTION
                String sql = "SELECT Question.name AS QuestionName, Question.text AS QuestionText, QUIZ.name AS QuizName " +
                        "FROM Question " +
                        "JOIN QUIZ_QUESTION ON Question.id = QUIZ_QUESTION.QuestionID " +
                        "JOIN QUIZ ON QUIZ_QUESTION.quizID = QUIZ.quizID " +
                        "WHERE QUIZ_QUESTION.quizID = ?";

                PreparedStatement statement = connector.getConnection().prepareStatement(sql);
                statement.setInt(1, quizID);
                ResultSet resultSet = statement.executeQuery();


                //
                vboxQuizBar.getChildren().clear();
                while (resultSet.next()) {




                    // Tạo một đối tượng quesBar từ file FXML
                    FXMLLoader innerLoader = new FXMLLoader(getClass().getResource("quesBar2.fxml"));
                    AnchorPane quesBar = innerLoader.load();

                    Text questionName = (Text) quesBar.lookup("#quesName");
                    Text questionText = (Text) quesBar.lookup("#quesText");
                    String questionNameValue = resultSet.getString("QuestionName");
                    String questionTextValue = resultSet.getString("QuestionText");

                    questionName.setText(questionNameValue);
                    questionText.setText(questionTextValue);
                    vboxQuizBar.getChildren().add(quesBar);
                    questionCount++;
                }
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e);
            } finally {
                connector.disconnect();
            }
            DecimalFormat decimalFormat = new DecimalFormat("0.00+");
            String formattedCount = decimalFormat.format(questionCount);
            countQues.setText(formattedCount);

        }

        private void deleteQuestions() {

        }
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {


            initData();

        }


        public void updateQuestionList() {
            // Xóa tất cả các câu hỏi hiện tại trong VBox
            if (vboxQuizBar != null) {
                vboxQuizBar.getChildren().clear();
                // Lấy danh sách câu hỏi từ nguồn dữ liệu (database) và thêm vào VBox
                DatabaseConnect connector = new DatabaseConnect();
                connector.connect();
                try {
                    // Thực hiện truy vấn để lấy danh sách câu hỏi từ bảng QUIZ_QUESTION
                    String sql = "SELECT Question.name AS QuestionName, Question.text AS QuestionText, QUIZ.name AS QuizName " +
                            "FROM Question " +
                            "JOIN QUIZ_QUESTION ON Question.id = QUIZ_QUESTION.QuestionID " +
                            "JOIN QUIZ ON QUIZ_QUESTION.quizID = QUIZ.quizID " +
                            "WHERE QUIZ_QUESTION.quizID = ?";

                    PreparedStatement statement = connector.getConnection().prepareStatement(sql);
                    statement.setInt(1, quizID);
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        // Tạo một đối tượng quesBar từ file FXML
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("quesBar2.fxml"));
                        AnchorPane quesBar = loader.load();

                        Text questionName = (Text) quesBar.lookup("#quesName");
                        Text questionText = (Text) quesBar.lookup("#quesText");

                        String questionNameValue = resultSet.getString("QuestionName");
                        String questionTextValue = resultSet.getString("QuestionText");

                        questionName.setText(questionNameValue);
                        questionText.setText(questionTextValue);
                        vboxQuizBar.getChildren().add(quesBar);
                    }
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    connector.disconnect();
                }
            }
        }

    }



