package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // autowire will cause circular dependency error with user service implementation
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource; // the datasource to use

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    String rolesQuery;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        return bCryptPasswordEncoder;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // Spring Security should completely ignore URLs starting with /resources/
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
        //.and().debug(true);
    }


    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(this.authenticationProvider())
                // In Memory configuration
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER")
//                .and()
//                .withUser("andy").password("andy").roles("USER","ADMIN");

                .jdbcAuthentication()
                .usersByUsernameQuery(this.usersQuery)
                .authoritiesByUsernameQuery(this.rolesQuery)
                .dataSource(this.dataSource)
                .passwordEncoder(this.passwordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // configure authorization for urls
                .authorizeRequests()
                // grant access to all users for root path and /home
                //.antMatchers("/", "/home").permitAll()
                // grant access to the greeting
                .antMatchers("/greeting")
                //.authenticated()
                //  [http-nio-8080-exec-6] o.s.s.w.a.i.FilterSecurityInterceptor    : Secure object: FilterInvocation: URL: /greeting; Attributes: [hasRole('ROLE_TESTROLE')]
                .hasRole("TESTROLE")
                .and().httpBasic()
                // for any other request user should be authenticated

                // and configure another thing (formLogin configuration
                .and()
                // configures a form login (specify the the support of form based authentication)
                .formLogin()
                // configures the URL to send users to if login is required
                .loginPage("/login")
                .failureForwardUrl("/login?error=true")
                .defaultSuccessUrl("/hello")
                .usernameParameter("username")
                .passwordParameter("password")
                // grant access to all to the login page
                .permitAll()
                // and configure another thing (logout)
                .and()
                // configures a default logout configuration
                .logout()
                // configures the page, which is triggering the logout (default is /logout)
                .logoutUrl("/logout")
                // configures redirect url after logout
                .logoutSuccessUrl("/login?logout")
                // grant access to all to the logout page
                .permitAll();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userDetailsService);
        BCryptPasswordEncoder passwordEncoder = this.passwordEncoder();
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }






}
