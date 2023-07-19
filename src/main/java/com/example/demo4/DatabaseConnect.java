package com.example.demo4;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DatabaseConnect {
    private Connection connection;
    private final String databaseName;
    public DatabaseConnect() {
        this.databaseName = "database.db";
    }
    public void connect() {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Establish connection to the database
            connection = DriverManager.getConnection("jdbc:sqlite:" + "database.db");

            System.out.println("Connected to the database.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }
    public Connection getConnection() {
        return connection;
    }
    public String addCategory(int parentId, String info, String name) {
        try {
            // Prepare SQL statement
            String sql = "INSERT INTO Category (parent_id, info, name) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the parameter values
            statement.setInt(1, parentId);
            statement.setString(2, info);
            statement.setString(3, name);
            statement.executeUpdate();
            return "Category added successfully!";
        } catch (SQLException e) {
            return ("Failed to add category: " + e.getMessage());
        }
    }
    public Category getCategory(int CategoryId) {
        List<Category> categories = new ArrayList<>();

        try {
            // Prepare SQL statement
            String sql = "SELECT * FROM Category WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, CategoryId);
            // Execute the SQL statement
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int parentId = resultSet.getInt("parent_id");
                String info = resultSet.getString("info");
                String name = resultSet.getString("name");
                // Create a new Category object
                //if(parentId!=CategoryId)
                return new Category(CategoryId, parentId, info, name);
                // Category category = new Category(CategoryId, parentId, info, name);

                // Add the category to the list
                //categories.add(category);
            }

            // Close the result set
            resultSet.close();
        } catch (SQLException e) {
            System.err.println("Failed to retrieve categories: " + e.getMessage());
        }

        //return categories;
        return new Category(CategoryId,-1,"","");
    }
    public void addChoice(int questionId, float grade, String pic, String text) {
        try {
            String sql = "INSERT INTO Choice (question_id, grade, pic, text) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, questionId);
            statement.setFloat(2, grade);
            statement.setString(3, pic);
            statement.setString(4, text);
            statement.executeUpdate();
            System.out.println("Choice added successfully");
        }

        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Choice> getChoicesFromQuestion(int questionId) {
        List<Choice> choices = new ArrayList<>();
        String sql = "SELECT * FROM Choice WHERE question_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, questionId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int questionIdResult = resultSet.getInt("question_id");
                float grade = resultSet.getFloat("grade");
                String text = resultSet.getString("text");

                Choice choice = new Choice(id, questionIdResult,text,grade);
                choices.add(choice);
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return choices;
    }
    public void addQuizQues( int  quizID, int  QuestionID ){
        try  {
        String sql =  "INSERT INTO QUIZ_QUESTION ( quizID, QuestionID) VALUES ( ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, quizID);
        statement.setInt(2, QuestionID);
        statement.executeUpdate();
            System.out.println("New QuizQues added successfully!");
         } catch (SQLException e) {
         e.printStackTrace();
       }

    }
    public List<QuizQuest> getQuizQues() {
        List<QuizQuest> QuizQuest  = new ArrayList<>();

        try {
            String sql = "SELECT * FROM QUIZ_QUESTION";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int quizID = resultSet.getInt("quizID");
                int QuestionID = resultSet.getInt("QuestionID");
                QuizQuest quiz_ques = new QuizQuest(quizID, QuestionID);
                QuizQuest.add(quiz_ques);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return QuizQuest;
    }

    public void addQuiz( String name, double time, boolean shuffle, double totalMark) {
        try  {
            String sql =  "INSERT INTO QUIZ( name, time, shuffle, totalmark) VALUES ( ?, ?, ?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setDouble(2, time);
            statement.setBoolean(3, shuffle);
            statement.setDouble(4, totalMark);
            statement.executeUpdate();
            System.out.println("New Quiz added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public int addQuestion(int categoryId, String questionText, String questionName, float questionMark) {
        int questionId = -1; // Default value in case of failure

        try {
            // Prepare SQL statement with parameter placeholders
            String sql = "INSERT INTO Question (category_id, text, name, mark) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set values for the parameters
            statement.setInt(1, categoryId);
            statement.setString(2, questionText);
            statement.setString(3, questionName);
            statement.setFloat(4, questionMark);

            // Execute the SQL statement
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                // Retrieve the generated keys (in this case, the question ID)
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    questionId = generatedKeys.getInt(1);
                    System.out.println("Question added successfully. ID: " + questionId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to add question: " + e.getMessage());
        }

        return questionId;
    }
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to disconnect from the database: " + e.getMessage());
        }
    }
    public List<Category> getCategories(int CategoryId) {
        List<Category> categories = new ArrayList<>();

        try {
            // Prepare SQL statement
            String sql = "SELECT * FROM Category";
            PreparedStatement statement = connection.prepareStatement(sql);
            // Execute the SQL statement
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int Id = resultSet.getInt("id");
                int parentId = resultSet.getInt("parent_id");
                String info = resultSet.getString("info");
                String name = resultSet.getString("name");
                // Create a new Category object
                Category category = new Category(Id, parentId, info, name);
                // Add the category to the list

                if(parentId==CategoryId) {
                    categories.add(category);
                    categories.addAll(getCategories(Id));
                }
            }

            // Close the result set
            resultSet.close();
        } catch (SQLException e) {
            System.err.println("Failed to retrieve categories: " + e.getMessage());
        }
        return categories;
    }
    public List<Question> getQuestionsFromCategory(int categoryId) {
        List<Question> questions = new ArrayList<>();

        try {
            // Prepare SQL statement
            String sql = "SELECT * FROM Question WHERE category_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the parameter value
            statement.setInt(1, categoryId);

            // Execute the SQL statement
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int questionId = resultSet.getInt("id");
                String text = resultSet.getString("text");
                String name = resultSet.getString("name");
                float mark = resultSet.getFloat("mark");
                Question question = new Question(questionId, categoryId, text, name, mark);
                questions.add(question);
            }
        } catch (SQLException e) {
            System.err.println("Failed to get questions from category: " + e.getMessage());
        }
        return questions;
    }
    public void deleteQues(int questionId) {
        String sql = "DELETE FROM Question WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, questionId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Question with ID " + questionId + " deleted successfully.");
            } else {
                System.out.println("Question with ID " + questionId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteChoice(int choiceId) {
        String sql = "DELETE FROM Choice WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, choiceId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Choice with ID " + choiceId + " deleted successfully.");
            } else {
                System.out.println("Choice with ID " + choiceId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Quiz> getQuiz() {
        List<Quiz> quizzes  = new ArrayList<>();

        try {
            String sql = "SELECT * FROM QUIZ";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("quizID");
                String name = resultSet.getString("name");
                double time = resultSet.getDouble("time");
                boolean shuffle = resultSet.getBoolean("shuffle");
                double totalMark = resultSet.getDouble("totalmark");

                Quiz quiz = new Quiz(id, name, time, shuffle, totalMark);
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }


    public static void main(String[] args) {
        // Create an instance of DatabaseConnector
        DatabaseConnect connector = new DatabaseConnect();

        // Connect to the database
        connector.connect();
        //connector.disconnect();
    }

}
