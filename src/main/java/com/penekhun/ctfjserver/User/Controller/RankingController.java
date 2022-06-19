package com.penekhun.ctfjserver.User.Controller;

import com.penekhun.ctfjserver.User.Dto.RankDto;
import com.penekhun.ctfjserver.User.Service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rank")
@Slf4j
public class RankingController {
    private final RankingService rankingService;

    @GetMapping("{howMany}")
    @Operation(tags= {"rank"}, summary = "랭킹을 가져오는 API", description = "인자로 넘겨준 숫자 만큼 랭크를 가져옵니다.")
    @Parameter(name = "howMany", description = "가져올 상위 n등", example = "5", required = true)
    public RankDto.EveryHourScore getRank(
             @PathVariable @Valid @NotNull Integer howMany){
        return rankingService.getRank(howMany);
    }


}
