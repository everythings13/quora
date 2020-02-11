package com.upgrad.quora.service.entity;

import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "users")
@NamedQueries({
  @NamedQuery(name = "userByEmail", query = "select u from User u where u.email =:email"),
  @NamedQuery(name = "userByUsername", query = "select u from User u where u.userName =:username")
})
public class User implements Serializable {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column
  @Size(max = 200)
  @NotNull
  private String uuid;

  @Column(name = "firstname")
  @Size(max = 30)
  @NotNull
  private String firstName;

  @Column(name = "lastname")
  @Size(max = 30)
  @NotNull
  private String lastName;

  @Column(name = "username", unique = true)
  @Size(max = 30)
  @NotNull
  private String userName;

  @Column(unique = true)
  @Size(max = 50)
  @NotNull
  private String email;

  @ToStringExclude
  @Column
  @Size(max = 255)
  @NotNull
  private String password;

  @Column
  @Size(max = 200)
  @ToStringExclude
  @NotNull
  private String salt;

  @Column
  @Size(max = 30)
  private String country;

  @Column(name = "aboutme")
  @Size(max = 30)
  private String aboutMe;

  @Column
  @Size(max = 30)
  private String dob;

  @Column
  @Size(max = 30)
  private String role;

  @Size(max = 30)
  @Column(name = "contactnumber")
  private String contactNumber;

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

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getAboutMe() {
    return aboutMe;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public String getDob() {
    return dob;
  }

  public void setDob(String dob) {
    this.dob = dob;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getContactNumber() {
    return contactNumber;
  }

  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }
}
