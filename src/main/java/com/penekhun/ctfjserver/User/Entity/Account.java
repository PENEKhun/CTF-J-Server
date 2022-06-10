package com.penekhun.ctfjserver.User.Entity;

import com.penekhun.ctfjserver.Config.SecurityRole;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Builder
    public Account(String username, String password, String nickname, String email, String realName) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.realName = realName;
    }

    @Lob
    @Column(name = "user_role", nullable = false)
    private String userRole = SecurityRole.USER.toString();

    public boolean isAdmin(){
        return (this.userRole.equals("ROLE_ADMIN"));
    }

    public void makeAdmin(){
        this.userRole = "ROLE_ADMIN";
    }

    public String getUserRole() {
        return userRole;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }
}