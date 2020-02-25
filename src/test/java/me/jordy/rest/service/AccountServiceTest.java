package me.jordy.rest.service;

import me.jordy.rest.entity.Account;
import me.jordy.rest.entity.AccountRole;
import me.jordy.rest.repository.AccountRepository;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUserName() {
        HashSet<AccountRole> accountRoles = new HashSet<AccountRole>();
        accountRoles.add(AccountRole.ADMIN);
        accountRoles.add(AccountRole.USER);
        // Given
        String username = "jordy";
        String password = "1234";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(accountRoles)
                .build();

        this.accountService.saveAccount(account);

        // When
        UserDetailsService userDetailsService = (UserDetailsService) accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword()));
    }

    // # 예외 처리 방법.
    // 1. 예외 타입만 확인 가능
//    @Test(expected = UsernameNotFoundException.class)
//    public void findByUsernameFail() {
//
//    }

    // 2. try-catch, 코드가 다소 복잡.
//    public void findByUsernameFail() {
//        String username = "random@gmail.com";
//        try {
//            accountService.loadUserByUsername(username);
//            fail("supposed to be a failed"); // 명시적으로 실패 처리.
//        } catch (UsernameNotFoundException e) {
//            assertThat(e.getMessage()).containsSequence(username);
//        }
//    }

    // 3. Rule 방식
    @Test
    public void findByUsernameFail() {
        //Expected
        String username = "random@gmail.com";
        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage(Matchers.containsString(username));

        // When
        accountService.loadUserByUsername(username);
    }
}