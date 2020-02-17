package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Author : Harika Etamukkala */
@RestController
@RequestMapping("/")
public class QuestionController {

  @Autowired private QuestionBusinessService questionBusinessService;

  /**
   * @param content
   * @param accessToken
   * @return QuestionResponse
   * @throws AuthorizationFailedException
   */
  @RequestMapping(
      method = RequestMethod.POST,
      path = "/question/create",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionResponse> createQuestion(
      final String content, @RequestHeader("authorization") final String accessToken)
      throws AuthorizationFailedException {
    QuestionEntity questionEntity = new QuestionEntity();
    questionEntity.setContent(content);
    questionEntity.setUuid(UUID.randomUUID().toString());
    questionEntity.setDate(ZonedDateTime.now());
    QuestionEntity createdQuestion =
        questionBusinessService.createQuestion(accessToken, questionEntity);
    QuestionResponse response =
        new QuestionResponse().id(createdQuestion.getUuid()).status("QUESTION CREATED");
    return new ResponseEntity<QuestionResponse>(response, HttpStatus.CREATED);
  }

  /**
   * @param accessToken
   * @return QuestionDetailsResponse
   * @throws AuthorizationFailedException
   */
  @RequestMapping(
      method = RequestMethod.GET,
      path = "/question/all",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(
      @RequestHeader("authorization") final String accessToken)
      throws AuthorizationFailedException {
    List<QuestionEntity> allQuestions = questionBusinessService.getAllQuestions(accessToken);
    List<QuestionDetailsResponse> responses = new ArrayList<>();
    allQuestions.stream()
        .forEach(
            question -> {
              QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
              questionDetailsResponse.content(question.getContent());
              questionDetailsResponse.id(question.getUuid());
              responses.add(questionDetailsResponse);
            });
    return new ResponseEntity<List<QuestionDetailsResponse>>(responses, HttpStatus.OK);
  }

  /**
   * @param questionId
   * @param accessToken
   * @param content
   * @return QuestionEditResponse
   * @throws AuthorizationFailedException
   * @throws InvalidQuestionException
   */
  @RequestMapping(
      method = RequestMethod.PUT,
      path = "/question/edit/{questionId}",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionEditResponse> editQuestionContent(
      @PathVariable("questionId") String questionId,
      @RequestHeader("authorization") final String accessToken,
      final String content)
      throws AuthorizationFailedException, InvalidQuestionException {
    QuestionEntity questionEntity = new QuestionEntity();
    questionEntity.setContent(content);
    QuestionEntity editedQuestion =
        questionBusinessService.editQuestion(questionId, accessToken, questionEntity);
    QuestionEditResponse questionEditResponse =
        new QuestionEditResponse().id(editedQuestion.getUuid()).status("QUESTION EDITED");
    return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
  }

  /**
   * @param questionId
   * @param accessToken
   * @return QuestionDeleteResponse
   * @throws AuthorizationFailedException
   * @throws InvalidQuestionException
   */
  @RequestMapping(
      method = RequestMethod.DELETE,
      path = "/question/delete/{questionId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<QuestionDeleteResponse> deleteQuestion(
      @PathVariable("questionId") String questionId,
      @RequestHeader("authorization") final String accessToken)
      throws AuthorizationFailedException, InvalidQuestionException {
    String deletedQuestionUuid = questionBusinessService.deleteQuestion(questionId, accessToken);
    QuestionDeleteResponse deleteResponse =
        new QuestionDeleteResponse().id(deletedQuestionUuid).status("QUESTION DELETED");
    return new ResponseEntity<QuestionDeleteResponse>(deleteResponse, HttpStatus.OK);
  }

  /**
   * @param userId
   * @param accessToken
   * @return list of QuestionDetailsResponse
   * @throws UserNotFoundException
   * @throws AuthorizationFailedException
   * @throws InvalidQuestionException
   */
  @RequestMapping(
      method = RequestMethod.GET,
      path = "question/all/{userId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByuser(
      @PathVariable("userId") String userId,
      @RequestHeader("authorization") final String accessToken)
      throws UserNotFoundException, AuthorizationFailedException, InvalidQuestionException {
    List<QuestionEntity> questions =
        questionBusinessService.getAllQuestionsByUser(userId, accessToken);
    List<QuestionDetailsResponse> responses = new ArrayList<>();
    questions.stream()
        .forEach(
            question -> {
              QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
              questionDetailsResponse.content(question.getContent());
              questionDetailsResponse.id(question.getUuid());
              responses.add(questionDetailsResponse);
            });
    return new ResponseEntity<List<QuestionDetailsResponse>>(responses, HttpStatus.OK);
  }
}
