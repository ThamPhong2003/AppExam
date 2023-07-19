package com.example.demo4;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
public class Menu2Controller implements Initializable {
    @FXML
    void showAllQuestion(ActionEvent event) {

    }

    @FXML
    private ChoiceBox<String> choiceBoxCategory1;
    @FXML
    private ChoiceBox<String> choiceBoxCategory2;
    @FXML
    public TreeView<Category> treeViewCategory;
    @FXML
    private TreeVierwHandle treeView1;
    @FXML
    private TreeVierwHandle treeView2;
    public int IdParent2;
    public int CateGoryID;
    @FXML
    private TextField nameTextField;
    @FXML
    private TreeView<Category> treeViewParent;
    @FXML
    private ListView<Question> ListQuestionVIew;
    @FXML
    private VBox quesVbox;

    @FXML
    public void buttonAddCategory(MouseEvent event) {
        DatabaseConnect connector = new DatabaseConnect();
        connector.connect();
        String name = nameTextField.getText();

        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Vui long điền tên category");
            alert.showAndWait();
        } else {
            connector.addCategory(IdParent2, "", name);
            connector.disconnect();
            treeView2.start();
            treeView1.start();
        }
    }

    @FXML
    private CheckBox tick1;

    // Set the event handler for mouse click
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /// Bài 4
        // Xử lý sự kiện khi có tệp được kéo và thả vào cửa sổ ứng dụng
        root.setOnDragOver((DragEvent event) -> {
            if (event.getGestureSource() != root && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        root.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                String filePath = db.getFiles().get(0).getAbsolutePath();
                // Xử lý tệp được kéo và thả ở đây
                labelDragFile.setText(filePath);
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        ////
        ////
        ////
        ////
        treeViewCategory.setVisible(false);
        treeViewParent.setVisible(false);
        treeView1 = new TreeVierwHandle(treeViewCategory, choiceBoxCategory1);
        treeView2 = new TreeVierwHandle(treeViewParent, choiceBoxCategory2);
        treeView1.start();
        treeView2.start();
        treeViewParent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                IdParent2 = treeView2.getIdChoice();
            }
        });
        treeViewCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateQuesShow(true);
            }
        });
        tick1.setOnMousePressed(event -> {
            updateQuesShow(false);
        });
        // Connect to the database
        //
        //
        //

    }
    void updateQuesShow(Boolean type) {
        quesVbox.getChildren().clear();
        CateGoryID = treeView1.getIdChoice();
        System.out.println(CateGoryID);
        DatabaseConnect connector = new DatabaseConnect();
        connector.connect();
        List<Question> questions = connector.getQuestionsFromCategory(CateGoryID);
        for (Question question : questions) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("quesBar.fxml"));
            Parent root = null;
            try {
                root = loader.load();
                Text nameQues = (Text) root.lookup("#quesName");
                Button quesEdit = (Button) root.lookup("#quesEdit");
                quesEdit.setOnMouseClicked(e -> {
                    // Gọi hàm openPreviewQuiz() với tham số là quizObj
                    openEditingQuestion(question);
                });
                Text textQues = (Text) root.lookup("#quesText");
                nameQues.setText(question.getName());
                textQues.setText(question.getText());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            quesVbox.getChildren().add(root);
        }
        if(tick1.isSelected() == type){
            try{
                List<Category> allCategory= connector.getCategories(CateGoryID);
                for(Category category: allCategory)
                {
                    List<Question> questionss = connector.getQuestionsFromCategory(category.getId());
                    for (Question question : questionss) {

                        FXMLLoader itemLoader = new FXMLLoader(getClass().getResource("quesBar.fxml"));
                        Parent itemNode = itemLoader.load();

                        Text nameQues = (Text) itemNode.lookup("#quesName");
                        Button quesEdit = (Button) itemNode.lookup("#quesEdit");
                        quesEdit.setOnMouseClicked(e -> {
                            // Gọi hàm openPreviewQuiz() với tham số là quizObj
                            openEditingQuestion(question);
                        });
                        Text textQues = (Text) itemNode.lookup("#quesText");
                        nameQues.setText(question.getName());
                        textQues.setText(question.getText());
                        quesVbox.getChildren().add(itemNode);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        connector.disconnect();
    }}
    public void openEditingQuestion(Question question) {
        try {
            // Tạo một Stage mới
            Stage stage = new Stage();

            // Tải file FXML mới
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditQuestion.fxml"));
            Parent root = loader.load();
            EditQuestionController controller = loader.getController();
            controller.setStage(stage);
            controller.run(question);
//            Gui32CreateQuestionViewController controller = loader.getController();
//            controller.setStage(stage);

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
    void selectCategory(MouseEvent event) {

        treeViewCategory.setVisible(!treeViewCategory.isVisible());
        //////
        /////
        DatabaseConnect connector = new DatabaseConnect();
        connector.connect();
        String qText = nameTextField.getText();
        //connector.addQuestion(CateGoryID, qText, "", 1);
        // ListQuestionVIew.getItems().add(qText);
        connector.disconnect();

    }

    @FXML
    void ParentCategory(MouseEvent event) {
        treeViewParent.setVisible(!treeViewParent.isVisible());
    }

    @FXML
    void switchToAddQuestion(ActionEvent event) {
        try {
            // Tạo một Stage mới
            Stage stage = new Stage();

            // Tải file FXML mới
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddQuestion.fxml"));
            Parent root = loader.load();

//            Gui32CreateQuestionViewController controller = loader.getController();
//            controller.setStage(stage);

            // T&#x1EA1;o m&#x1ED9;t Scene t&#x1EEB; Parent v&agrave; &#x111;&#x1EB7;t n&oacute; cho Stage
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Hiển thị cửa sổ mới
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////
    ////
    //// BAI 4==========================================================
    ////
    ////
    @FXML
    private Label labelDragFile;
    @FXML
    private VBox root;

    public File selectedFile;
    private long fileSize;
    private long maxSize = 100 * 1024 * 1024; // 100MB
    @FXML
    private ListView<String> listviewFile;
    private String fileExtension;
    @FXML
    private Button btDeleteLíst;
    @FXML
    private Button btChooseFile;
    @FXML
    private Button btImportFile;
    public File draggedFile;


    @FXML
    public void ChooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (draggedFile != null) {
            selectedFile = draggedFile;
        } else {
            selectedFile = fileChooser.showOpenDialog(stage);
        }

        if (selectedFile != null) {
            fileSize = selectedFile.length();
            labelDragFile.setText(selectedFile.getName());
            fileExtension = getFileExtension(selectedFile);
        } else {
            showErrorMessageBox("Error", "No file selected");
        }
    }


    @FXML
    public void CheckImportFile(ActionEvent event) {
        File fileToCheck = selectedFile != null ? selectedFile : draggedFile;
        if (fileToCheck != null) {
            String fileExtension = getFileExtension(fileToCheck);
            long fileSize = fileToCheck.length();

            if (fileSize > 100 * 1024 * 1024) {
                showErrorMessageBox("Error", "Kích thước quá lớn");
            } else if (isValidTextFileExtension(fileExtension)) {
                showMessage("OK", "Import thành công");
                if (fileToCheck.getName().endsWith(".txt")  ) {
                    AikenFormatTxt.getInstance().checkFormat(fileToCheck.getPath());
                } else if (fileToCheck.getName().endsWith(".docx")) {
                    AikenFormatDocx.getInstance().checkFormat(fileToCheck.getPath());
                }

            } else {
                showErrorMessageBox("Error", "Sai định dạng tệp");
            }
        } else {
            showErrorMessageBox("Error", "Chưa chọn file");
        }
    }


    @FXML
    public void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            draggedFile = db.getFiles().get(0);
            //textDragFile.setVisible(false);
            labelDragFile.setText(draggedFile.getName());
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    // public Label textDragFile;
    @FXML
    public void handleDragEntered(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            // Đổi màu nền của nhãn khi tệp được kéo qua
            labelDragFile.setStyle("-fx-background-color: lightblue");
        }
        event.consume();
    }

    @FXML
    public void handleDragExited(DragEvent event) {
        // Đặt lại màu nền của nhãn khi tệp rời khỏi
        labelDragFile.setStyle("-fx-background-color: lightpink");
        event.consume();
    }

    @FXML
    public void DeleteList(ActionEvent event) {
        selectedFile = null;
        draggedFile = null;
        labelDragFile.setText("           DRAG FILE HERE");
    }

    private boolean isValidTextFileExtension(String extension) {
        return extension.equalsIgnoreCase("txt") || extension.equalsIgnoreCase("doc")
                || extension.equalsIgnoreCase("docx") || extension.equalsIgnoreCase("rtf");
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorMessageBox(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    /////////// 4.1
    /*@FXML
    public void import2(ActionEvent event) {
        if (selectedFile.getName().endsWith(".txt") ) {
            AikenFormatTxt.getInstance().checkFormat(selectedFile.getPath());
        } else if (selectedFile.getName().endsWith(".docx")) {
            AikenFormatDocx.getInstance().checkFormat(selectedFile.getPath());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Wrong format");
            alert.show();
        }
    } */
}
