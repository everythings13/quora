package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userprofile")
public class CommonController {
  private static String MSG_WHEN_USER_SIGNED_OUT =
      "User is signed out.Sign in first to get user details";
  private static String MSG_WHEN_USER_NOT_FOUND = "User with entered uuid does not exist";
  @Autowired private UserBusinessService userBusinessService;

  @RequestMapping(
      method = RequestMethod.GET,
      path = "/{userId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserDetailsResponse> getUser(
      @PathVariable("userId") final String userUuid,
      @RequestHeader("authorization") final String authorization)
      throws AuthorizationFailedException, UserNotFoundException {
    userBusinessService.getUserAuthByAccessToken(authorization, MSG_WHEN_USER_SIGNED_OUT);
    User user = userBusinessService.getValidatedGivenUser(userUuid, MSG_WHEN_USER_NOT_FOUND);
    UserDetailsResponse userDetailsResponse =
        new UserDetailsResponse()
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .emailAddress(user.getEmail())
            .contactNumber(user.getContactNumber())
            .userName(user.getUserName())
            .aboutMe(user.getAboutMe())
            .country(user.getCountry())
            .dob(user.getDob());
    return new ResponseEntity<>(userDetailsResponse, HttpStatus.OK);
  }
}
