package com.example.demo4;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class questionFromQBController implements Initializable {
    @FXML
    private TreeVierwHandle treeView4;
    public int IdParent4;
    private VBox parentVBox;
    public int CateGoryID;

    @FXML
    private VBox quesQuizVbox;
    @FXML
    private ChoiceBox<String> choiceBoxQuizQues;

    @FXML
    private TreeView<Category> treeViewQuizQues;
    private List<Question> selectedQuestions = new ArrayList<>();
    private BooleanProperty isCloseProperty = new SimpleBooleanProperty(false);
    public BooleanProperty isCloseProperty() {
        return isCloseProperty;
    }

    public void setIsClose(boolean value) {
        isCloseProperty.set(value);
    }
    int  selectedQuestionCount ; // Biến đếm số câu hỏi đã được chọn
    @FXML
    public Label ParentCountQues;

    public List<Question> getQuesList() {
        return selectedQuestions;
    }
    private static questionFromQBController instance;

    public static questionFromQBController getInstance() {
        return instance;
    }
    @FXML
    private CheckBox tick1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        instance = this;

        treeViewQuizQues.setVisible(false);
        treeView4 = new TreeVierwHandle(treeViewQuizQues, choiceBoxQuizQues);
        treeView4.start();

        /*treeViewQuizQues.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                IdParent4 = treeView4.getIdChoice();
            }
        }); */
        treeViewQuizQues.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateQuesShow(true);
            }
        });

                tick1.setOnMousePressed(event -> {
                    updateQuesShow(false);
                });

                /*
                quesQuizVbox.getChildren().clear();
                CateGoryID = treeView4.getIdChoice();
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
                        Text textQues = (Text) root.lookup("#quesText");
                        nameQues.setText(question.getName());
                        textQues.setText(question.getText());
                        CheckBox checkBox =  (CheckBox) root.lookup("#quesCheckbox") ;
                        checkBox.setOnMouseClicked(e -> {
                            if (checkBox.isSelected()) {
                                selectedQuestions.add(question);
                                selectedQuestionCount ++;
                            } else {
                                selectedQuestions.remove(question);
                                selectedQuestionCount --;
                            }
                            ParentCountQues.setText(String.format("%d.00", selectedQuestionCount)); // Cập nhật giá trị của countQues với định dạng .00

                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    quesQuizVbox.getChildren().add(root);
                }
                connector.disconnect(); */

    }
    void updateQuesShow(Boolean type) {
        quesQuizVbox.getChildren().clear();
        CateGoryID = treeView4.getIdChoice();
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
                Text textQues = (Text) root.lookup("#quesText");
                nameQues.setText(question.getName());
                textQues.setText(question.getText());
                CheckBox checkBox =  (CheckBox) root.lookup("#quesCheckbox") ;
                checkBox.setOnMouseClicked(e -> {
                    if (checkBox.isSelected()) {
                        selectedQuestions.add(question);
                        selectedQuestionCount ++;
                    } else {
                        selectedQuestions.remove(question);
                        selectedQuestionCount --;
                    }
                    ParentCountQues.setText(String.format("%d.00", selectedQuestionCount)); // Cập nhật giá trị của countQues với định dạng .00
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            quesQuizVbox.getChildren().add(root);
        }
        if(tick1.isSelected() == type){
            try{
                List<Category> allCategory= connector.getCategories(CateGoryID);
                for(Category category: allCategory)
                {
                    List<Question> questionss = connector.getQuestionsFromCategory(category.getId());
                    for (Question question : questionss) {

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("quesBar.fxml"));
                        Parent itemNode  = loader.load();


                            Text nameQues = (Text) itemNode.lookup("#quesName");
                            Text textQues = (Text) itemNode.lookup("#quesText");

                            CheckBox checkBox =  (CheckBox) itemNode.lookup("#quesCheckbox") ;
                            checkBox.setOnMouseClicked(e -> {
                                if (checkBox.isSelected()) {
                                    selectedQuestions.add(question);
                                    selectedQuestionCount ++;
                                } else {
                                    selectedQuestions.remove(question);
                                    selectedQuestionCount --;
                                }
                                ParentCountQues.setText(String.format("%d.00", selectedQuestionCount)); // Cập nhật giá trị của countQues với định dạng .00
                     });
                        nameQues.setText(question.getName());
                        textQues.setText(question.getText());
                        quesQuizVbox.getChildren().add(itemNode);
                  }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            connector.disconnect();

        }
    }

    @FXML
    public void addQuesToQuiz(ActionEvent event) {
        if (!selectedQuestions.isEmpty()) {
            for (Question question : selectedQuestions) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("quesBar2.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                    Text nameQues = (Text) root.lookup("#quesName");
                    Text textQues = (Text) root.lookup("#quesText");
                    nameQues.setText(question.getName());
                    textQues.setText(question.getText());
                    parentVBox.getChildren().add(root);
                    setIsClose(true);
                    //

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public double setParentCountQues(Label ParentCountQues) {
        this.ParentCountQues = ParentCountQues;
        PreviewQuizController pr = new PreviewQuizController();
        double total = pr.questionCount + Double.parseDouble(ParentCountQues.getText());
        return total;
    }


    public void setParentVBox(VBox parentVBox)  {
        this.parentVBox = parentVBox;
    }
    @FXML
    void seclectCate(MouseEvent event) {
        treeViewQuizQues.setVisible(!treeViewQuizQues.isVisible());
        //////
        /////
        DatabaseConnect connector = new DatabaseConnect();
        connector.connect();
        //connector.addQuestion(CateGoryID, qText, "", 1);
        // ListQuestionVIew.getItems().add(qText);
        connector.disconnect();
    }


}
