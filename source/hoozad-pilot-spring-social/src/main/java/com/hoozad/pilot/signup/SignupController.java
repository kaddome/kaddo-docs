/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hoozad.pilot.signup;

import com.hoozad.pilot.account.Account;
import com.hoozad.pilot.account.AccountRepository;
import com.hoozad.pilot.account.UsernameAlreadyInUseException;
import com.hoozad.pilot.signin.SignInUtils;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.inject.Inject;

@Controller
public class SignupController {

	private final AccountRepository accountRepository;
	private final ProviderSignInUtils providerSignInUtils;

	@Inject
	public SignupController(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
		this.providerSignInUtils = new ProviderSignInUtils();
	}

	@RequestMapping(value="/signup", method=RequestMethod.GET)
	public String signupForm(WebRequest request) throws UsernameAlreadyInUseException {
		Connection<Facebook> connection = (Connection<Facebook>) providerSignInUtils.getConnectionFromSession(request);
		if (connection != null) {
			
			Account account = createAccount(connection.getApi().userOperations().getUserProfile());
			if (account != null) {
				SignInUtils.signin(account.getUsername());
				providerSignInUtils.doPostSignUp(account.getUsername(), request);
				return "redirect:/";
			}
		} 
		return null;
	}

	private Account createAccount(User user) throws UsernameAlreadyInUseException {
		Account account = new Account(user.getId(), user.getFirstName(), user.getLastName());
		accountRepository.createAccount(account);
		return account;
	}

}
