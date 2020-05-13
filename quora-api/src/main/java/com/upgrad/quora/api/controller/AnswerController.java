package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerBusiness;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AnswerController {

  @Autowired private AnswerBusiness answerBusiness;

  @RequestMapping(
      method = RequestMethod.POST,
      path = "/question/{questionId}/answer/create",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<AnswerResponse> createAnswer(
      @PathVariable("questionId") final String questionUuid,
      @RequestHeader("authorization") final String accessToken,
      final String content)
      throws AuthorizationFailedException, InvalidQuestionException {
    AnswerEntity answerEntity = new AnswerEntity();
    answerEntity.setAnswer(content);
    answerEntity.setDate(ZonedDateTime.now());
    answerEntity.setUuid(UUID.randomUUID().toString());
    AnswerEntity createdAnswerEntity =
        answerBusiness.createAnswer(answerEntity, accessToken, questionUuid);
    AnswerResponse response =
        new AnswerResponse().id(createdAnswerEntity.getUuid()).status("ANSWER CREATED");

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @RequestMapping(
      method = RequestMethod.PUT,
      path = "/answer/edit/{answerId}",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<AnswerEditResponse> editAnswerContent(
      @PathVariable("answerId") final String answerId,
      @RequestHeader("authorization") final String accessToken,
      final AnswerRequest answerRequest)
      throws AuthorizationFailedException, AnswerNotFoundException {
    AnswerEntity editedAnsweredEntity =
        answerBusiness.editAnswer(answerId, accessToken, answerRequest.getAnswer());
    AnswerEditResponse answerEditResponse =
        new AnswerEditResponse().id(editedAnsweredEntity.getUuid()).status("ANSWER EDITED");
    return new ResponseEntity<>(answerEditResponse, HttpStatus.OK);
  }

  @RequestMapping(
      method = RequestMethod.DELETE,
      path = "/answer/delete/{answerId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<AnswerDeleteResponse> deleteAnswer(
      @PathVariable("answerId") final String answerId,
      @RequestHeader("authorization") final String accessToken)
      throws AuthorizationFailedException, AnswerNotFoundException {
    String deletedAnswerUuid = answerBusiness.deleteAnswer(answerId, accessToken);
    AnswerDeleteResponse answerDeleteResponse =
        new AnswerDeleteResponse().id(deletedAnswerUuid).status("ANSWER DELETED");
    return new ResponseEntity<>(answerDeleteResponse, HttpStatus.OK);
  }

  @RequestMapping(
      method = RequestMethod.GET,
      path = "answer/all/{questionId}",
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(
      @PathVariable("questionId") final String questionId,
      @RequestHeader("authorization") final String accessToken)
      throws AuthorizationFailedException, InvalidQuestionException {
    List<AnswerEntity> answerEntities =
        answerBusiness.getAllAnswersForQuestion(accessToken, questionId);
    List<AnswerDetailsResponse> answerDetailsResponses = new ArrayList<>();

    answerEntities.stream()
        .forEach(
            answerEntity -> {
              AnswerDetailsResponse response = new AnswerDetailsResponse();
              response.setId(answerEntity.getUuid());
              response.setQuestionContent(answerEntity.getQuestionEntity().getContent());
              response.setAnswerContent(answerEntity.getAnswer());
              answerDetailsResponses.add(response);
            });

    return new ResponseEntity<>(answerDetailsResponses, HttpStatus.OK);
  }
}
