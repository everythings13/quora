package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author : Harika Etamukkala
 * QuestionBusinessService will contain business logic related to Question feature
 */
@Service
public class QuestionBusinessService {

    public static final String USER_HAS_NOT_SIGNED_IN = "User has not signed in";
    public static final String USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_POST_A_QUESTION = "User is signed out.Sign in first to post a question";
    public static final String ONLY_THE_QUESTION_OWNER_CAN_EDIT_THE_QUESTION = "Only the question owner can edit the question";
    public static final String ONLY_THE_QUESTION_OWNER_OR_ADMIN_CAN_DELETE_THE_QUESTION = "Only the question owner or admin can delete the question";
    public static final String USER_WITH_ENTERED_UUID_WHOSE_QUESTION_DETAILS_ARE_TO_BE_SEEN_DOES_NOT_EXIST = "User with entered uuid whose question details are to be seen does not exist";
    public static final String ATHR_001 = "ATHR-001";
    public static final String ATHR_002 = "ATHR-002";
    public static final String ATHR_003 = "ATHR-003";
    public static final String QUES_001 = "QUES-001";
    public static final String ENTERED_QUESTION_UUID_DOES_NOT_EXIST = "Entered question uuid does not exist";
    public static final String USR_001 = "USR-001";

    @Autowired
    private QuestionDao questionDao;


    /**
     * This method is used to create Question
     * @param accessToken
     * @param questionEntity
     * @return QuestionEntity
     * @throws AuthorizationFailedException
     */
    public QuestionEntity createQuestion(String accessToken, QuestionEntity questionEntity) throws AuthorizationFailedException {
        UserAuthEntity userAuthToken = questionDao.getAuthToken(accessToken);
        if (userAuthToken == null || userAuthToken.getAccessToken() == null) {
            throw new AuthorizationFailedException(ATHR_001, USER_HAS_NOT_SIGNED_IN);
        } else if (userAuthToken.getLogoutAt() != null) {
            throw new AuthorizationFailedException(ATHR_002, USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_POST_A_QUESTION);
        }
        questionEntity.setUser(userAuthToken.getUser());
        return questionDao.save(questionEntity);

    }

    /**
     * This method is used to get all questions from database
     * @param accessToken
     * @return List<QuestionEntity>
     * @throws AuthorizationFailedException
     */
    public List<QuestionEntity> getAllQuestions(String accessToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthToken = questionDao.getAuthToken(accessToken);
        if (userAuthToken == null || userAuthToken.getAccessToken() == null) {
            throw new AuthorizationFailedException(ATHR_001, USER_HAS_NOT_SIGNED_IN);
        } else if (userAuthToken.getLogoutAt() != null) {
            throw new AuthorizationFailedException(ATHR_002, USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_POST_A_QUESTION);
        }
        return questionDao.getAllQuestions();
    }

    /**
     * @param questionId
     * @param accessToken
     * @param questionEntity
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    public QuestionEntity editQuestion(String questionId, String accessToken, QuestionEntity questionEntity) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthToken = questionDao.getAuthToken(accessToken);
        QuestionEntity questionFromDb = questionDao.getQuestionById(questionId);
        if (userAuthToken == null || userAuthToken.getAccessToken() == null) {
            throw new AuthorizationFailedException(ATHR_001, USER_HAS_NOT_SIGNED_IN);
        } else if (userAuthToken.getLogoutAt() != null) {
            throw new AuthorizationFailedException(ATHR_002, USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_POST_A_QUESTION);
        } else if (questionFromDb != null && !(questionFromDb.getUser().getUuid().equals(userAuthToken.getUser().getUuid()))) {
            throw new AuthorizationFailedException(ATHR_003, ONLY_THE_QUESTION_OWNER_CAN_EDIT_THE_QUESTION);
        } else if (questionFromDb == null || questionEntity.getUuid() == null) {
            throw new InvalidQuestionException(QUES_001, ENTERED_QUESTION_UUID_DOES_NOT_EXIST);
        }
        return questionDao.save(questionEntity);

    }

    /**
     * @param questionId
     * @param accessToken
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    public String deleteQuestion(String questionId, String accessToken) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthToken = questionDao.getAuthToken(accessToken);
        QuestionEntity questionFromDb = questionDao.getQuestionById(questionId);
        if (userAuthToken == null || userAuthToken.getAccessToken() == null) {
            throw new AuthorizationFailedException(ATHR_001, USER_HAS_NOT_SIGNED_IN);
        } else if (userAuthToken.getLogoutAt() != null) {
            throw new AuthorizationFailedException(ATHR_002, USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_POST_A_QUESTION);
        } else if (questionFromDb != null && (questionFromDb.getUser().getUuid().equals(userAuthToken.getUser().getUuid()) || userAuthToken.getUser().getRole().equals("nonadmin"))) {
            throw new AuthorizationFailedException(ATHR_003, ONLY_THE_QUESTION_OWNER_OR_ADMIN_CAN_DELETE_THE_QUESTION);
        } else if (questionFromDb == null) {
            throw new InvalidQuestionException(QUES_001, ENTERED_QUESTION_UUID_DOES_NOT_EXIST);
        }
        return questionDao.deleteQuestion(questionId);
    }

    /**
     * @param userId
     * @param accessToken
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     * @throws UserNotFoundException
     */
    public List<QuestionEntity> getAllQuestionsByUser(String userId, String accessToken) throws AuthorizationFailedException, InvalidQuestionException, UserNotFoundException {
        UserAuthEntity userAuthToken = questionDao.getAuthToken(accessToken);

        if (userAuthToken == null || userAuthToken.getAccessToken() == null) {
            throw new AuthorizationFailedException(ATHR_001, USER_HAS_NOT_SIGNED_IN);
        } else if (userAuthToken.getLogoutAt() != null) {

            throw new AuthorizationFailedException(ATHR_002, USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_POST_A_QUESTION);
        }
        UserEntity userEntity = questionDao.getUser(userId);

        List<QuestionEntity> questions = questionDao.getAllQuestionsByUser(userEntity);
        if (questions.isEmpty()) {
            throw new UserNotFoundException(USR_001, USER_WITH_ENTERED_UUID_WHOSE_QUESTION_DETAILS_ARE_TO_BE_SEEN_DOES_NOT_EXIST);
        }
        return questions;

    }
}
