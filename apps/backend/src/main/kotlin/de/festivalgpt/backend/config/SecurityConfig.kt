package de.festivalgpt.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.*

@Configuration
@EnableWebSecurity
class SecurityConfig {

  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http
        .csrf { it.disable() }
        .cors { it.configurationSource(corsConfigurationSource()) }
        .authorizeHttpRequests { authorize -> authorize.anyRequest().permitAll() }

    return http.build()
  }

  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource {
    val configuration = CorsConfiguration()
    configuration.allowedOrigins = listOf("*")
    configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
    configuration.allowedHeaders = listOf("*")
    val source = UrlBasedCorsConfigurationSource()
    source.registerCorsConfiguration("/**", configuration)
    return source
  }
}
