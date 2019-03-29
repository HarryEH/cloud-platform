package uk.ac.sheffield.cloud.authserver;

import org.springframework.security.core.userdetails.User;

public class CustomUser extends User {

	private static final long serialVersionUID = 1L;

	public CustomUser(UserModel userModel) {
		super(userModel.getUsername(), userModel.getPassword(), userModel.getGrantedAuthoritiesList());
	}
}
