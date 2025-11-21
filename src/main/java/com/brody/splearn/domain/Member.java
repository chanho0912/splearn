package com.brody.splearn.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Objects;

@Getter
@ToString
public class Member {

    private String email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    // 기본 생성자 or Setter 사용을 지양 하는 이유가 도메인에서 특정 행위로 인해 여러가지 속성이 동시에 바뀌고 해야 하는데
    // 상태가 한번에 바뀌는게 아니다 보니 실수할 여지도 큼. 그래서 그 이유를 잘 담은 메서드로 표현하는 것이 좋음
    private Member(String email, String nickname, String passwordHash) {
        this.email = Objects.requireNonNull(email);
        this.nickname = Objects.requireNonNull(nickname);
        this.passwordHash = Objects.requireNonNull(passwordHash);

        this.status = MemberStatus.PENDING;
    }

    public static Member create(String email, String name, String password, PasswordEncoder encoder) {
        return new Member(email, name, encoder.encode(password));
    }

    public void activate() {
        Assert.state(status == MemberStatus.PENDING, "PENDING 상태가 아닙니다.");

        this.status = MemberStatus.ACTIVE;
    }

    public void deactivate() {
        Assert.state(status == MemberStatus.ACTIVE, "ACTIVE 상태가 아닙니다.");

        this.status = MemberStatus.DEACTIVATED;
    }

    public boolean verifyPassword(String password, PasswordEncoder encoder) {
        return encoder.matches(password, this.passwordHash);
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePassword(String alpha, PasswordEncoder encoder) {
        this.passwordHash = encoder.encode(alpha);
    }
}
