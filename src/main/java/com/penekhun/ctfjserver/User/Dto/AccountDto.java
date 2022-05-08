package com.penekhun.ctfjserver.User.Dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.validation.Valid;

public class AccountDto {


    public class Req {

        @Data
        public class Signup{
            private String username;
            private String password;
            private String nickname;
            private String email;
            private String realName;
            //private String userRole;
        }

    }

    public class Res {

    }

}
