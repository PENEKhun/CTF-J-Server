package com.penekhun.ctfjserver.User.Service;

import com.penekhun.ctfjserver.User.Dto.ProblemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProblemService {

    public ResponseEntity<String> addProblem(ProblemDto.Default problemDto){
        return null;
    }

    public ResponseEntity<String> getProblemList(){
        return null;
    }

    public ResponseEntity<?> authProblem(ProblemDto.Req.Auth auth){

        return null;
    }



}
