package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SigninResponse;
import com.upgrad.quora.api.model.SignupUserRequest;
import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.SignInAuthenticationService;
import com.upgrad.quora.service.business.SignupService;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuthToken;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

  private static final String SALT = "1234abc";
  private static final String NON_ADMIN = "nonAdmin";

  @Autowired private SignupService signupService;

  @Autowired private SignInAuthenticationService signInAuthenticationService;

  @RequestMapping(
      method = RequestMethod.POST,
      path = "/signup",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignupUserResponse> signUp(final SignupUserRequest signupUserRequest)
      throws SignUpRestrictedException {
    signupService.validateUserByUsernameAndEmail(
        signupUserRequest.getUserName(), signupUserRequest.getEmailAddress());
    User user = getUser(signupUserRequest);
    User createdUser = signupService.signUp(user);
    return new ResponseEntity<>(
        new SignupUserResponse().id(createdUser.getUuid()).status("USER SUCCESSFULLY REGISTERED"),
        HttpStatus.CREATED);
  }

  @RequestMapping(
      method = RequestMethod.POST,
      path = "/signin",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SigninResponse> signIn(
      @RequestHeader("encodedCredentials") final String encodedCredentials)
      throws AuthenticationFailedException {
    byte[] decode = Base64.getDecoder().decode(encodedCredentials.split("Basic ")[1]);
    String decodedText = new String(decode);
    String[] decodedArray = decodedText.split(":");
    UserAuthToken userAuthToken =
        signInAuthenticationService.authenticate(decodedArray[0], decodedArray[1]);
    HttpHeaders headers = new HttpHeaders();
    headers.add("access-token", userAuthToken.getAccessToken());
    return new ResponseEntity<>(
        new SigninResponse()
            .id(userAuthToken.getUser().getUuid())
            .message("SIGNED IN SUCCESSFULLY"),
        headers,
        HttpStatus.OK);
  }

  private User getUser(SignupUserRequest signupUserRequest) {
    User user = new User();
    user.setUuid(UUID.randomUUID().toString());
    user.setFirstName(signupUserRequest.getFirstName());
    user.setLastName(signupUserRequest.getLastName());
    user.setEmail(signupUserRequest.getEmailAddress());
    user.setPassword(signupUserRequest.getPassword());
    user.setSalt(SALT);
    user.setUserName(signupUserRequest.getUserName());
    user.setCountry(signupUserRequest.getCountry());
    user.setAboutMe(signupUserRequest.getAboutMe());
    user.setDob(signupUserRequest.getDob());
    user.setContactNumber(signupUserRequest.getContactNumber());
    user.setRole(NON_ADMIN);
    return user;
  }
}
