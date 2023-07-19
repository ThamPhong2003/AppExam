package com.example.demo4;

import javafx.scene.control.Alert;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class AikenFormatTxt {

    public static AikenFormatTxt getInstance() {
        return new AikenFormatTxt();
    }


    public int checkLine(String choiceLine) {
        if (choiceLine.isEmpty()) {
            return -1;
        } else if (Character.isUpperCase(choiceLine.charAt(0)) && choiceLine.charAt(1) == '.'
                && choiceLine.charAt(2) == ' ') {
            return 0;
        } else if (choiceLine.length() == 9 && choiceLine.startsWith("ANSWER: ")) {
            return 2;
        } else {
            return 1;
        }
    }

    public void checkFormat(String path) {
        File file = new File(path);

        try {
            int numberQuestions = 0;
            int countChoice = -2;
            int result = 1;

            List<String> allLine = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            ArrayList<Character> keyChoice = new ArrayList<Character>();

            int numberLine = allLine.size();

            for (int i = 0; i < numberLine; i++) {
                String checkLine = allLine.get(i);
                if (AikenFormatTxt.getInstance().checkLine(checkLine) == 1 && countChoice == -2) {
                    countChoice = 0;
                    keyChoice.clear();

                } else if (AikenFormatTxt.getInstance().checkLine(checkLine) == 0 && countChoice >= 0) {
                    countChoice++;
                    keyChoice.add(checkLine.charAt(0));

                } else if (AikenFormatTxt.getInstance().checkLine(checkLine) == 2 && countChoice >= 2) {
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
                } else if (AikenFormatTxt.getInstance().checkLine(checkLine) == -1 && countChoice == -1) {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
