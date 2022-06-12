package com.cocovo.fitqaspringjava.interfaces.feedback;

import com.cocovo.fitqaspringjava.application.feedback.FeedbackFacade;
import com.cocovo.fitqaspringjava.common.response.CommonResponse;
import com.cocovo.fitqaspringjava.domain.feedback.FeedbackCommand;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
public class FeedbackApiController {

  private final FeedbackFacade feedbackFacade;
  private final FeedbackDtoMapper feedbackDtoMapper;

  @GetMapping
  public CommonResponse<List<FeedbackDto.Main>> getFeedbackAll() {
    var feedbacks = feedbackFacade.retrieveFeedbacks();
    var response = feedbacks.stream()
        .map(feedbackDtoMapper::of)
        .collect(Collectors.toList());
    return CommonResponse.success(response);
  }

  @GetMapping("/{feedbackToken}")
  public CommonResponse<FeedbackDto.Main> getFeedbackById(
      @PathVariable("feedbackToken") String feedbackToken) {
    var feedbackInfo = feedbackFacade.retrieveFeedbackByToken(feedbackToken);
    var response = feedbackDtoMapper.of(feedbackInfo);
    return CommonResponse.success(response);
  }

  @PostMapping("/register")
  public CommonResponse<FeedbackDto.Main> registerFeedback(
      @RequestPart(value = "context", required = true) @Valid FeedbackDto.RegisterReq request,
      @RequestPart(value = "videos", required = true) List<MultipartFile> videos) {
    var registerCommand = new FeedbackCommand.RegisterFeedback(request, videos);

    var feedbackInfo = feedbackFacade.registerFeedback(registerCommand);

    var response = feedbackDtoMapper.of(feedbackInfo);
    return CommonResponse.success(response);
  }

  @PostMapping("/{feedbackToken}/comment")
  public CommonResponse<FeedbackDto.Main> addComment(
      @PathVariable("feedbackToken") String feedbackToken,
      @RequestBody @Valid FeedbackDto.AddCommentReq request) {
    var commentCommand = feedbackDtoMapper.of(request);
    var feedbackInfo = feedbackFacade.addComment(feedbackToken, commentCommand);
    var response = feedbackDtoMapper.of(feedbackInfo);
    return CommonResponse.success(response);
  }

  @PostMapping("/{feedbackToken}/answer")
  public CommonResponse<FeedbackDto.FeedbackAnswerInfo> registerFeedbackAnswer(
      @PathVariable("feedbackToken") String feedbackToken,
      @RequestBody @Valid FeedbackDto.RegisterAnswerReq request) {
    var answerCommand = feedbackDtoMapper.of(request);
    var feedbackInfo = feedbackFacade.registerFeedbackAnswer(feedbackToken, answerCommand);
    var response = feedbackDtoMapper.of(feedbackInfo);
    return CommonResponse.success(response);
  }
}
