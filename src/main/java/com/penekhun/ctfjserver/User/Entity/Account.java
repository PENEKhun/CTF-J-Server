package com.penekhun.ctfjserver.User.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.penekhun.ctfjserver.Config.SecurityRole;
import com.penekhun.ctfjserver.Config.SecurityRoleConverter;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "Account", schema = "ctf")
public class Account {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(name = "password", nullable = false, length = 130)
    private String password;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "real_name", nullable = false, length = 10)
    private String realName;

    @Column(name = "last_auth_time")
    private Timestamp lastAuthTime;

    @Lob
    @Column(name = "user_role", nullable = false)
    @Convert(converter = SecurityRoleConverter.class)
    private SecurityRole userRole = SecurityRole.USER;


    // for easy account remove START //
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_idx")
    private List<NotificationDetail> receivedNotifications = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_idx")
    private List<LogStore> logStores  = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_idx")
    private List<AuthLog> authLogs  = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private List<Problem> problems  = new ArrayList<>();
    // for easy account remove END //

    @Builder
    public Account(String username, String password, String nickname, String email, String realName, Timestamp lastAuthTime, SecurityRole role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.realName = realName;
        this.lastAuthTime = lastAuthTime;
        this.userRole = role;
    }

    public AccountDto.Res.MyPage toInfo(){
        return AccountDto.Res.MyPage.builder()
                .id(this.id)
                .nickname(this.nickname)
                .realName(this.realName)
                .email(this.email)
                .role(this.userRole)
                .username(this.username)
                .build();
    }

    public void editPartly(AccountDto.Req.SignupWithoutValid editInfo){
        if (editInfo.getEmail() != null)
            this.email = editInfo.getEmail();
        if (editInfo.getNickname() != null)
            this.nickname = editInfo.getNickname();
        if (editInfo.getPassword() != null) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            this.password = bCryptPasswordEncoder.encode(editInfo.getPassword());
        }
        if (editInfo.getRealName() != null)
            this.realName = editInfo.getRealName();
        if (editInfo.getUserRole() != null) {
            this.userRole = editInfo.getUserRole();
        }
    }

    public void changePassword(String newPassword) {
        if (newPassword != null) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            this.password = bCryptPasswordEncoder.encode(newPassword);
        }
    }

    public boolean isAdmin(){
        return (this.userRole.equals(SecurityRole.ADMIN));
    }

    public void makeAdmin(){
        this.userRole = SecurityRole.ADMIN;
    }

    public void updateLastAuthTime(){
        this.lastAuthTime = new Timestamp(System.currentTimeMillis());
    }

    public SecurityRole getRole() {
        return userRole;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname; //for modelMapper at signup
    }

    public String getEmail() {
        return email; //for modelMapper at signup
    }

    public String getRealName() {
        return realName; //for modelMapper at signup
    }

    public Long getId() {
        return id;
    }
}