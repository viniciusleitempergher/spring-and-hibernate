package com.viniciusleitemperg.rest.filter;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.viniciusleitemperg.rest.configs.JwtTokenUtil;
import com.viniciusleitemperg.rest.models.Customer;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// Get authorization header and validate
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}

		// Get jwt token and validate
		final String token = header.split(" ")[1].trim();

		try {
			Customer customer = jwtTokenUtil.getCustomerFromToken(token);
			if (!jwtTokenUtil.validateAccessTokenByCustomer(token, customer)) {
				throw new AccessDeniedException("Invalid Token");
			}
		} catch (EntityNotFoundException | AccessDeniedException e) {
			chain.doFilter(request, response);
		}

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null, null);

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

}