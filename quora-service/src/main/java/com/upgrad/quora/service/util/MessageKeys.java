package com.upgrad.quora.service.util;

public final class MessageKeys {

    //Authorization Error Keys
    public static final String ATHR_001 = "ATHR-001";
    public static final String ATHR_002 = "ATHR-002";
    public static final String ATHR_003 = "ATHR-003";
    public static final String ATH_001 = "ATH-001";
    public static final String ATH_002 = "ATH-002";

    //Keys related to User
    public static final String USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_POST_AN_ANSWER =
            "User is signed out.Sign in first to post an answer";
    public static final String USER_HAS_NOT_SIGNED_IN = "User has not signed in";
    public static final String USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_EDIT_AN_ANSWER =
            "User is signed out.Sign in first to edit an answer";
    public static final String USER_IS_SIGNED_OUT_TO_DELETE =
            "User is signed out.Sign in first to delete an answer";
    public static final String USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_GET_AN_ANSWER =
            "User is signed out.Sign in first to get the answers";
    public static final String USER_IS_SIGNED_OUT_SIGN_IN_FIRST_TO_POST_A_QUESTION =
            "User is signed out.Sign in first to post a question";
    public static final String USR_001 = "USR-001";
    public static final String ADMIN = "admin";
    public static final String USERNAME_DOES_NOT_EXIST = "This username does not exist";
    public static final String USER_NOT_SIGNED_IN = "User is not Signed in";

    //Password Error Keys
    public static final String PASSWORD_FAILED = "Password Failed";

    //Sign In/Out error keys
    public static final String SGR_001 = "SGR-001";
    public static final String SGR_002 = "SGR-002";
    public static final String TRY_ANY_OTHER_USERNAME =
            "Try any other Username, this Username has already been taken";
    public static final String USER_ALREADY_REGISTERED =
            "This user has already been registered, try with any other emailId";

    //Question Service Keys
    public static final String
            USER_WITH_ENTERED_UUID_WHOSE_QUESTION_DETAILS_ARE_TO_BE_SEEN_DOES_NOT_EXIST =
            "User with entered uuid whose question details are to be seen does not exist";
    public static final String QUESTION_ENTERED_INVALID = "The question entered is invalid";
    public static final String QUES_001 = "QUES-001";
    public static final String QUESTION_ENTERED_UUID_DOES_NOT_EXIST =
            "The question with entered uuid whose details are to be seen does not exist";
    public static final String ONLY_THE_QUESTION_OWNER_CAN_EDIT_THE_QUESTION =
            "Only the question owner can edit the question";
    public static final String ONLY_THE_QUESTION_OWNER_OR_ADMIN_CAN_DELETE_THE_QUESTION =
            "Only the question owner or admin can delete the question";
    public static final String ENTERED_QUESTION_UUID_DOES_NOT_EXIST =
            "Entered question uuid does not exist";

    //Answer Service Keys
    public static final String ANS_001 = "ANS-001";
    public static final String ANSWER_DOESNOT_EXIST = "Entered answer uuid does not exist";
    public static final String ONLY_ANSWER_OWNER_CAN_EDIT =
            "Only the answer owner can edit the answer";
    public static final String ONLY_OWNER_OR_ADMIN_CAN_DELETE =
            "Only the answer owner or admin can delete the answer";
}
