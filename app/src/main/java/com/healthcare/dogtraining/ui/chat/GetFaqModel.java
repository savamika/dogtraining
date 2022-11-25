package com.healthcare.dogtraining.ui.chat;

public class GetFaqModel {

  String id,question,answer,status;


    public GetFaqModel(String id, String question, String answer, String status) {
        this.id=id;
        this.question=question;
        this.answer=answer;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
