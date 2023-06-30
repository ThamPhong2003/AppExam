package com.example.demo4;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {


    //-------Chuyển sang Gui 2.1
    //-------------------------
    @FXML
    private BorderPane MainPane;
    @FXML
    private MenuItem btMenu1;

    @FXML
    public void switchToMenu1(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("Menu2.fxml"));
        MainPane.setCenter(view);
    }


    //------- chuyển sang Gui 3.2
    //-------------------------
    @FXML
    private Button btMenu2;

    @FXML
    public void switchToAddQuestion(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("AddQuestion.fxml"));
        MainPane.setCenter(view);
    }


    // Bài 4-------------------------
    ///-----------------------------------------
    @FXML
    private Button btDeleteLíst;
    @FXML
    private ListView<String> listviewFile;
    @FXML
    private Button btChooseFile;
    @FXML
    private Button btImportFile;
    private String fileExtension;
    private long fileSize;
    private long maxSize = 100 * 1024 * 1024; // 100MB
    private File selectedFile;


    @FXML
    public void ChooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            fileSize = selectedFile.length();
            listviewFile.getItems().add(selectedFile.getName());
            fileExtension = getFileExtension(selectedFile);
        } else {
            System.out.println("File is not valid");
        }
    }

    @FXML
    public int checkAikenFormat(String filePath) {
        int questionCount = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            String line;
            boolean isValidQuestion = false;
            boolean isValidAnswer = false;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (!isValidQuestion && !isValidAnswer) {
                    // Kiểm tra tiêu đề câu hỏi
                    if (line.endsWith("?")) {
                        isValidQuestion = true;
                        continue;
                    } else {
                        showErrorMessageBox("Lỗi", "Lỗi tiêu đề câu hỏi");
                        break;
                    }
                }

                if (isValidQuestion && !isValidAnswer) {
                    // Kiểm tra danh sách đáp án
                    if (line.matches("[A-Z]\\. .+")) {
                        continue;
                    } else if (line.startsWith("ANSWER: ") && line.length() > 9) {
                        char answer = line.charAt(8);
                        if (isValidAnswerChoice(answer)) {
                            questionCount++;
                            isValidQuestion = false;
                            isValidAnswer = false;
                            continue;
                        } else {
                            showErrorMessageBox("Lỗi", "Lỗi đáp án đúng");
                            break;
                        }
                    } else {
                        showErrorMessageBox("Lỗi", "Lỗi danh sách đáp án");
                        break;
                    }
                }
            }

            reader.close();

            if (questionCount > 0) {
                showMessage("Thành công", "Import thành công. Số câu hỏi: " + questionCount);
            } else {
                showErrorMessageBox("Lỗi", "Định dạng Aiken không hợp lệ");
            }
        } catch (IOException e) {
            showErrorMessageBox("Lỗi", "Đọc file");
        }

        return questionCount;
    }


    private boolean isValidQuestionTitle(String line) {
        String[] parts = line.split("\\.");
        return parts.length >= 1 && !parts[0].isEmpty() && !line.endsWith(".");
    }


    private boolean isValidAnswerChoice(char answer) {
        return answer >= 'A' && answer <= 'Z';
    }


    private boolean isValidAnswer(String line) {
        return line.matches("ANSWER:\\s[A-Z]\\s*");

    }
    @FXML
    public void CheckImportFile(ActionEvent event) {
        if (fileExtension == null || fileExtension.isEmpty()) {
            showErrorMessageBox("Error", "Chưa chọn file");
        } else if (!fileExtension.equalsIgnoreCase("txt")) {
            showErrorMessageBox("Wrong Format", "Định dạng tệp không hợp lệ");
        } else if (fileSize > maxSize) {
            showErrorMessageBox("Lỗi", "Tệp vượt quá giới hạn kích thước");
        } else {
            if (selectedFile != null) {
                int questionCount = checkAikenFormat(selectedFile.getPath());
                if (questionCount != -1) {
                    showMessage("Success", "Import thành công. Số câu hỏi theo định dạng Aiken: " + questionCount);
                } else {
                    showMessage("Success", "Định dạng Aiken không hợp lệ");
                }
            } else {
                showErrorMessageBox("Error", "Chưa chọn file");
            }
        }
    }

    private void showError(String message) {
        System.out.println("Error: " + message);
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
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
    @FXML
    public void DeleteList(ActionEvent event) {
        listviewFile.getItems().clear();

    }



    ///---------------
    //----------------


}


