package com.example.demo4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class quesBar2Controller implements Initializable {
    public Question question;

    public void setQuestion(Question question) {
        this.question = question;
    }

    @FXML
    private Button deleteQues;

    @FXML
    private Text quesName;

    @FXML
    private Text quesText;
    private QuestionWrapper questionWrapper; // Thay thế bằng QuestionWrapper

    @FXML
    private VBox parentContainer;
    public int questionID;
    PreviewQuizController controller = new PreviewQuizController();
    public int quizID;
    @FXML
    private void deleteQuestion(ActionEvent event) throws SQLException {
      /*  DatabaseConnect connector = new DatabaseConnect();
        connector.connect();
        String deleteQuestionSQL = "DELETE FROM Question WHERE id = ? AND quiz_id = ?";
        String deleteChoicesSQL = "DELETE FROM Choice WHERE question_id = ?";

        // Xóa câu hỏi từ bảng "Question" của Quiz cụ thể
        PreparedStatement deleteQuestionStatement = connector.getConnection().prepareStatement(deleteQuestionSQL);
        deleteQuestionStatement.setInt(1, questionID);
        deleteQuestionStatement.setInt(2, quizID);
        deleteQuestionStatement.executeUpdate();

        // Xóa tất cả các bản ghi liên quan trong bảng "Choice"
        PreparedStatement deleteChoicesStatement = connector.getConnection().prepareStatement(deleteChoicesSQL);
        deleteChoicesStatement.setInt(1, questionID);
        deleteChoicesStatement.executeUpdate();

        VBox parentVBox = (VBox) deleteQues.getParent().getParent();

        // Đánh dấu câu hỏi là đã bị xóa
        questionWrapper.setDeleted(true);

        parentVBox.getChildren().remove(deleteQues.getParent());
        controller.updateQuestionList();
        connector.disconnect();

       */
    }




    public void setPreviewQuizController(PreviewQuizController controller) {
        this.controller = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionWrapper = new QuestionWrapper(null); // Khởi tạo questionWrapper với giá trị mặc định
    }

}
