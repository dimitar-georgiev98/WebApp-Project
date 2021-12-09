package uni.fmi.masters.webapplication.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uni.fmi.masters.webapplication.WebSecurityConfig;
import uni.fmi.masters.webapplication.entities.UserEntity;
import uni.fmi.masters.webapplication.repositories.UserRepository;

@RestController
public class LoginController {
	
	private UserRepository userRepository;
	private WebSecurityConfig webSecurityConfig;	
		
	public LoginController(UserRepository userRepo, WebSecurityConfig webSecurityConfig) {
		userRepository = userRepo;
		this.webSecurityConfig = webSecurityConfig;
	}	
	
	@PostMapping(path = "/register")
	public UserEntity register( @RequestParam(value = "email") String email,
								@RequestParam(value = "username") String username,
								@RequestParam(value = "password") String password,
								@RequestParam(value = "repeatPassword") String repeatPassword) {
		
		if(password.equals(repeatPassword)) {
			UserEntity user = new UserEntity(username, hashMe(password), email);
			
			return userRepository.saveAndFlush(user);
		}
									
		return null;
	}
	
	@PostMapping(path = "/login")
	public String login(@RequestParam(value="username") String username,
						@RequestParam(value="password") String password,
						HttpSession session) throws UsernameNotFoundException, Exception {
		
		UserEntity user = userRepository.findUserByUsernameAndPassword(username, hashMe(password));
		
		if(user != null) {
			session.setAttribute("user", user);
			
			UserDetails userDetails = 
					webSecurityConfig.userDetailsServiceBean().loadUserByUsername(username);
			
			if(userDetails != null) {
				Authentication auth = 
						new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
																userDetails.getPassword(),
																userDetails.getAuthorities());
				
				SecurityContextHolder.getContext().setAuthentication(auth);
				
				ServletRequestAttributes attr = 
						(ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
				
				session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext()); 
				
				return "home.html";								
			}
		}				
		return "error.html";		
	}
	
	@GetMapping(path = "/loggedUserId")
	public ResponseEntity<Integer>loggedUserId(HttpSession session){
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		
		if(user != null) {
			return new ResponseEntity<Integer> (user.getId(), HttpStatus.OK);
		}else {
			return new ResponseEntity<Integer> (-1, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@PostMapping(path = "/logout")
	public ResponseEntity<Boolean> logout(HttpSession session){
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		
		if (user != null) {
			session.invalidate();
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}else {
			return new ResponseEntity<Boolean>(false, HttpStatus.I_AM_A_TEAPOT);
		}
	}
	
	private String hashMe(String password) {		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			md.update(password.getBytes());
			
			byte[] arr = md.digest();
			
			StringBuilder hash = new StringBuilder();
			
			for(int i = 0; i < arr.length; i++) {
				hash.append((char)arr[i]);
			}
			
			return hash.toString();
			
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		}		
				
		return null;
	}
}