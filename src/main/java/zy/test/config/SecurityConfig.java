package zy.test.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 开启WebSecurity模式
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //认证
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //功能页只对有指定权限的人开放
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/level1/**").hasRole("vip1")
                .antMatchers("/level2/**").hasRole("vip2")
                .antMatchers("/level3/**").hasRole("vip3");

        // 开启自动配置的登录功能
        // /login 请求来到登录页
        // /login?error 重定向到这里表示登录失败
        http.formLogin().loginPage("/toLogin")
                .usernameParameter("username")
                .passwordParameter("password").
                loginProcessingUrl("/login");

        //关闭csrf功能:跨站请求伪造,默认只能通过post方式提交logout请求
        http.csrf().disable();

        //开启自动配置的注销的功能
        // /logout 注销请求
        http.logout().logoutSuccessUrl("/index");

        //记住我
        http.rememberMe().rememberMeParameter("remember");
    }


    //授权
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //设置用户名密码，及用户权限
        //从数据库拿权限数据
//        auth.jdbcAuthentication()
        //为测试，先把数据放缓存
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
                .withUser("zhangsan").password(new BCryptPasswordEncoder().encode("zhangsan")).roles("vip1").and()
                .withUser("lisi").password(new BCryptPasswordEncoder().encode("lisi")).roles("vip3", "vip2").and()
                .withUser("admin").password(new BCryptPasswordEncoder().encode("admin")).roles("vip1", "vip2", "vip3");
    }
}
