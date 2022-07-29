package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.Admin.Service.AdminAccountService;
import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.Config.SecurityRole;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Dto.TokenDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountServiceTest {

    @Autowired
    AccountService accountService;
    @Autowired
    AdminAccountService adminAccountService;
    @Autowired
    AccountRepository accountRepository;

    public Account 암호화_되지않은_유저엔티티(){
        return Account.builder()
                .username("nickolas")
                .nickname("nickolas")
                .password("nickolas123")
                .realName("니콜라스")
                .email("duplicate@google.com")
                .role(SecurityRole.USER)
                .build();
    }

    public Account 암호화된_유저엔티티(){
        Account account = 암호화_되지않은_유저엔티티();
        return Account.builder()
                .username(account.getUsername())
                .nickname(account.getNickname())
                .password(new BCryptPasswordEncoder().encode(account.getPassword()))
                .realName(account.getRealName())
                .email(account.getEmail())
                .role(account.getRole())
                .build();
    }

    @Test
    void 회원가입_성공() throws Exception {
        //given
        AccountDto.Req.Signup signup = AccountDto.Req.Signup.builder()
                .username("nickolas")
                .nickname("nickolas")
                .password("nickolas123")
                .realName("홍길동")
                .email("nicolas123@google.com")
                .build();
        //when
        AccountDto.Res.Signup res = accountService.signup(signup);

        //then
        assertEquals(res.getUsername(),
                accountRepository.findByUsername(signup.getUsername())
                        .get()
                        .getUsername());
    }

    @Test
    void 회원가입_실패_중복된_아이디() throws Exception {
        //given
        AccountDto.Req.Signup DuplicateUser1 = AccountDto.Req.Signup.builder()
                .username("nickolas")
                .nickname("nickolas")
                .password("nickolas123")
                .realName("니콜라스")
                .email("nicolas123@google.com")
                .role(SecurityRole.USER)
                .build();

        AccountDto.Req.Signup DuplicateUser2 = AccountDto.Req.Signup.builder()
                .username("nickolas")
                .nickname("james")
                .password("james123")
                .realName("제임스")
                .email("james@google.com")
                .role(SecurityRole.USER)
                .build();
        //when
        accountService.signup(DuplicateUser1);
        CustomException exception = assertThrows(CustomException.class, () -> {
            accountService.signup(DuplicateUser2);
        });

        //then
        ErrorCode errorCode = exception.getErrorCode();
        assertEquals(ErrorCode.USERNAME_DUPLICATION, errorCode);
    }

    @Test
    void 회원가입_실패_중복된_닉네임() throws Exception {
        //given
        AccountDto.Req.Signup DuplicateNick1 = AccountDto.Req.Signup.builder()
                .username("nickolas")
                .nickname("duplicate")
                .password("nickolas123")
                .realName("니콜라스")
                .email("nicolas123@google.com")
                .role(SecurityRole.USER)
                .build();

        AccountDto.Req.Signup DuplicateNick2 = AccountDto.Req.Signup.builder()
                .username("james")
                .nickname("duplicate")
                .password("james123")
                .realName("제임스")
                .email("james@google.com")
                .role(SecurityRole.USER)
                .build();
        //when
        accountService.signup(DuplicateNick1);
        CustomException exception = assertThrows(CustomException.class, () -> {
            accountService.signup(DuplicateNick2);
        });

        //then
        ErrorCode errorCode = exception.getErrorCode();
        assertEquals(ErrorCode.NICKNAME_DUPLICATION, errorCode);
    }

    @Test
    void 회원가입_실패_중복된_이메일() throws Exception {
        //given
        AccountDto.Req.Signup DuplicateEmail1 = AccountDto.Req.Signup.builder()
                .username("nickolas")
                .nickname("nickolas")
                .password("nickolas123")
                .realName("니콜라스")
                .email("duplicate@google.com")
                .role(SecurityRole.USER)
                .build();

        AccountDto.Req.Signup DuplicateEmail2 = AccountDto.Req.Signup.builder()
                .username("james")
                .nickname("james")
                .password("james123")
                .realName("제임스")
                .email("duplicate@google.com")
                .role(SecurityRole.USER)
                .build();
        //when
        accountService.signup(DuplicateEmail1);
        CustomException exception = assertThrows(CustomException.class, () -> {
            accountService.signup(DuplicateEmail2);
        });

        //then
        ErrorCode errorCode = exception.getErrorCode();
        assertEquals(ErrorCode.EMAIL_DUPLICATION, errorCode);
    }

    @Test
    void 로그인_성공() throws Exception {
        //given
        accountRepository.save(this.암호화된_유저엔티티());
        Account account = this.암호화_되지않은_유저엔티티();
        AccountDto.Req.Login loginForm = new AccountDto.Req.Login();
        loginForm.setUsername(account.getUsername());
        loginForm.setPassword(account.getPassword());

        //when
        TokenDto tokenDto = accountService.login(loginForm);

        //then
        assertNotNull(tokenDto.getToken());
        assertNotNull(tokenDto.getTokenExpired());
        assertNotNull(tokenDto.getRefresh());
    }

    @Test
    void 토큰재발급_성공() throws Exception {
        //given
        accountRepository.save(this.암호화된_유저엔티티());
        Account account = this.암호화_되지않은_유저엔티티();
        AccountDto.Req.Login loginForm = new AccountDto.Req.Login();
        loginForm.setUsername(account.getUsername());
        loginForm.setPassword(account.getPassword());
        TokenDto tokenDto = accountService.login(loginForm);

        //when
        TokenDto newTokenDto = accountService.reissue(tokenDto.getToken(), tokenDto.getRefresh());

        //then
        assertNotNull(newTokenDto.getToken());
        assertNotNull(newTokenDto.getTokenExpired());
        assertNotNull(newTokenDto.getRefresh());
    }


    @Test
    void 회원정보수정_이메일_성공() throws Exception {
        //given
        Account before = accountRepository.save(this.암호화된_유저엔티티());
        AccountDto.Req.SignupWithoutValid after =
                new AccountDto.Req.SignupWithoutValid(null, null, null, "newemail@google.com", null, null);

        //when
        accountService.editAccountPartly(before.getId(), after);

        //then
        assertEquals(before.getEmail(), after.getEmail());
    }

    @Test
    void 회원정보수정_닉네임_성공() throws Exception {
        //given
        Account before = accountRepository.save(this.암호화된_유저엔티티());
        AccountDto.Req.SignupWithoutValid after =
                new AccountDto.Req.SignupWithoutValid(null, null, "새로운닉네임", null, null, null);

        //when
        accountService.editAccountPartly(before.getId(), after);

        //then
        assertEquals(before.getNickname(), after.getNickname());
    }

    @Test
    void 회원정보수정_실명_성공() throws Exception {
        //given
        Account before = accountRepository.save(this.암호화된_유저엔티티());
        AccountDto.Req.SignupWithoutValid after =
                new AccountDto.Req.SignupWithoutValid(null, null, null, null, "아이유", null);

        //when
        accountService.editAccountPartly(before.getId(), after);

        //then
        assertEquals(before.getRealName(), after.getRealName());
    }

    @Test
    void 회원정보수정_사용자권한_성공() throws Exception {
        //given
        Account before = accountRepository.save(this.암호화된_유저엔티티());
        SecurityRole newRole = (before.getRole() == SecurityRole.USER) ? SecurityRole.ADMIN : SecurityRole.USER;
        AccountDto.Req.SignupWithoutValid after =
                new AccountDto.Req.SignupWithoutValid(null, null, null, null, null, newRole);

        //when
        accountService.editAccountPartly(before.getId(), after);

        //then
        assertEquals(before.getRole(), after.getUserRole());
    }

    @Test
    void 회원정보수정_아이디_변경안되는게_성공임() throws Exception {
        //given
        Account before = accountRepository.save(this.암호화된_유저엔티티());
        AccountDto.Req.SignupWithoutValid after =
                new AccountDto.Req.SignupWithoutValid("newid123", null, null, null, null, null);

        //when
        accountService.editAccountPartly(before.getId(), after);

        //then
        assertNotEquals(before.getUsername(), after.getUsername());
    }

    @Test
    void 사용자_패스워드_변경_성공() throws Exception {
        //given
        String 맞는_패스워드 = this.암호화_되지않은_유저엔티티().getPassword();
        String newPassword = "NEW_PASSWORD_213123";
        Account before = accountRepository.save(this.암호화된_유저엔티티());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        //when
        accountService.editMyPassword(before.getId(), newPassword, 맞는_패스워드);
        Optional<Account> after = accountRepository.findById(before.getId());

        //then
        assertTrue(after.isPresent());
        assertTrue(bCryptPasswordEncoder.matches(newPassword, after.get().getPassword()));
    }

    @Test
    void 사용자_패스워드_변경_실패_맞지않는패스워드() throws Exception {
        //given
        String 틀린_패스워드 = this.암호화된_유저엔티티().getPassword() + "salt";
        String newPassword = "NEW_PASSWORD_213123";
        Account before = accountRepository.save(this.암호화된_유저엔티티());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        //when
        CustomException exception = assertThrows(CustomException.class, () -> {
            accountService.editMyPassword(before.getId(), newPassword, 틀린_패스워드);
        });

        //then
        ErrorCode errorCode = exception.getErrorCode();
        assertEquals(ErrorCode.PASSWORD_NOT_MATCH, errorCode);
    }

}