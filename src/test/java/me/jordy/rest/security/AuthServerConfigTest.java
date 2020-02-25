package me.jordy.rest.security;


import me.jordy.rest.BaseControllerTest;
import me.jordy.rest.entity.Account;
import me.jordy.rest.entity.AccountRole;
import me.jordy.rest.service.AccountService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    public void getAuthToken() throws Exception{
        //Given
        String email = "scappy@gmail.com";
        String password = "1234";
        Account account = Account.builder()
                .email(email)
                .password(password)
                .roles(new HashSet(Arrays.asList(AccountRole.ADMIN,AccountRole.USER)))
                .build()
        ;
        this.accountService.saveAccount(account);

        String clientId = "myApp";
        String clientSecret = "pass";

        this.mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(clientId, clientSecret))
                        .param("username",email)
                        .param("password",password)
                        .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists())
                ;
    }
}
