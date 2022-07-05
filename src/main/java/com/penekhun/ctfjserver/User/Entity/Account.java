package com.penekhun.ctfjserver.User.Entity;

import com.penekhun.ctfjserver.Config.SecurityRole;
import com.penekhun.ctfjserver.Config.SecurityRoleConverter;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Table(name = "Account", schema = "ctf")
public class Account {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "idx", nullable = false)
    private Integer id;

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

    @Lob
    @Column(name = "user_role", nullable = false)
    private String userRole = SecurityRole.USER.toString();

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

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }
}