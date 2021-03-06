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
import idu.cs.entity.UserEntity;
import idu.cs.exception.ResourceNotFoundException;
import idu.cs.repository.UserRepository;
import idu.cs.service.UserService;

@Controller
public class UserController {
	// @Autowired UserRepository userRepo; // Dependency Injection
	@Autowired UserService userService;
	
	@GetMapping("/")
	public String home(Model model) {
		return "index";
	}
	@GetMapping("/user-login-form")
	public String getLoginForm(Model model) {
		return "login";
	}

	@PostMapping("/login")
	public String loginUser(@Valid UserEntity user, HttpSession session) {
		System.out.println("login process: " + user.getUserId());
		User sessionUser = userService.getUserByUserId(user.getUserId());
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
	
	@GetMapping("/users")
	public String getAllUser(Model model, HttpSession session) {
		model.addAttribute("users", userService.getUsers());
		return "userlist";
	}

	@GetMapping("/user-register-form")
	public String getRegForm(Model model) {
		return "register";
	}
	
	@PostMapping("/users")
	public String createUser(@Valid User user, Model model) {
		userService.saveUser(user);
		return "redirect:/users"; // get 방식으로 해당 url에 redirect
	}

	@GetMapping("/user-update-form")
	public String getUpdateForm(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		userService.getUserById(user.getId());
		model.addAttribute("user", user);
		return "info";
	}
	
	@PutMapping("/users/{id}") // @PatchMapping - 수정한 필드만 고쳐주는 메소드
	public String updateU(@PathVariable(value = "id") Long id, @Valid User user, Model model, HttpSession session) {
		/*
		 * updateUser 객체는 사용자가 입력한 폼의 값 내용 : id값이 없음.
		 */
		user.setId(userService.getUserById(id).getId());
		userService.updateUser(user);
		session.setAttribute("user", user);
		return "redirect:/users";
	}
	
	/*
	 @GetMapping("/update/{userId}")
	
	public String getUpdateForm(@PathVariable(value = "userId") Long userId, Model model) throws ResourceNotFoundException{
		UserEntity user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
		model.addAttribute("user", user);
		return "update";
		
	}
	@PutMapping("/update/{userId}") // @PatchMapping - 수정한 필드만 고쳐주는 메소드
	public String updateUser(@PathVariable(value = "userId") Long userId, @Valid UserEntity userDetails, Model model) {
		UserEntity user = userRepo.findById(userId).get(); // user는 DB로부터 읽어온 객체
		user.setName(userDetails.getName()); // userDetails는 전송한 객체
		user.setUserPw(userDetails.getUserPw());
		user.setCompany(userDetails.getCompany());
		userRepo.save(user);
		return "redirect:/users";
	}
	@DeleteMapping("/delete/{userId}")
	public String delteUser(@PathVariable(value = "userId") Long userId, Model model) {
		UserEntity user = userRepo.findById(userId).get();
		userRepo.delete(user);
		model.addAttribute("id", user.getId());
		model.addAttribute("name", user.getName());
		model.addAttribute("company", user.getCompany());
		return "redirect:/users";
	}


	@GetMapping("/users/{id}")
	public String getUserById(@PathVariable(value = "id") Long userId, Model model)
			throws ResourceNotFoundException {
		UserEntity user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
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
		List<UserEntity> users = userRepo.findByName(name);
		model.addAttribute("users", users);
		return "userlist";
		//return ResponseEntity.ok().body(user);
	}

	@DeleteMapping("/users/{id}")
	public String delteU(@PathVariable(value = "id") Long userId, Model model) {
		UserEntity user = userRepo.findById(userId).get();
		userRepo.delete(user);
		model.addAttribute("id", user.getId());
		model.addAttribute("name", user.getName());
		model.addAttribute("company", user.getCompany());
		return "user-deleted";
	}
	 */

}