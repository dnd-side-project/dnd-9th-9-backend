package com.dnd.Exercise.domain.user.service;

import com.dnd.Exercise.domain.user.dto.UserMapper;
import com.dnd.Exercise.domain.user.dto.request.*;
import com.dnd.Exercise.domain.user.dto.response.GetProfileDetail;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.global.error.dto.ErrorCode;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.dnd.Exercise.global.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final AwsS3Service awsS3Service;
    private final String S3_FOLDER = "user-profile";

    @Override
    public GetProfileDetail getProfileDetail(long userId) {
        User user = getUser(userId);
        return userMapper.from(user);
    }

    @Override
    @Transactional
    public void updateOnboardProfile(UpdateOnboardProfileReq updateOnboardProfileReq, long userId) {
        validateCalorieGoal(updateOnboardProfileReq);
        User user = getUser(userId);
        userMapper.updateFromDto(updateOnboardProfileReq,user);
    }

    @Override
    @Transactional
    public void postSkillLevel(PostSkillLevelReq postSkillLevelReq, long userId) {
        User user = getUser(userId);
        user.updateSkillLevel(postSkillLevelReq.getSkillLevel());
    }

    @Override
    @Transactional
    public void updateProfile(UpdateMyProfileReq updateMyProfileReq, long userId) {
        User user = getUser(userId);

        if (updateMyProfileReq.getDeletePrevImg() == true) {
            deleteMemoImgAtS3(user.getProfileImg());
            user.updateProfileImgUrl(null);
        }

        if (updateMyProfileReq.getNewProfileImg() != null) {
            String newImgUrl = postMemoImgAtS3(updateMyProfileReq.getNewProfileImg());
            user.updateProfileImgUrl(newImgUrl);
        }

        userMapper.updateFromDto(updateMyProfileReq,user);
    }

    @Override
    @Transactional
    public void updateAppleLinked(UpdateAppleLinkedReq updateAppleLinkedReq, long userId) {
        User user = getUser(userId);
        user.updateIsAppleLinked(updateAppleLinkedReq.getIsAppleLinked());
    }

    @Override
    @Transactional
    public void updateNotificationAgreed(UpdateNotificationAgreementReq updateNotificationAgreementReq, long userId) {
        User user = getUser(userId);
        user.updateIsNotificationAgreed(updateNotificationAgreementReq.getIsNotificationAgreed());
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).get();
    }

    private void validateCalorieGoal(UpdateOnboardProfileReq updateOnboardProfileReq) {
        if (updateOnboardProfileReq.getIsAppleLinked() == true) {
            if (updateOnboardProfileReq.getCalorieGoal() == null) {
                throw new BusinessException(ErrorCode.NEED_CALORIE_GOAL);
            }
        }
        else {
            if (updateOnboardProfileReq.getCalorieGoal() != null) {
                throw new BusinessException(ErrorCode.CALORIE_GOAL_UPDATE_UNAVAILABLE);
            }
        }
    }

    private String postMemoImgAtS3(MultipartFile memoImg) {
        String imgUrl = null;
        if (memoImg != null) {
            imgUrl = awsS3Service.upload(memoImg,S3_FOLDER);
        }
        return imgUrl;
    }

    private void deleteMemoImgAtS3(String fileName) {
        if(fileName != null) {
            awsS3Service.deleteImage(fileName);
        }
    }
}
