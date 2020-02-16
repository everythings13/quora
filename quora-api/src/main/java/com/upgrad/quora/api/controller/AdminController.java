package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.SignupUserResponse;
import com.upgrad.quora.service.business.AdminService;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.entity.UserAuthToken;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private static String MSG_WHEN_USER_SIGNED_OUT = "User is signed out";
  private static String MSG_WHEN_USER_NOT_FOUND =
      "User with entered uuid to be deleted does not exist";

  @Autowired private UserBusinessService userBusinessService;
  @Autowired private AdminService adminService;

  @RequestMapping(
      method = RequestMethod.DELETE,
      path = "/user/{userId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SignupUserResponse> deleteUser(
      @PathVariable("userId") final String userUuid,
      @RequestHeader("authorization") final String authorization)
      throws UserNotFoundException, AuthorizationFailedException {

    UserAuthToken userAuthToken =
        userBusinessService.getUserAuthByAccessToken(authorization, MSG_WHEN_USER_SIGNED_OUT);
    adminService.validateIfRoleIsAdmin(userAuthToken, userUuid);
    User user = userBusinessService.getValidatedGivenUser(userUuid, MSG_WHEN_USER_NOT_FOUND);
    String uuid = adminService.deleteUser(user);
    return new ResponseEntity<>(
        new SignupUserResponse().id(uuid).status("USER SUCCESSFULLY DELETED"), HttpStatus.OK);
  }
}
