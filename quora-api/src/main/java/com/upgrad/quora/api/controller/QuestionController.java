package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDeleteResponse;
import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditResponse;
import com.upgrad.quora.api.model.QuestionResponse;
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

/** Contains all the APIs for Question feature Author : Harika Etamukkala */
@RestController
@RequestMapping("/")
public class QuestionController {

  @Autowired private QuestionBusinessService questionBusinessService;

  /**
   * Returns QuestionResponse by creating new question with content provided by user
   *
   * @param content - content
   * @param accessToken - used for authorization
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
   * Returns all questions
   *
   * @param accessToken - used for user authorization
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
    copyProperties(allQuestions, responses);
    return new ResponseEntity<List<QuestionDetailsResponse>>(responses, HttpStatus.OK);
  }

  /**
   * Returns QuestionEditResponse by editing question
   *
   * @param questionId - uuid of question
   * @param accessToken - user authorization
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
   * Returns QuestionDeleteResponse by deleting the question
   *
   * @param questionId - uuid of question
   * @param accessToken - user authorization
   * @return QuestionDeleteResponse - which contains uuid of deleted question and status
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
   * Returns list of questions created by user
   *
   * @param userId - unique id of user
   * @param accessToken - user authorization
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
    copyProperties(questions, responses);
    return new ResponseEntity<List<QuestionDetailsResponse>>(responses, HttpStatus.OK);
  }

  private void copyProperties(
      List<QuestionEntity> allQuestions, List<QuestionDetailsResponse> responses) {
    allQuestions.stream()
        .forEach(
            question -> {
              QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
              questionDetailsResponse.content(question.getContent());
              questionDetailsResponse.id(question.getUuid());
              responses.add(questionDetailsResponse);
            });
  }
}
