package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.UserRepository;
import org.example.exercise_shop.dto.request.UpdateUserProfileRequest;
import org.example.exercise_shop.dto.response.UserProfileResponse;
import org.example.exercise_shop.entity.User;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.mapper.UserMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserProfileResponse getUserProfileByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserProfileResponse(user);
    }

    @Override
    public void updateUserProfile(UpdateUserProfileRequest updateUserProfileRequest) {
        User user = userRepository.findByUsername(updateUserProfileRequest.getUsername()).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, updateUserProfileRequest);
        try {
            synchronized (this) {
                userRepository.save(user);
            }

        } catch (DataIntegrityViolationException | JpaSystemException e) {
            throw new ApplicationException(ErrorCode.UNCATEGORIZED);
        }

    }

    @Override
    @Transactional
    public void saveAvatarUrl(String username, String avatarUrl) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        user.setImageUrl(avatarUrl);
        userRepository.save(user);
    }

    @Override
    public void complete2FAAuthentication(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_USER));
        user.set2FAAuthenticated(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void logout2FAAuthentication(String username) {
        userRepository.updateUser(username, false);

    }


    @Override
    @Transactional
    public void login2FAAuthentication(String username) {
        userRepository.updateUser(username, true);

    }


}
