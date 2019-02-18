package cn.xinzhili.infra;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//import javax.servlet.Filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *  TODO not implemented yet, refer to https://spring.io/blog/2014/05/23/preview-spring-security-test-web-security
 *  to see how to add tests
 *
 * Date: 16/8/3 Time: PM2:00
 *
 * @author Gan Dong
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
//@WebAppConfiguration
public class TokenTest {

//  @Autowired
//  WebApplicationContext context;
//
//  @Autowired
//  private Filter springSecurityFilterChain;

  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(
      "build/generated-snippets");

  private MockMvc mockMvc;

  @Before
  public void setUp() {
//    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
//        .addFilters(springSecurityFilterChain)
//        .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
//        .build();
  }

  @Test
  public void testClientNotAuthenticated() throws Exception {
//    this.mockMvc.perform(get("/token").accept(MediaType.APPLICATION_JSON))
//        .andExpect(status().isOk())
//        ;
//    this.mockMvc
//        .perform(get("/"))
//        .andExpect(status().isMovedTemporarily());
  }
}
