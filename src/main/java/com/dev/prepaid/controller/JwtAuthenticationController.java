package com.dev.prepaid.controller;

import com.dev.prepaid.model.invocation.InstanceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.prepaid.model.JwtRequest;
import com.dev.prepaid.model.JwtResponse;
import com.dev.prepaid.service.JwtUserDetailsService;
import com.dev.prepaid.util.JwtTokenUtil;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/")
public class JwtAuthenticationController {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@PostMapping("auths")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}

	@PostMapping("generateTokenExportProduct")
	public ResponseEntity<?> createJwtAuthorizationHeader(@RequestBody InstanceContext instanceContext) throws Exception {
		Map<String, Object> claims = new HashMap<>();
		log.info("{}", instanceContext);
		String token = jwtTokenUtil.generateTokenExportProduct(null, instanceContext);

		return ResponseEntity.ok(new JwtResponse(token));
	}
}
