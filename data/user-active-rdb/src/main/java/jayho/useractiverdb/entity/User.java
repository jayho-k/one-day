package jayho.useractiverdb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "\"user\"")
@NoArgsConstructor
public class User {

    @Id
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
