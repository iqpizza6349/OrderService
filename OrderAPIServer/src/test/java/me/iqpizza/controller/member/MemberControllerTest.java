package me.iqpizza.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.iqpizza.config.jwt.JwtProperties;
import me.iqpizza.config.jwt.JwtProvider;
import me.iqpizza.config.security.SecurityConfig;
import me.iqpizza.config.security.dto.AuthenticateUserService;
import me.iqpizza.config.security.dto.AuthenticateUserStorage;
import me.iqpizza.controller.ControllerTestUtil;
import me.iqpizza.controller.MemberController;
import me.iqpizza.domain.member.dto.SignDto;
import me.iqpizza.domain.member.entity.Member;
import me.iqpizza.domain.member.ro.MemberRO;
import me.iqpizza.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(value = {
        JwtProvider.class, AuthenticateUserStorage.class,
        AuthenticateUserService.class, SecurityConfig.class
})
@EnableAutoConfiguration
@WebMvcTest(MemberController.class)
@EnableConfigurationProperties(value = JwtProperties.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Spy
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("회원가입 컨트롤러 테스트")
    void registerMemberControllerTest() throws Exception {
        // given
        final SignDto signDto = new SignDto("username", "password", null);
        MemberRO memberRO = new MemberRO(signDto
                .toEntity(Member.MemberRole.CUSTOMER, encoder));
        String content = objectMapper.writeValueAsString(signDto);
        given(memberService.register(any(SignDto.class)))
                .willReturn(memberRO);

        // when
        ResultActions actions = ControllerTestUtil.resultActions(
                mockMvc, "/members/register", content,
                HttpMethod.POST, null
        );

        // then
        actions.andDo(print())
                .andExpect(status().isCreated());
    }
}
