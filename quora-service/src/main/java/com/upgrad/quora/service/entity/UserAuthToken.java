package com.upgrad.quora.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "user_auth")
@NamedQueries({
  @NamedQuery(
      name = "userAuthTokenByAccessToken",
      query = "select ut from UserAuthToken ut where ut.accessToken = :accessToken ")
})
public class UserAuthToken {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column
  @Size(max = 200)
  @NotNull
  private String uuid;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @Column(name = "access_token")
  @Size(max = 500)
  @NotNull
  private String accessToken;

  @Column(name = "expires_at")
  @NotNull
  private ZonedDateTime expiresAt;

  @Column(
      name = "login_at",
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
      updatable = false)
  @NotNull
  private ZonedDateTime loginAt;

  @Column(name = "logout_at")
  @Null
  private ZonedDateTime logoutAt;

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

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public ZonedDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(ZonedDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public ZonedDateTime getLoginAt() {
    return loginAt;
  }

  public void setLoginAt(ZonedDateTime loginAt) {
    this.loginAt = loginAt;
  }

  public ZonedDateTime getLogoutAt() {
    return logoutAt;
  }

  public void setLogoutAt(ZonedDateTime logoutAt) {
    this.logoutAt = logoutAt;
  }
}
