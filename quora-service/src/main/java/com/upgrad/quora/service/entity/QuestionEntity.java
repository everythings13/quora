package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "question")
@NamedQueries({
  @NamedQuery(name = "getAllQuestions", query = "SELECT q FROM QuestionEntity q"),
  @NamedQuery(
      name = "getQuestionByUserId",
      query = "SELECT q FROM QuestionEntity q where q.user = :userId"),
  @NamedQuery(
      name = "getQuestionById",
      query = "SELECT q FROM QuestionEntity q where q.uuid = :uuid")
})
public class QuestionEntity implements Serializable {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "UUID")
  @Size(max = 200)
  private String uuid;

  @Column(name = "content")
  @Size(max = 500)
  private String content;

  @Column(name = "date")
  private ZonedDateTime date;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

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

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
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
}
