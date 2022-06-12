package com.cocovo.fitqaspringjava.common.response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {
  COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
  COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),
  COMMON_ENTITY_NOT_FOUND("존재하지 않는 엔터티입니다."),
  COMMON_ILLEGAL_STATUS("잘못된 상태값입니다."),

  USER_AUTH_FAILED("로그인 시도가 실패했습니다."),

  FEEDBACK_ANSWER_FAILED("피드백 답변이 실패했습니다."),

  VIDEO_NOT_REGISTERED("동영상이 아직 등록되지 않았습니다."),
  VIDEO_ENCODE_FAILED("동영상 인코딩이 실패했습니다."),
  FAILED_CONVERT_TO_FILE("File로 변환이 실패했습니다.");

  private final String errorMsg;

  public String getErrorMsg(Object... arg) {
    return String.format(errorMsg, arg);
  }
  }
