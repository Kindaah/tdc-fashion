/*
 * Copyright (c) 2018 by The Dress Club.  All Rights Reserved.
 * This software is the confidential and proprietary information of
 * The Dress Club. ("Confidential Information").
 * You may not disclose such Confidential Information, and may only
 * use such Confidential Information in accordance with the terms of
 * the license agreement you entered into with The Dress Club.
 */

package com.thedressclub.tdc_backend.security;

import static com.thedressclub.tdc_backend.controller.CompanyController.COMPANY_URL;
import static com.thedressclub.tdc_backend.controller.CompanyController.REQUEST;
import static com.thedressclub.tdc_backend.controller.OrderController.ORDERS_URL;
import static com.thedressclub.tdc_backend.controller.ShippingAddressController.SHIPPING_ADDRESS_URL;
import static java.util.Arrays.asList;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration class for security authentication.
 *
 * @author Daniel Mejia.
 */
@Configuration
@EnableWebSecurity
public class AuthenticationFilter extends WebSecurityConfigurerAdapter
{
    private static final String ALL_MATCHER = "**";
    private static final String REGISTER_URL = "/users";
    private static final String TOKEN_REGISTER_URL = "/users/token";
    private static final String ADMIN_ROLE = "'ADMIN'";
    private static final String CUSTOMER_ROLE = "'CUSTOMER'";
    private static final String DESIGNER_ROLE = "'DESIGNER'";
    private static final String ORDERS_ID_URL = "/orders/{id}";
    private static final String ORDER_COMMENT_URL = "/orders/{id}/comment";
    private static final String ORDER_FINAL_QUOTE_URL = "/orders/{id}/finalQuote";
    private static final String ORDER_INITIAL_QUOTE_URL = "/orders/{id}/quote";
    private static final String PRODUCT_CREATION_URL = "/orders/{orderId}/products";
    private static final String PRODUCT_UPDATE_URL = "/orders/{orderId}/products/{id}";
    private static final String UPLOAD_FILE_URL = "/products/{id}/uploadFiles";
    private static final String PAYMENTS_URL = "/payments/" + ALL_MATCHER;
    private static final String WEB_HOOKS_URL = "/webhooks/" + ALL_MATCHER;
    private static final String CHECK_ACCESS = "@authorizationChecker.checkAccess(authentication, ";
    private static final String FINAL = ")";
    private static final String COMMA = ", ";
    private static final String LOCAL_DOMAIN = "http://localhost:4200";
    private static final String SLASH = "/";

    @Value("${auth0.apiAudience}")
    private String apiAudience;

    @Value("${auth0.issuer}")
    private String issuer;

    @Value("${app.domain}")
    private String corsDomain;

    /** (non-Javadoc) @see {@link WebSecurityConfigurerAdapter} */
    @Override
    protected void configure(HttpSecurity http)
    throws Exception
    {
        http.cors();

        JwtWebSecurityConfigurer
            .forRS256(apiAudience, issuer)
            .configure(http)
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, REGISTER_URL)
            .permitAll()
            .antMatchers(HttpMethod.GET, TOKEN_REGISTER_URL)
            .permitAll()
            .antMatchers(HttpMethod.POST, WEB_HOOKS_URL)
            .permitAll()
            .antMatchers(HttpMethod.POST, ORDERS_URL, PRODUCT_CREATION_URL)
            .access(CHECK_ACCESS + CUSTOMER_ROLE + FINAL)
            .antMatchers(HttpMethod.PATCH, ORDERS_ID_URL)
            .access(CHECK_ACCESS + CUSTOMER_ROLE + FINAL)
            .antMatchers(HttpMethod.PATCH, ORDER_FINAL_QUOTE_URL, ORDER_INITIAL_QUOTE_URL)
            .access(CHECK_ACCESS + ADMIN_ROLE + FINAL)
            .antMatchers(HttpMethod.POST, UPLOAD_FILE_URL)
            .access(CHECK_ACCESS + CUSTOMER_ROLE + COMMA + DESIGNER_ROLE + FINAL)
            .antMatchers(HttpMethod.PATCH, ORDER_COMMENT_URL)
            .access(CHECK_ACCESS + CUSTOMER_ROLE + COMMA + ADMIN_ROLE + FINAL)
            .antMatchers(HttpMethod.PUT, PRODUCT_UPDATE_URL)
            .access(CHECK_ACCESS + CUSTOMER_ROLE + COMMA + ADMIN_ROLE + FINAL)
            .antMatchers(PAYMENTS_URL)
            .access(CHECK_ACCESS + CUSTOMER_ROLE + FINAL)
            .antMatchers(SLASH + ALL_MATCHER + COMPANY_URL + SLASH + ALL_MATCHER)
            .access(CHECK_ACCESS + ADMIN_ROLE + FINAL)
            .antMatchers(SLASH + ALL_MATCHER + COMPANY_URL + REQUEST + SLASH + ALL_MATCHER)
            .access(CHECK_ACCESS + CUSTOMER_ROLE + FINAL)
            .antMatchers(ALL_MATCHER + SHIPPING_ADDRESS_URL + SLASH + ALL_MATCHER)
            .access(CHECK_ACCESS + CUSTOMER_ROLE + FINAL)
            .anyRequest()
            .authenticated();
    }

    /**
     * Adds a cors filter to the application.
     *
     * @return the cors filter.
     */
    @Bean
    public CorsFilter corsFilter()
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(asList(corsDomain, LOCAL_DOMAIN));
        config.addAllowedHeader("*");
        config.setAllowedMethods(asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
