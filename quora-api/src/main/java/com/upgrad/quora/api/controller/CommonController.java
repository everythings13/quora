package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.User;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userprofile")
public class CommonController {
  @Autowired private UserBusinessService userBusinessService;

  @RequestMapping(
      method = RequestMethod.GET,
      path = "/{userId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserDetailsResponse> getUser(
      final String userUuid, @RequestHeader("authorization") final String authorization)
      throws AuthenticationFailedException, UserNotFoundException {
    User user = userBusinessService.getUserAuthByAccessToken(authorization, userUuid);
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
