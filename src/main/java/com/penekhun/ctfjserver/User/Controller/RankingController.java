package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.User.Dto.RankDto;
import com.penekhun.ctfjserver.User.Service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rank")
@Slf4j
public class RankingController {
    private final RankingService rankingService;

    @GetMapping("{howMany}")
    public List<RankDto.AccountSolveProbList> getRank(@PathVariable @Valid @NotNull Integer howMany){
        return rankingService.getRank(howMany);
    }


}
