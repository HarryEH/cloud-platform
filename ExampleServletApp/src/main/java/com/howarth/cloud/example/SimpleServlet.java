package com.howarth.cloud.example;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import sun.security.util.SecurityConstants;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleServlet extends HttpServlet {

	private static final long serialVersionUID = -4751096228274971485L;

	@Override
	protected void doGet(HttpServletRequest reqest, HttpServletResponse response) throws ServletException, IOException {
		// Check Cookie
		if (validateCookie(reqest)) {
			response.getWriter().println("You are a verified user!");

			// Check Inform Payment server
			informPaymentServer(reqest);

			response.getWriter().println("Hello World!");
		} else {
			response.getWriter().println("You would be redirected now!");
		}
	}

	private void informPaymentServer(HttpServletRequest request) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {

			// This automatically gets the correct URL to sent the request to.
			final String URL = request.getScheme() + "://" + request.getServerName() +
					("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
					"/peanut_bank/usage?app_name=ExampleAppUpload&access_token=";

			//access token
			String token = getCookieToken(request);

			final String FINAL_URL = URL + token;

			HttpGet req = new HttpGet(FINAL_URL);

			httpClient.execute(req);

			httpClient.close();
		} catch (Exception ex) {
			// handle exception here
		}
	}


	private boolean validateCookie(HttpServletRequest request) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {

			// This automatically gets the correct URL to sent the request to.
			final String URL = request.getScheme() + "://" + request.getServerName() +
					("http".equals(request.getScheme()) && request.getServerPort() == 80 || "https".equals(request.getScheme()) && request.getServerPort() == 443 ? "" : ":" + request.getServerPort()) +
					"/users/verify_token?access_token=";

			//access token
			String token = getCookieToken(request);

			final String FINAL_URL = URL + token;

			System.out.println(FINAL_URL);

			HttpGet req = new HttpGet(FINAL_URL);

			CloseableHttpResponse response = httpClient.execute(req);

			System.out.println(EntityUtils.toString(response.getEntity()));

			String json = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(json);
			boolean valid = (boolean) jsonObject.get("verified");
			String user = (String) jsonObject.get("username");


			httpClient.close();
			return valid;
		} catch (Exception ex) {
			// handle exception here
		}
		return false;
	}

	private String getCookieToken(HttpServletRequest request) {
		String token = "x.y.z";

		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("access_token")) {
				token = c.getValue();
			}
		}
		return token;
	}
	
	@Override
	public void init() throws ServletException {
		System.out.println("Servlet " + this.getServletName() + " has started");
	}

	@Override
	public void destroy() {
		System.out.println("Servlet " + this.getServletName() + " has stopped");
	}
	
}
