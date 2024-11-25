package com.greyson.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createAt"),
        @Index(columnList = "createBy")
})
@Entity
public class UserAccount extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false, length = 50) private String userId;
    @Setter @Column(nullable = false) private String userPassword;

    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickName;
    @Setter private String memo;

    protected UserAccount() {}  // JPA는 매개변수가 없는 기본생성자가 필요하므로 생성

    private UserAccount(String userId, String userPassword, String email, String nickName, String memo) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickName = nickName;
        this.memo = memo;
    }
    public static UserAccount of(String userId, String userPassword, String email, String nickName, String memo) {
        return new UserAccount(userId, userPassword, email, nickName, memo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount userAccount)) return false;
        return Objects.equals(id, userAccount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
