package com.example.demo4;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PreviewQuiz implements Initializable {
    public Quiz quiz;
    @FXML
    private Label nameQuiz;
    public int quizID;


    public void setVariable(Quiz quiz) {
        this.quiz = quiz;     // You can use this variable to initialize or update the GUI elements in your controller
    }

    @FXML
    public AnchorPane gui61pane;

    @FXML
    private AnchorPane gui60pane;
    @FXML
    private AnchorPane gui72pane;
    public Label countQues;

    @FXML
    public Button back;


    @FXML
    void settingQuiz(MouseEvent event) throws IOException {
        gui61pane.setVisible(true);
        back.setVisible(true);
        gui60pane.setVisible(false);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PreviewQuiz.fxml"));
        AnchorPane view = loader.load();
        PreviewQuizController controller = loader.getController();
        controller.setQuizID(quiz.getId()); // Truyền ID của Quiz vào PreviewQuizController
        controller.initData();
        controller.updateQuestionList();
        gui61pane.getChildren().setAll(view);
        System.out.println(quiz.getId());
    }

    @FXML
    public void backgui61(ActionEvent event) {
        gui61pane.setVisible(false);
        gui60pane.setVisible(true);
        back.setVisible(false);

    }

    @FXML
    public void PreviewQuizAction(ActionEvent event) {
        try {
            // Tạo FXMLLoader để tải tệp FXML của giao diện mới
            FXMLLoader loader = new FXMLLoader(getClass().getResource("popup7.2.fxml"));
            Parent root = loader.load();

            popup72 popupController = loader.getController();
            popupController.setQuizID(quiz.getId()); // Truyền ID của Quiz vào PreviewQuizController

            // Tạo một Stage mới và thiết lập Scene từ root
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(quiz.getId());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        back.setVisible(false);
    }
    @FXML
    public void exportExam(ActionEvent event) throws IOException {

    }

    @FXML
    private AnchorPane guigido;
    @FXML
    public VBox examVBox;

    public void setExamVBox(VBox examVBox) {
        this.examVBox = examVBox;
    }

    public int questionID;
}
