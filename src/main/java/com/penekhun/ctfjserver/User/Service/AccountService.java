package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.Config.Jwt.JwtFilter;
import com.penekhun.ctfjserver.Config.Jwt.TokenProvider;
import com.penekhun.ctfjserver.Config.SecurityRole;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Dto.TokenDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Entity.TokenStorage;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import com.penekhun.ctfjserver.User.Repository.AuthLogRepository;
import com.penekhun.ctfjserver.User.Repository.TokenStorageRepository;
import com.penekhun.ctfjserver.Util.RankSchedule;
import com.penekhun.ctfjserver.Util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final AuthLogRepository authLogRepository;
    private final RedisUtil redisUtil;

    private final TokenStorageRepository tokenStorageRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Transactional(readOnly=true)
    public TokenDto login(AccountDto.Req.Login loginReq){
        String username = loginReq.getUsername();
        String password = loginReq.getPassword();

        if (username == null || password == null || username.isBlank() || password.isBlank())
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);

        //회원 검증
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Account findMember = accountRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (!encoder.matches(password, findMember.getPassword()))
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //토큰 생성및 리턴
        Map<String, String> jwt = tokenProvider.createToken(username);
        String accessToken = jwt.get("token");
        String tokenExpired = jwt.get("tokenExpired");

        String refreshToken = tokenProvider.createRefreshToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);

        TokenStorage tokenStorageEntity = TokenStorage.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .username(username)
                        .build();

        redisUtil.insertTokenToStorage(username, tokenStorageEntity);
        return new TokenDto(accessToken, tokenExpired, refreshToken);
    }

    public ResponseEntity logout(){
        //todo Logout 구현
        return ResponseEntity.noContent().build();
    }

    @Transactional(readOnly=true)
    public TokenDto reissue(String oldAccessToken, String oldRefreshToken){
        log.info("reissue oldAccessToken: {}", oldAccessToken);
        log.info("reissue oldRefreshToken: {}", oldRefreshToken);

        TokenStorage findAccessToken = tokenStorageRepository.findByAccessToken(oldAccessToken)
                                        .orElseThrow(() -> new CustomException(ErrorCode.DOESNT_EXIST_TOKEN));

        String username = findAccessToken.getUsername();

        log.info("reissue username : {}", username);

        if (username == null)
            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);

        //refresh 토큰 비교
        if (!findAccessToken.getRefreshToken().equals(oldRefreshToken))
            throw new CustomException(ErrorCode.UNCHECKED_ERROR);


        Map<String, String> jwt = tokenProvider.createToken(username);
        String newAccessToken = jwt.get("token");
        String tokenExpired = jwt.get("tokenExpired");
        String newRefreshToken = tokenProvider.createRefreshToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + newAccessToken);

        TokenStorage tokenStorageEntity = TokenStorage.builder()
                .refreshToken(newRefreshToken)
                .accessToken(newAccessToken)
                .username(username)
                .build();

        redisUtil.insertTokenToStorage(username, tokenStorageEntity);
        return new TokenDto(newAccessToken, tokenExpired, newRefreshToken);
    }

    public AccountDto.Res.Signup signup(AccountDto.Req.Signup signup){
        String username = signup.getUsername();
        accountRepository.findByUsername(username)
                .ifPresent(s -> {
                    throw new CustomException(ErrorCode.USERNAME_DUPLICATION
                    );
                });
        String nickname = signup.getNickname();
        accountRepository.findOneByNickname(nickname)
                .ifPresent(s -> {
                    throw new CustomException(ErrorCode.NICKNAME_DUPLICATION
                    );
                });
        String email = signup.getEmail();
        accountRepository.findOneByEmail(email)
                .ifPresent(s -> {
                    throw new CustomException(ErrorCode.EMAIL_DUPLICATION
                    );
                });

        Account resultAccount = accountRepository.save(signup.toEntity());
        return modelMapper.map(resultAccount, AccountDto.Res.Signup.class);
    }

    public Account editAccountPartly(Long id, AccountDto.Req.SignupWithoutValid editPartly) throws DataIntegrityViolationException {
        //변경 된 부분만 수정이 이뤄지도록
        Account account = accountRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if ((account.getRole() == SecurityRole.ADMIN && editPartly.getUserRole().equals(SecurityRole.USER))
                || (account.getRole() == SecurityRole.USER && editPartly.getUserRole().equals(SecurityRole.ADMIN))) {
                    // ADMIN <-> USER 권한 변경시, authLog 삭제
                    authLogRepository.deleteAllBySolver(account);
        }
        account.editPartly(editPartly);
        return accountRepository.save(account);
    }


    public void editMyPassword(Long id, String newPassword, String oldPassword){
        Account account = accountRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(oldPassword, account.getPassword()))
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);

        account.changePassword(newPassword);
        accountRepository.save(account);
    }


    @Transactional(readOnly=true)
    public AccountDto.Res.MyPage getMyAccount(Account account){
        AccountDto.Res.MyPage myInfo = account.toInfo();
        //todo ToDto

        RankSchedule.accSolveList.stream()
                .filter(db -> db.getAccountId().equals(account.getId()))
                    .findFirst()
                        .ifPresentOrElse(
                            foundAcc -> {
                                myInfo.setSolved(foundAcc.getSolved());
                                myInfo.setScore(foundAcc.getScore());
                            },
                            () -> {
                                myInfo.setScore(0);
                                myInfo.setSolved(null);
                            });
        return myInfo;
    }

    @Transactional(readOnly=true)
    public AccountDto.Res.AccountList getAllAccount(Pageable pageable){

        Page<Account> accPage = accountRepository.findAll(pageable);
        List<Account> accList = accPage.getContent();

        AccountDto.Res.AccountList response = new AccountDto.Res.AccountList(accPage.getTotalPages(), accPage.getTotalElements());

        accList.forEach(acc -> {
            // 점수랑 푼 문제 채워넣기
            AccountDto.Res.MyPage myPage = AccountDto.Res.MyPage.valueOf(acc);
            RankSchedule.accSolveList.stream().filter(db -> db.getAccountId().equals(acc.getId()))
                    .findFirst().ifPresentOrElse(
                            foundAcc -> {
                                myPage.setSolved(foundAcc.getSolved());
                                myPage.setScore(foundAcc.getScore());
                            },
                            () -> myPage.setScore(0));
            response.addAccount(myPage);
        });

        return response;
    }
}
