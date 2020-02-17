package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserAuthTokenDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthToken;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.upgrad.quora.service.util.MessageKeys.*;

@Service
public class AnswerBusinessService {


  @Autowired private QuestionDao questionDao;
  @Autowired private AnswerDao answerDao;
  @Autowired private UserAuthTokenDao userAuthTokenDao;

  /**
   * This method provides the QuestionEntity for given questionId after validation of QuestionEntity
   * @param questionId
   * @return QuestionEntity for given Question Id
   * @throws InvalidQuestionException
   */
  public QuestionEntity getQuestionById(String questionId) throws InvalidQuestionException {

    QuestionEntity questionEntity = questionDao.getQuestionById(questionId);

    if (questionEntity == null || questionId == null || questionEntity.getUuid() == null) {
      throw new InvalidQuestionException(QUES_001, QUESTION_ENTERED_INVALID);
    }

    return questionEntity;
  }

  /**
   * This method creates and saves AnswerEntity for given question after validation of User and QuestionEntity
   * @param answerEntity
   * @param accessToken
   * @param questionId
   * @return AnswerEntity which was created
   * @throws AuthorizationFailedException,InvalidQuestionException
   */
  public AnswerEntity createAnswer(AnswerEntity answerEntity, String accessToken, String questionId)
      throws AuthorizationFailedException, InvalidQuestionException {
    QuestionEntity questionEntity = getQuestionById(questionId);
    UserAuthToken userAuthEntity =
        validateUserAuthEntity(accessToken, USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_POST_AN_ANSWER);
    answerEntity.setUser(userAuthEntity.getUser());
    answerEntity.setQuestionEntity(questionEntity);
    return answerDao.save(answerEntity);
  }

  /**
   * This method is to edit AnswerEntity for given answerId after validation of User and AnswerEntity
   * @param answerId
   * @param accessToken
   * @param content
   * @return AnswerEntity which was edited
   * @throws AuthorizationFailedException,AnswerNotFoundException
   */
  public AnswerEntity editAnswer(
      final String answerId, final String accessToken, final String content)
      throws AuthorizationFailedException, AnswerNotFoundException {
    UserAuthToken userAuthEntity =
        validateUserAuthEntity(accessToken, USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_EDIT_AN_ANSWER);

    AnswerEntity answer = answerDao.getAnswerById(answerId);
    if (answer != null) {
      if (!(answer.getUser().getUuid().equals(userAuthEntity.getUser().getUuid()))) {
        throw new AuthorizationFailedException(ATHR_003, ONLY_ANSWER_OWNER_CAN_EDIT);
      }
    }

    if (answer == null || answerId == null || answer.getUuid() == null) {
      throw new AnswerNotFoundException(ANS_001, ANSWER_DOESNOT_EXIST);
    }

    answer.setAnswer(content);
    return answerDao.updateAnswer(answer);
  }

  /**
   * This method is to delete AnswerEntity for given answerId after validation of User and AnswerEntity
   * @param answerId
   * @param accessToken
   * @return AnswerId of deleted Answer
   * @throws AuthorizationFailedException,AnswerNotFoundException
   */
  public String deleteAnswer(String answerId, String accessToken)
      throws AuthorizationFailedException, AnswerNotFoundException {
    UserAuthToken userAuthEntity =
        validateUserAuthEntity(accessToken, USER_IS_SIGNED_OUT_TO_DELETE);

    AnswerEntity answerEntity = answerDao.getAnswerById(answerId);
    if (answerEntity != null) {
      if (!((answerEntity.getUser().getUuid().equals(userAuthEntity.getUser().getUuid()))
          || (userAuthEntity.getUser().getRole().equals(ADMIN)))) {
        throw new AuthorizationFailedException(ATHR_003, ONLY_OWNER_OR_ADMIN_CAN_DELETE);
      }
    }

    if (answerEntity == null || answerId == null || answerEntity.getUuid() == null) {
      throw new AnswerNotFoundException(ANS_001, ANSWER_DOESNOT_EXIST);
    }

    return answerDao.deleteAnswer(answerId);
  }

  /**
   * This method will provide list of all AnswerEntity for given question after validation of User and QuestionEntity
   * @param accessToken
   * @param questionId
   * @return List of all answers for given question
   * @throws AuthorizationFailedException,InvalidQuestionException
   */
  public List<AnswerEntity> getAllAnswersForQuestion(String accessToken, String questionId)
      throws AuthorizationFailedException, InvalidQuestionException {
    QuestionEntity questionEntity = questionDao.getQuestionById(questionId);

    if (questionEntity == null || questionId == null || questionEntity.getUuid() == null) {
      throw new InvalidQuestionException(QUES_001, QUESTION_ENTERED_UUID_DOES_NOT_EXIST);
    }

    UserAuthToken userAuthEntity =
        validateUserAuthEntity(accessToken, USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_GET_AN_ANSWER);

    List<AnswerEntity> answerEntities = answerDao.getAllAnswersForQuestion(questionEntity);

    return answerEntities;
  }

  /**
   * This method will validate the access token provided and if access token is valid and user is logged in then provide the UserAuthEntity object
   * @param accessToken
   * @param message
   * @return User Entity after validating access token and logout time
   * @throws AuthorizationFailedException
   */
  private UserAuthToken validateUserAuthEntity(String accessToken, String message)
      throws AuthorizationFailedException {
    UserAuthToken userAuthEntity = userAuthTokenDao.getUserAuthEntityByAccessToken(accessToken);
    if (userAuthEntity == null || userAuthEntity.getAccessToken() == null) {
      throw new AuthorizationFailedException(ATHR_001, USER_HAS_NOT_SIGNED_IN);
    } else if (userAuthEntity.getLogoutAt() != null) {
      throw new AuthorizationFailedException(ATHR_002, message);
    }

    return userAuthEntity;
  }
}
