package com.cocovo.fitqaspringjava.domain.feedback;

import com.cocovo.fitqaspringjava.domain.common.TypeInfo;
import com.cocovo.fitqaspringjava.domain.feedback.entity.Feedback;
import com.cocovo.fitqaspringjava.domain.feedback.entity.FeedbackAnswer;
import com.cocovo.fitqaspringjava.domain.feedback.entity.FeedbackComment;
import com.cocovo.fitqaspringjava.domain.trainer.entity.Trainer;
import com.cocovo.fitqaspringjava.domain.user.User;
import com.cocovo.fitqaspringjava.interfaces.feedback.FeedbackDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

public class FeedbackCommand {

  @Getter
  @ToString
  public static class RegisterFeedback {

    private String ownerToken;
    private String trainerToken;
    private TypeInfo.InterestArea interestArea;
    private Integer price;
    private String title;
    private String content;
    private boolean locked;
    private List<MultipartFile> videos;

    public RegisterFeedback(FeedbackDto.RegisterReq registerReq, List<MultipartFile> videos) {
      this.ownerToken = registerReq.getOwnerToken();
      this.trainerToken = registerReq.getTrainerToken();
      this.interestArea = registerReq.getInterestArea();
      this.price = registerReq.getPrice();
      this.title = registerReq.getTitle();
      this.content = registerReq.getContent();
      this.locked = registerReq.isLocked();
      this.videos = videos;
    }

    public Feedback toEntity(User owner, Trainer trainer) {
      return Feedback.builder()
          .owner(owner)
          .trainer(trainer)
          .interestArea(interestArea)
          .price(price)
          .title(title)
          .content(content)
          .locked(locked)
          .build();
    }
  }

  @Getter
  @Builder
  @ToString
  public static class RegisterFeedbackAnswer {

    private String trainerToken;
    private String videoUrl;
    private String description;

    public FeedbackAnswer toEntity(Feedback feedback) {
      return FeedbackAnswer.builder()
          .feedback(feedback)
          .videoUrl(videoUrl)
          .description(description)
          .build();
    }
  }

  @Getter
  @Builder
  @ToString
  public static class AddComment {

    private String writerToken;
    private String comment;

    public FeedbackComment toEntity(Feedback feedback, User writer) {
      return FeedbackComment.builder()
          .feedback(feedback)
          .writer(writer)
          .comment(comment)
          .build();
    }
  }
}
