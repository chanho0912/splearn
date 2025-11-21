package com.brody.splearn.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Getter
@ToString
public class Member {

    private String email;

    private String nickname;

    private String passwordHash;

    private MemberStatus status;

    private Member() {
    }

    public static Member create(MemberCreateRequest createRequest, PasswordEncoder encoder) {
        var member = new Member();

        member.email = requireNonNull(createRequest.email());
        member.nickname = requireNonNull(createRequest.nickname());
        member.passwordHash = encoder.encode(requireNonNull(createRequest.password()));

        member.status = MemberStatus.PENDING;

        return member;
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
        this.nickname = requireNonNull(nickname);
    }

    public void changePassword(String password, PasswordEncoder encoder) {
        this.passwordHash = encoder.encode(requireNonNull(password));
    }

    public boolean isActive() {
        return status == MemberStatus.ACTIVE;
    }
}
