package com.cocovo.fitqaspringjava.domain.feedback.service;

import com.cocovo.fitqaspringjava.common.exception.FailedConvertToFileException;
import com.cocovo.fitqaspringjava.common.exception.FeedbackAnswerException;
import com.cocovo.fitqaspringjava.domain.common.FileUploader;
import com.cocovo.fitqaspringjava.domain.feedback.FeedbackCommand;
import com.cocovo.fitqaspringjava.domain.feedback.FeedbackInfo;
import com.cocovo.fitqaspringjava.domain.feedback.component.FeedbackCommentStore;
import com.cocovo.fitqaspringjava.domain.feedback.component.FeedbackInfoMapper;
import com.cocovo.fitqaspringjava.domain.feedback.component.FeedbackReader;
import com.cocovo.fitqaspringjava.domain.feedback.component.FeedbackStore;
import com.cocovo.fitqaspringjava.domain.feedback.entity.Feedback;
import com.cocovo.fitqaspringjava.domain.trainer.component.TrainerReader;
import com.cocovo.fitqaspringjava.domain.user.component.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private static final String REGISTER_FEEDBACK_DIRECTORY = "register";
    private static final String ANSWER_FEEDBACK_DIRECTORY = "answer";
    private static final String VIDEO_DIRECTORY = "videos";

    private final FeedbackInfoMapper feedbackInfoMapper;

    private final FeedbackStore feedbackStore;
    private final FeedbackReader feedbackReader;
    private final FeedbackCommentStore feedbackCommentStore;

    private final UserReader userReader;
    private final TrainerReader trainerReader;

    private final FileUploader fileUploader;

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackInfo.Main> retrieveFeedbacks() {
        var feedbacks = feedbackReader.retrieveFeedbackAll();
        return feedbacks.stream()
                .map(feedbackInfoMapper::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FeedbackInfo.FeedbackAnswerInfo registerFeedbackAnswer(String feedbackToken,
                                                                  FeedbackCommand.RegisterFeedbackAnswer command) {
        var trainer = trainerReader.retrieveTrainerByToken(command.getTrainerToken());
        var feedback = feedbackReader.retrieveFeedbackByToken(feedbackToken);

        if (feedback.getFeedbackAnswer() != null) {
            throw new FeedbackAnswerException("이미 답변이 완료된 피드백입니다.");
        } else if (trainer.getId() != feedback.getTrainer().getId()) {
            throw new FeedbackAnswerException("잘못된 트레이너가 응답하였습니다.");
        }

        var initFeedbackAnswer = command.toEntity(feedback);
        feedbackStore.store(initFeedbackAnswer);
        feedback.changeComplete();
        return feedbackInfoMapper.of(initFeedbackAnswer);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackInfo.Main retrieveFeedbackByToken(String feedbackToken) {
        var feedback = feedbackReader.retrieveFeedbackByToken(feedbackToken);
        return feedbackInfoMapper.of(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackInfo.Main> retrieveFeedbacksByTrainerId(Long trainerId) {
        var feedbacks = feedbackReader.retrieveFeedbackAllByTrainerId(trainerId);
        return feedbacks.stream()
                .map(feedbackInfoMapper::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FeedbackInfo.Main registerFeedback(FeedbackCommand.RegisterFeedback command) {
        // Feedback Row 생성
        var createdFeedback = createFeedback(command);

        // S3 upload
        try {
            String destination = VIDEO_DIRECTORY + "/" + createdFeedback.getFeedbackToken()
                    + "/" + REGISTER_FEEDBACK_DIRECTORY + "/";
            var videoUrls = fileUploader.uploadFiles(destination, command.getVideos()
                    .toArray(MultipartFile[]::new));
        } catch (FailedConvertToFileException e) {
            e.printStackTrace();
        }

        return feedbackInfoMapper.of(createdFeedback);
    }

    @Override
    @Transactional
    public String addComment(String feedbackToken, FeedbackCommand.AddComment command) {
        var feedback = feedbackReader.retrieveFeedbackByToken(feedbackToken);
        var writer = userReader.retrieveUserByToken(command.getWriterToken());

        var initComment = command.toEntity(feedback, writer);
        var comment = feedbackCommentStore.store(initComment);
        return comment.getFeedbackCommentToken();
    }

    private Feedback createFeedback(FeedbackCommand.RegisterFeedback command) {
        var owner = userReader.retrieveUserByToken(command.getOwnerToken());
        var trainer = trainerReader.retrieveTrainerByToken(command.getTrainerToken());

        var initFeedback = command.toEntity(owner, trainer);
        owner.registerFeedback(initFeedback);
        trainer.registerFeedback(initFeedback);
        return feedbackStore.store(initFeedback);
    }
}
