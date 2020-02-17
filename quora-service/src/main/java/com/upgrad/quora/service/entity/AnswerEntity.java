package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "answer")
@NamedQueries({
  @NamedQuery(
      name = "getAllAnswersForQuestion",
      query = "SELECT a FROM AnswerEntity a where a.questionEntity = :question"),
  @NamedQuery(name = "getAnswerById", query = "SELECT a FROM AnswerEntity a where a.uuid = :uuid")
})
public class AnswerEntity implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "uuid")
  @Size(max = 200)
  private String uuid;

  @Column(name = "ans")
  @Size(max = 255)
  private String answer;

  @Column(name = "date")
  private ZonedDateTime date;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "question_id")
  private QuestionEntity questionEntity;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public ZonedDateTime getDate() {
    return date;
  }

  public void setDate(ZonedDateTime date) {
    this.date = date;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public QuestionEntity getQuestionEntity() {
    return questionEntity;
  }

  public void setQuestionEntity(QuestionEntity questionEntity) {
    this.questionEntity = questionEntity;
  }
}
