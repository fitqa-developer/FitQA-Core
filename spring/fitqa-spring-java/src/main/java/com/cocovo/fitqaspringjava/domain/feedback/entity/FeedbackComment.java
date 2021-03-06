package com.cocovo.fitqaspringjava.domain.feedback.entity;

import com.cocovo.fitqaspringjava.common.exception.InvalidParamException;
import com.cocovo.fitqaspringjava.common.util.TokenGenerator;
import com.cocovo.fitqaspringjava.domain.BaseEntity;
import com.cocovo.fitqaspringjava.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Slf4j
@Getter
@Entity
@NoArgsConstructor
@Table(name = "feedback_comment")
public class FeedbackComment extends BaseEntity {
    private static final String FEEDBACK_COMMENT_PREFIX = "fct_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String feedbackCommentToken;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;
    private String comment;

    @Builder
    public FeedbackComment(Feedback feedback, User writer, String comment) {
        if (feedback == null) throw new InvalidParamException("feedback cannot be null");
        if (StringUtils.isEmpty(comment)) throw new InvalidParamException("comment cannot be empty.");

        this.feedbackCommentToken = TokenGenerator.randomCharacterWithPrefix(FEEDBACK_COMMENT_PREFIX);
        this.feedback = feedback;
        this.writer = writer;
        this.comment = comment;
    }
}
