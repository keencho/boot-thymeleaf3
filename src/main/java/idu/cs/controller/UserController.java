package idu.cs.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import idu.cs.domain.User;
import idu.cs.exception.ResourceNotFoundException;
import idu.cs.repository.UserRepository;

@Controller
public class UserController {
	@Autowired UserRepository userRepo; // Dependency Injection
	
	@GetMapping("/")
	public String home(Model model) {
		return "index";
	}
	@GetMapping("/user-login-form")
	public String getLoginForm(Model model) {
		return "login";
	}
	@PostMapping("/login")
	public String loginUser(@Valid User user, HttpSession session) {
		System.out.println("login process: " + user.getUserId());
		User sessionUser = userRepo.findByUserId(user.getUserId());
		if(sessionUser == null) {
			System.out.println("id error: ");
			return "redirect:/user-login-form";
		}
		if(!sessionUser.getUserPw().equals(user.getUserPw())) {
			System.out.println("pw error: ");
			return "redirect:/user-login-form";
		}
		session.setAttribute("user", sessionUser);
		session.setAttribute("userId", sessionUser.getId());
		return "redirect:/";
	}
	@GetMapping("/logout")
	public String logoutUser(HttpSession session){
		session.removeAttribute("user");
		// session invalidate();
		return "redirect:/";
	}
	@GetMapping("/user-register-form")
	public String getRegForm(Model model) {
		return "register";
	}
	@GetMapping("/update/{userId}")
	public String getUpdateForm(@PathVariable(value = "userId") Long userId, Model model) throws ResourceNotFoundException{
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		model.addAttribute("user", user);
		return "update";
		
	}
	@PutMapping("/update/{userId}") // @PatchMapping - 수정한 필드만 고쳐주는 메소드
	public String updateUser(@PathVariable(value = "userId") Long userId, @Valid User userDetails, Model model) {
		User user = userRepo.findById(userId).get(); // user는 DB로부터 읽어온 객체
		user.setName(userDetails.getName()); // userDetails는 전송한 객체
		user.setUserPw(userDetails.getUserPw());
		user.setCompany(userDetails.getCompany());
		userRepo.save(user);
		return "redirect:/users";
	}
	@DeleteMapping("/delete/{userId}")
	public String delteUser(@PathVariable(value = "userId") Long userId, Model model) {
		User user = userRepo.findById(userId).get();
		userRepo.delete(user);
		model.addAttribute("id", user.getId());
		model.addAttribute("name", user.getName());
		model.addAttribute("company", user.getCompany());
		return "redirect:/users";
	}
	@GetMapping("/users")
	public String getAllUser(Model model) {
		model.addAttribute("users", userRepo.findAll());
		return "userlist";
	}
	@PostMapping("/users")
	public String createUser(@Valid User user, Model model) {
		if(userRepo.save(user) != null)
			System.out.println("Database register complete");
		else
			System.out.println("Datbase register fail");
		model.addAttribute("users", userRepo.findAll());
		return "redirect:/users";
	}
	@GetMapping("/users/{id}")
	public String getUserById(@PathVariable(value = "id") Long userId, Model model)
			throws ResourceNotFoundException {
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		// model.addAttribute("id", user.getId());
		// model.addAttribute("name", user.getName());
		// model.addAttribute("company", user.getCompany());
		model.addAttribute("user", user);
		return "user";
		//return ResponseEntity.ok().body(user);
	}
	@GetMapping("/users/fn")
	public String getUserByName(@Param(value="name") String name, Model model)
			throws ResourceNotFoundException {
		List<User> users = userRepo.findByName(name);
		model.addAttribute("users", users);
		return "userlist";
		//return ResponseEntity.ok().body(user);
	}
	@PutMapping("/users/{id}") // @PatchMapping - 수정한 필드만 고쳐주는 메소드
	public String updateU(@PathVariable(value = "id") Long userId, @Valid User userDetails, Model model) {
		User user = userRepo.findById(userId).get(); // user는 DB로부터 읽어온 객체
		user.setName(userDetails.getName()); // userDetails는 전송한 객체
		user.setCompany(userDetails.getCompany());
		userRepo.save(user);
		return "redirect:/users";
	}
	@DeleteMapping("/users/{id}")
	public String delteU(@PathVariable(value = "id") Long userId, Model model) {
		User user = userRepo.findById(userId).get();
		userRepo.delete(user);
		model.addAttribute("id", user.getId());
		model.addAttribute("name", user.getName());
		model.addAttribute("company", user.getCompany());
		return "user-deleted";
	}
}
// test2