package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

//Questa classe avrà tutto ciò che ha a che fare con la sicurezza della nostra applicazione
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder; //collegamento al password encoder creato

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    //Andando a sovrascrivere questa classe andiamo ad autorizzare le richieste con il meccanismo basic authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/","index").permitAll() //specifichiamo alcuni paths che NON hanno bisogno del basic auth per accedervi
                .antMatchers("/api/**").hasRole(ApplicationUserRole.ADMIN.name()) //specifichiamo quali utenti con specifico ruolo possono accedere a questi path
                //andiamo a vedere come selezionare i vari permessi ai percorsi
                .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAnyAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission()) // cioè il permesso di utilizzo di questa api è riconosciuta solo agli utenti con permessi di scrittura
                .antMatchers(HttpMethod.POST,"/management/api/**").hasAnyAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission()) // cioè il permesso di utilizzo di questa api è riconosciuta solo agli utenti con permessi di scrittura
                .antMatchers(HttpMethod.PUT,"/management/api/**").hasAnyAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission()) // cioè il permesso di utilizzo di questa api è riconosciuta solo agli utenti con permessi di scrittura
                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(),ApplicationUserRole.ADMINTRAINEE.name())
                .anyRequest()
                .authenticated()
                .and()
                //.httpBasic()
                    .formLogin()
                    .loginPage("/login").permitAll()
                    .defaultSuccessUrl("/courses", true)
                .and()
                .rememberMe()// default to 2 weeks
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21) )
                    .key("somethingverySecurity") //così facendo estendiamo la sessione ad un periodo a scelta con remember me
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET")) //questo perchè abbiamo disattivato csfr, per evitare attacchi (con csfr attivo questa linea si elimina)
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID","Remember-me")
                    .logoutSuccessUrl("/login");
    }

    //Creazione di un utente/user, dobbiamo sovrascrivere un metodo:
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user")) //La password dev'essere codificata, bisogna fare un password encoder
                //.roles(ApplicationUserRole.STUDENT.name()) //Ruolo che si crea internamente
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthority())
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin")) //La password dev'essere codificata, bisogna fare un password encoder
                //.roles(ApplicationUserRole.ADMIN.name()) //Ruolo che si crea internamente
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthority())
                .build();

        UserDetails admintrainee = User.builder()
                .username("admintrainee")
                .password(passwordEncoder.encode("admintrainee")) //La password dev'essere codificata, bisogna fare un password encoder
                //.roles(ApplicationUserRole.ADMINTRAINEE.name()) //Ruolo che si crea internamente
                .authorities(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthority())
                .build();

        return new InMemoryUserDetailsManager(
                user, admin, admintrainee
        );
    }
}
