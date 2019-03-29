package uk.ac.sheffield.cloud.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

	private String clientId = "talk2amareswaran";
	private String clientSecret = "my-secret";
	private String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
			"MIIEpAIBAAKCAQEA3D+zbfBsqrx9w1nDYOuJ56iDFgbfWQKGnw00g8EDPFUIid2o\n" +
			"5HjNkbiUy1w8aOPDTaLvKIR8CSvkAGmD5fGIPy5sfFNuTxAEzGT7WSi3vN85Qf6B\n" +
			"PMTuoMSfJZ3xmppDOXWLfWijslIQyfMLBM9SP7nfdsE5j5rWlYzGDqtIAcMuIsGM\n" +
			"PJBYlLby1o5C7k2hOm4ezu1nIK4U2oOybTV7oVAZwellxU/vAof+TOTnduE252om\n" +
			"9eirF1T9aHfc212zbCbw9ABRtxn1XyW8fFwTJXXDOUd6qSGmsrz16Y+sC501M2da\n" +
			"KQTBEZ71QCoDGqKfdOdbELr73eZ7pLJOTsybqQIDAQABAoIBACyu/8geLu20sA8h\n" +
			"9vGC18eX+IBPhKCaUpWSKEVSs0VaOxE9VzaDC7G0sPtRCzB8hPMLS1n9Bu0Vanxq\n" +
			"68eK6QlocboHcym3JkIReqzsBdrbrtIkwDOEAKW9PkxzbsDe6ySNJG+c+xkCbUWL\n" +
			"eYDQTu9+1kdRjEwGLeLJXKU9Oq6geiQB8qv3ahs8DkMfjKgXKiaMfnaBmffvHm3N\n" +
			"T5Ok4T8PAbV20VuiPaw40SAC3wLZv9YXLPalhVd4AfSr6ezUd+ilS/lHShqSu3Ib\n" +
			"3R2BjVkvvShs01iVuuptRlhm/I83Yp9ci4jzEar1rya0Snia8CdCc2kgPIXp8HEQ\n" +
			"Txa3o8ECgYEA9a4flYQyl95TpUM6/Upmz4qSrqA0GHJltPaHy7yZCqfRRI2gKXQQ\n" +
			"Oz8KpM21USNG1UcpCxlnz0L+abTHioD47LweqX8b1174WYoouei3nGy1QK9OAzf1\n" +
			"wNEIAn1GnhcX5WV9nLo4EWeOfTXCs4Lh4guqwUvnrE/wQY7Dz91zoTMCgYEA5YAb\n" +
			"NjiiVXSm5NmbDgq4kQmu+w+buWUYz3fvoQyi0QOoGTMynZ4x9+ADt0kyr8kdQZcv\n" +
			"fY8hvp8YKrdtMOn7U9X6U8550rjsgY2vZKsUspZOGLdgIgErhcNb5+pVluxvZ2Uw\n" +
			"E5WkbwJWNFYe5ME8uHCUgEjp/6XdxICBvrFLh7MCgYEAnEbeoGUvKZuq6X0SkPw6\n" +
			"2dnz5FDT0pkySSJozxhw4W5Ol6TfNH83s/gt9RdFNw+Rhyl02jKW7ihXXTY5l8ji\n" +
			"jvQ7LOZF94HZyFsx/NAju8UKptLcdP+ru7M7vRmjSfy2np0oggGSmL+ey9beuwGO\n" +
			"XhO90qdN/zX6RjliJV9gpjECgYAYT0+SmMEpSCf9icdQ33XZdysf0PFv2/Oa1lt9\n" +
			"tbbxaO+/a5rq8zzbwtDh81D5KdZ9giQ3qpeqd3O54qAgVEiZAst3YSGqXog+2OFB\n" +
			"SpXwrNcs6w+1ymXvz1fG4x2Z1QWf8Cn9iRZX4+l5tb2kAYwLBXHVfOO8frdU/z+/\n" +
			"EUys3QKBgQClwvMfBVhoF5wYVillhDiAo1Oj+UcD7AjOi27bYeujT0hkuWhBhzcT\n" +
			"JQCye68i9pLjiUggdDaDxHD1KEsMCGHvQPRNhQzUW98SUQWn4LzySm0udzJLlUt0\n" +
			"KSaiF2SelvOSGZZU8Yeb7H2erb95fnZ7L9xqk4bB9LE6s3GA4HrvIQ==\n" +
			"-----END RSA PRIVATE KEY-----";
	private String publicKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDcP7Nt8GyqvH3DWcNg64nnqIMWBt9ZAoafDTSDwQM8VQiJ3ajkeM2RuJTLXDxo48NNou8ohHwJK+QAaYPl8Yg/Lmx8U25PEATMZPtZKLe83zlB/oE8xO6gxJ8lnfGamkM5dYt9aKOyUhDJ8wsEz1I/ud92wTmPmtaVjMYOq0gBwy4iwYw8kFiUtvLWjkLuTaE6bh7O7WcgrhTag7JtNXuhUBnB6WXFT+8Ch/5M5Od24Tbnaib16KsXVP1od9zbXbNsJvD0AFG3GfVfJbx8XBMldcM5R3qpIaayvPXpj6wLnTUzZ1opBMERnvVAKgMaop9051sQuvvd5nuksk5OzJup";

	
	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Bean
	public JwtAccessTokenConverter tokenEnhancer() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(privateKey);
		converter.setVerifierKey(publicKey);
		return converter;
	}


	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(tokenEnhancer());
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore())
				.accessTokenConverter(tokenEnhancer());
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.inMemory().withClient(clientId).secret(clientSecret).scopes("read", "write")
				.authorizedGrantTypes("password", "refresh_token").accessTokenValiditySeconds(20000)
				.refreshTokenValiditySeconds(20000);

	}
	
}
