package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.request.UpdateUserProfileRequest;
import org.example.exercise_shop.dto.response.UserProfileResponse;

public interface UserService {
    void login2FAAuthentication(String username);
    UserProfileResponse getUserProfileByUsername(String username);
    void UpdateUserProfile(UpdateUserProfileRequest updateUserProfileRequest);
    void complete2FAAuthentication(String username);
    void logout2FAAuthentication(String username);
}
