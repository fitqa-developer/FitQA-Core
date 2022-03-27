package com.cocovo.fitqaspringjava.domain.trainer;

import com.cocovo.fitqaspringjava.domain.common.entity.type.WorkOutType;
import com.cocovo.fitqaspringjava.domain.trainer.entity.*;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

public class TrainerInfo {

  @Getter
  @ToString
  public static class Main {
    private final String name;
    private final WorkOutType.Style style;
    private final String introduceTitle;
    private final String introduceContext;
    private final Integer likesCount;
    private final List<TrainerImage> images;
    private final List<TrainerCareer> careers;
    private final List<TrainerFeedbackPrice> feedbackPrices;
    private final List<TrainerInterestArea> interestAreas;
    private final List<TrainerSns> sns;

    public Main(Trainer trainer) {
      this.name = trainer.getName();
      this.style = trainer.getStyle();
      this.introduceTitle = trainer.getIntroduceTitle();
      this.introduceContext = trainer.getIntroduceContext();
      this.likesCount = trainer.getLikesCount();
      this.images = trainer.getImages();
      this.careers = trainer.getCareers();
      this.feedbackPrices = trainer.getFeedbackPrices();
      this.interestAreas = trainer.getInterestAreas();
      this.sns = trainer.getSns();
    }
  }
}
