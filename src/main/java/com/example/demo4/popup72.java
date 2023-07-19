package com.example.demo4;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

//import static jdk.nio.zipfs.ZipFileAttributeView.AttrID.permissions;

public class popup72 {
    public Quiz quiz;

    @FXML
    private AnchorPane gui72pane;

    @FXML
    void cancelPreviewQuiz(ActionEvent event) {
        // Lấy đối tượng Stage hiện tại từ sự kiện
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Đóng stage hiện tại
        currentStage.close();
    }
    public int quizID  ;
    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }
    public void setVariable(Quiz quiz) {
        this.quiz = quiz;     // You can use this variable to initialize or update the GUI elements in your controller
    }

    @FXML
    void exportExam(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu tệp PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            String fileName = file.getAbsolutePath();

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Mật khẩu");
            dialog.setHeaderText("Nhập mật khẩu để bảo vệ tệp PDF");
            dialog.setContentText("Mật khẩu:");

            // Hiển thị hộp thoại nhập mật khẩu
            Optional<String> result = dialog.showAndWait();
            String password = result.orElse(""); // Lấy mật khẩu từ kết quả nhập vào hoặc rỗng nếu không có nhập

            try {
                // Tạo tệp PDF
                PdfWriter writer = new PdfWriter(fileName, new WriterProperties().setStandardEncryption(
                        password.getBytes(),
                        password.getBytes(),
                        EncryptionConstants.ALLOW_PRINTING,
                        EncryptionConstants.ENCRYPTION_AES_256));

                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                PdfFont titleFont = PdfFontFactory.createFont("C://Windows/Fonts/Tahoma.ttf");

                Paragraph title = new Paragraph("Đề thi");
                title.setFont(titleFont);
                title.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(title);

                DatabaseConnect connector = new DatabaseConnect();
                connector.connect();

                String quizQuestionSql = "SELECT QuestionID FROM QUIZ_QUESTION WHERE quizID = ?";
                PreparedStatement quizQuestionStatement = connector.getConnection().prepareStatement(quizQuestionSql);
                quizQuestionStatement.setInt(1, quizID);
                ResultSet quizQuestionResultSet = quizQuestionStatement.executeQuery();

                while (quizQuestionResultSet.next()) {
                    int questionID = quizQuestionResultSet.getInt("QuestionID");

                    String questionSql = "SELECT id, text, name,mark FROM Question WHERE id = ?";
                    PreparedStatement questionStatement = connector.getConnection().prepareStatement(questionSql);
                    questionStatement.setInt(1, questionID);
                    ResultSet questionResultSet = questionStatement.executeQuery();

                    if (questionResultSet.next()) {
                        String questionText = questionResultSet.getString("text");
                        String questionName = questionResultSet.getString("name");

                        // Tạo đối tượng font cho nội dung câu hỏi
                        PdfFont questionFont = PdfFontFactory.createFont("C://Windows/Fonts/Tahoma.ttf");

                        // In nội dung câu hỏi
                        Paragraph question1 = new Paragraph(questionName);
                        question1.setFont(questionFont);
                        document.add(question1);

                        Paragraph question = new Paragraph(questionText);
                        question.setFont(questionFont);
                        document.add(question);

                        // Retrieve choices for the current question
                        String choiceSql = "SELECT id,text FROM Choice WHERE question_id = ?";
                        PreparedStatement choiceStatement = connector.getConnection().prepareStatement(choiceSql);
                        choiceStatement.setInt(1, questionID);
                        ResultSet choiceResultSet = choiceStatement.executeQuery();
                        PdfFont choiceFont = PdfFontFactory.createFont("C://Windows/Fonts/Tahoma.ttf");
                        // In ra các lựa chọn
                        while (choiceResultSet.next()) {
                            String choiceText = choiceResultSet.getString("text");

                            Paragraph choiceParagraph = new Paragraph(choiceText);
                            choiceParagraph.setFont(choiceFont);
                            document.add(choiceParagraph);
                        }

                        choiceResultSet.close();
                        choiceStatement.close();
                    }

                    questionResultSet.close();
                    questionStatement.close();
                }

                quizQuestionResultSet.close();
                quizQuestionStatement.close();

                // Đóng tệp PDF
                document.close();
                pdfDoc.close();
                //
                PdfReader protectedReader = new PdfReader(fileName, new ReaderProperties().setPassword(password.getBytes()));
                PdfDocument protectedPdfDoc = new PdfDocument(protectedReader);
                // Xử lý tệp PDF đã được bảo vệ

                protectedPdfDoc.close();
                //

                System.out.println(quizID);
                System.out.println("Xuất tệp PDF thành công!");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // Lấy đối tượng Stage hiện tại từ sự kiện
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Đóng stage hiện tại
            currentStage.close();
        }


    }


    @FXML
    void startAttempt(ActionEvent event) {
        try {
            // Tạo FXMLLoader để tải tệp FXML của giao diện mới
            FXMLLoader loader = new FXMLLoader(getClass().getResource("examStage.fxml"));
            Parent root = loader.load();
            examController controller = loader.getController(); // Lấy đối tượng controller từ FXMLLoader
            controller.setQuizID(quizID);
            controller.initData();


// Tạo một Stage mới và thiết lập Scene từ root
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            controller.startCountdown();



        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(quizID);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Đóng stage hiện tại
        currentStage.close();
    }
}


