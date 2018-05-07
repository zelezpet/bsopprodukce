/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.bma.bsop.backend.service;

import java.util.Collections;
import ml.bma.bsop.backend.data.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author ironman
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final AccountService accountService;

	@Autowired
	public UserDetailsServiceImpl(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Account account = accountService.findOneByEmail(email);
		if (null == account && account.isLocked()) {
			throw new UsernameNotFoundException("No user present with username: " + email);
                        
		} else {
			return new User(account.getEmail(), account.getPassword(), Collections.singletonList(new SimpleGrantedAuthority(account.getAuthority().name())));
		}
	}
}