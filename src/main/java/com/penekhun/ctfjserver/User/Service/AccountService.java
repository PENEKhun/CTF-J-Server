package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.Config.Exception.CustomException;
import com.penekhun.ctfjserver.Config.Exception.ErrorCode;
import com.penekhun.ctfjserver.Config.Jwt.JwtFilter;
import com.penekhun.ctfjserver.Config.Jwt.TokenProvider;
import com.penekhun.ctfjserver.User.Dto.AccountDto;
import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import com.penekhun.ctfjserver.User.Dto.RankDto;
import com.penekhun.ctfjserver.User.Dto.TokenDto;
import com.penekhun.ctfjserver.User.Entity.Account;
import com.penekhun.ctfjserver.User.Entity.TokenStorage;
import com.penekhun.ctfjserver.User.Repository.AccountRepository;
import com.penekhun.ctfjserver.User.Repository.TokenStorageRepository;
import com.penekhun.ctfjserver.Util.RankSchedule;
import com.penekhun.ctfjserver.Util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final RedisUtil redisUtil;

    private final TokenStorageRepository tokenStorageRepository;
    private final RankSchedule rankSchedule;

    @Autowired
    private final ModelMapper modelMapper;

    public TokenDto login(AccountDto.Req.Login loginReq){
        String username = loginReq.getUsername();
        String password = loginReq.getPassword();

        if (username == null || password == null || username.isBlank() || password.isBlank())
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);

        //회원 검증
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Optional<Account> findMember = accountRepository.findByUsername(username);
        findMember.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        if (!encoder.matches(password, findMember.get().getPassword()))
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

    public ResponseEntity reissue(String oldAccessToken, String oldRefreshToken){
        log.info("reissue oldAccessToken: {}", oldAccessToken);
        log.info("reissue oldRefreshToken: {}", oldRefreshToken);

        Optional<TokenStorage> findAccessToken = tokenStorageRepository.findByAccessToken(oldAccessToken);
        if (findAccessToken.isEmpty())
            throw new CustomException(ErrorCode.DOESNT_EXIST_TOKEN);

        String username = findAccessToken.get().getUsername();

        log.info("reissue username : {}", username);

        if (username == null)
            throw new CustomException(ErrorCode.HANDLE_ACCESS_DENIED);

        log.info(findAccessToken.get().getRefreshToken());

        //refresh 토큰 비교
        if (!findAccessToken.get().getRefreshToken().equals(oldRefreshToken))
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
        return new ResponseEntity<>(new TokenDto(newAccessToken, tokenExpired, newRefreshToken), httpHeaders, HttpStatus.OK);
    }

    @Transactional
    public AccountDto.Res.Signup signup(AccountDto.Req.Signup signup){
        Optional<Account> findMember = accountRepository.findByUsername(signup.getUsername());
        findMember.ifPresent(then -> {
            throw new CustomException(ErrorCode.USERNAME_DUPLICATION);
        });

        findMember = accountRepository.findOneByEmail(signup.getEmail());
        findMember.ifPresent(then -> {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATION);
        });

        findMember = accountRepository.findOneByNickname(signup.getNickname());
        findMember.ifPresent(then -> {
            throw new CustomException(ErrorCode.NICKNAME_DUPLICATION);
        });

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Account account = Account.builder()
                .username(signup.getUsername())
                .password(bCryptPasswordEncoder.encode(signup.getPassword()))
                .email(signup.getEmail())
                .nickname(signup.getNickname())
                .realName(signup.getRealName()).build();
        Account resultAccount = accountRepository.save(account);

        return modelMapper.map(resultAccount, AccountDto.Res.Signup.class);
    }


    @Transactional
    public AccountDto.Res.Signup editMyAccountPartly(AccountDto.Req.Signup signup){
        return null;
    }

    @Transactional
    public AccountDto.Res.MyPage getMyAccount(Account account){
        AccountDto.Res.MyPage myInfo = modelMapper.map(account, AccountDto.Res.MyPage.class);
        RankDto.AccountSolveProbList accountSolveProbList = rankSchedule.getAccountSolveProbLists().stream()
                .filter(a -> a.getAccountId().equals(myInfo.getId()))
                .findFirst().orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        myInfo.setScore(accountSolveProbList.getScore());

        //가져온 푼 문제 Id값으로 List<제목, 타입>를 생성
        List<ProblemDto.Res.CorrectProblem> solvedList = new ArrayList<>();
        List<RankDto.ProbWithDynamicScore> probList = rankSchedule.getProbSolveCntList();
        accountSolveProbList.getProbIdList().forEach(probId-> {
                        RankDto.ProbWithDynamicScore problem = probList.stream()
                            .filter(prob -> prob.getId().equals(probId)).findFirst()
                            .orElseThrow(() -> new CustomException((ErrorCode.UNCHECKED_ERROR)));

                        solvedList.add(ProblemDto.Res.CorrectProblem.builder()
                                .id(problem.getId())
                                .title(problem.getTitle())
                                .type(problem.getType())
                                .build());
                }
        );
        myInfo.setSolved(solvedList);
        return myInfo;
    }

    @Transactional
    public AccountDto.Res.Signup getAccount(AccountDto.Req.Signup signup){
        return null;
    }
}
