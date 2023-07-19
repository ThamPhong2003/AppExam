package com.example.demo4;


import javafx.scene.control.Alert;
import org.apache.poi.xwpf.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AikenFormatDocx {
    public static AikenFormatDocx getInstance(){return new AikenFormatDocx();}

    public int checkLine(String choiceLine) {
        if (choiceLine.isEmpty()) {
            return -1;
        } else if (Character.isUpperCase(choiceLine.charAt(0)) && choiceLine.charAt(1) == '.' && choiceLine.charAt(2) == ' ') {
            return 0;
        } else if (choiceLine.length() == 9 && choiceLine.startsWith("ANSWER: ")) {
            return 2;
        } else if (choiceLine.startsWith("image")) {
            return 3;
        } else {
            return 1;
        }
    }

    public void checkFormat(String path) {
        File file = new File(path);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(fileInputStream);

            int numberQuestions = 0;
            int countpicture=0;
            int countChoice = -2;
            int result = 1;
            Map<Integer,XWPFPictureData> listpicture=new HashMap<>();
            List<String> allLine = new ArrayList<>();
            for (int i = 0; i < document.getParagraphs().size(); i++) {
                XWPFParagraph paragraph = document.getParagraphs().get(i);
                // Lấy văn bản của đoạn
                String paragraphText = paragraph.getText();
                // Kiểm tra xem đoạn có chứa ảnh không
                if (paragraph.getRuns().size() > 0 && paragraph.getRuns().get(0).getEmbeddedPictures().size() > 0) {
                    // Lấy vị trí dòng của ảnh
                    String pictureName = paragraph.getRuns().get(0).getEmbeddedPictures().get(0).getPictureData().getFileName();
                    allLine.add(pictureName);
                    listpicture.put(i,paragraph.getRuns().get(0).getEmbeddedPictures().get(0).getPictureData());
                }
                else allLine.add(paragraphText);
            }
            ArrayList<Character> keyChoice = new ArrayList<Character>();
            int numberLine = allLine.size();
            for (int i = 0; i < numberLine; i++) {
                String checkLine = allLine.get(i);
                if (AikenFormatDocx.getInstance().checkLine(checkLine) == 1 && countChoice == -2) {
                    countChoice = 0;
                    countpicture=0;
                    keyChoice.clear();
                } else if (AikenFormatDocx.getInstance().checkLine(checkLine)==3 && countChoice >= 0) {
                    countpicture++;
                } else if (AikenFormatDocx.getInstance().checkLine(checkLine) == 0 && countChoice >= 0) {
                    countChoice++;
                    keyChoice.add(checkLine.charAt(0));

                } else if (AikenFormatDocx.getInstance().checkLine(checkLine) == 2 && countChoice >= 2) {
                    if (keyChoice.contains(checkLine.charAt(8))) {
                        countChoice = -1;
                        numberQuestions++;
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Error at " + (i + 1));
                        alert.show();
                        result = 0;

                        break;
                    }
                } else if (AikenFormatDocx.getInstance().checkLine(checkLine) == -1 && countChoice == -1) {
                    countChoice = -2;

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Error at " + (i + 1));
                    alert.show();
                    result = 0;
                    break;
                }
            }

            if (result == 1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Success: " + numberQuestions);
                alert.show();

            }
            document.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
