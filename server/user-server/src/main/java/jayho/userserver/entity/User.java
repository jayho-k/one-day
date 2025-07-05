package jayho.userserver.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {

    private Long userId;
    private String userName;
    private String userProfileImage;

    public static User create(Long userId, String userName, String userProfileImage) {
        User user = new User();
        user.userId = userId;
        user.userName = userName;
        user.userProfileImage = userProfileImage;
        return user;
    }
}
