package com.example.demo4;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class HelloController implements Initializable {

    @FXML
    private VBox quizzBox;
    @FXML
    private AnchorPane menu1_1;
    void updateQuizShow() {
        //
        quizzBox.getChildren().clear();
        DatabaseConnect connector = new DatabaseConnect();
        connector.connect();
        List<Quiz> quizzes = connector.getQuiz(); // Sửa thành getQuizzes()
        for (Quiz quizObj : quizzes) { // Thay đổi tên biến quiz thành quizObj
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quizBar.fxml"));
            try {
                AnchorPane quizBar = loader.load();;
                Text nameQuiz = (Text) quizBar.lookup("#quizName");
                nameQuiz.setOnMouseClicked(e -> {
                    System.out.println("Nhao");
                    // Call your new function here
                    openPreviewQuiz(quizObj);
                });

                nameQuiz.setText(quizObj.getName());
                quizzBox.getChildren().add(quizBar);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuPanel.setVisible(false);
        //
        quizzBox.getChildren().clear();
        DatabaseConnect connector = new DatabaseConnect();
        connector.connect();

        //

        //

        List<Quiz> quizzes = connector.getQuiz(); // Sửa thành getQuizzes()
        for (Quiz quizObj : quizzes) { // Thay đổi tên biến quiz thành quizObj
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quizBar.fxml"));
            try {
                AnchorPane quizBar = loader.load();
                Text nameQuiz = (Text) quizBar.lookup("#quizName");
                nameQuiz.setOnMouseClicked(e -> {
                    // Gọi hàm openPreviewQuiz() với tham số là quizObj
                    openPreviewQuiz(quizObj);
                });
                nameQuiz.setText(quizObj.getName());
                quizzBox.getChildren().add(quizBar);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @FXML
    private VBox examVbox;

    public void hidePane(){
        menu1_1.setVisible(false);
        menu1_1.setManaged(false);
    }
    private HelloController helloController;

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }

    private void openPreviewQuiz (Quiz quiz){
            System.out.println(quiz.getName());
            System.out.println(quiz.getId());
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("Gui6.1.fxml"));
                Parent root = loader.load();

                // Create a new stage
                Stage previewStage = new Stage();
                previewStage.setTitle("Quiz Preview");

                PreviewQuiz controller = loader.getController(); // Get the controller instance
                controller.setVariable(quiz); // Pass the variable to the controller

                // Set the loaded scene as the content for the new stage
                previewStage.setScene(new Scene(root));

                // Show the new stage
                previewStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý các ngoại lệ xảy ra trong quá trình tải hoặc hiển thị stag
            }

        }
    // Bai 1
    @FXML
    public ImageView SettingImage;
    @FXML
    private MenuButton setting;
    @FXML
    private FontIcon btSetting;


    //-------Chuyển sang Gui 2.1
    //-------------------------
    @FXML
    private AnchorPane menuPanel;
    @FXML
    private Button turneditingon;
    @FXML
    private AnchorPane MainPane;
    @FXML
    private MenuItem btMenu1;
    @FXML
    public AnchorPane firstPane;

    @FXML
    private AnchorPane menu2View;
    @FXML
    void switchToMenu1(MouseEvent event) {
        menuPanel.setVisible(true);
        btSetting.setVisible(false);
        turneditingon.setVisible(false);
    }




    @FXML
    public void switchToMenu2(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu2.fxml"));
        AnchorPane view = loader.load();
        menu2Controller = loader.getController();
        MainPane.getChildren().setAll(view);
        menuPanel.setVisible(false);
        turneditingon.setVisible(true);
        btSetting.setVisible(true);
        menu1_1.setVisible(false);

    }
    Menu2Controller menu2Controller ;
    @FXML
    void home (){

    }
    @FXML
    void thicuoiky(MouseEvent event) {
        MainPane.getChildren().clear();
        turneditingon.setVisible(true);
        btSetting.setVisible(true);
        menu1_1.setVisible(true);
    }


    @FXML
    public void turnEdit(ActionEvent event) {
        try {
            // Tạo một Stage mới
            Stage stage = new Stage();

            // Tải file FXML mới
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewQuiz.fxml"));
            Parent root = loader.load();
            NewQuizController controller = loader.getController();
            controller.setStage(stage);
//            Gui32CreateQuestionViewController controller = loader.getController();
//            controller.setStage(stage);

            // T&#x1EA1;o m&#x1ED9;t Scene t&#x1EEB; Parent v&agrave; &#x111;&#x1EB7;t n&oacute; cho Stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Hiển thị cửa sổ mới
            stage.show();
            controller.isCloseProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    updateQuizShow();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //------- chuyển sang Gui 3.2
    //-------------------------
    /// Bai 6

    @FXML
    void selectCategory(MouseEvent event) {

    }
    @FXML
    private Button btMenu2;


    /// Add Catrgory
    @FXML
    private TextField idNumberTextField;

    @FXML
    private TextField inforTextField;



}


