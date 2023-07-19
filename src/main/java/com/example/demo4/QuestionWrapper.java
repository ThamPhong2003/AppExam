package com.example.demo4;

public class QuestionWrapper{
        private Question question;
        private boolean deleted;

        public QuestionWrapper(Question question) {
            this.question = question;
            this.deleted = false;
        }

        public Question getQuestion() {
            return question;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }
}
