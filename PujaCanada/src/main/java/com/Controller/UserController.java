package com.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Exceptions.UserAlreadyExistException;
import com.Model.CartItem;
import com.Model.CartList;
import com.Model.PujaAppointment;
import com.Model.User;
import com.Model.WishList;
import com.Repository.CartItemRepository;
import com.Repository.PujaAppointmentRepository;
import com.Service.UserService;
import com.dto.UserSignup;

@RestController
@CrossOrigin
@RequestMapping("User")
public class UserController {
	@Autowired
	private UserService service;
	
	@Autowired
	private CartItemRepository cartItemRepository;

//	@Autowired
//	private PujaAppointmentRepository pujaAppointmentRepository;

	@PostMapping(value = "RegisterUser")
	public User RegisterUser(@RequestBody UserSignup userSignup) throws UserAlreadyExistException {
		return service.RegisterUser(userSignup);
	}
	
	@GetMapping(value = "Badrinadh",produces = "text/plain")
	public ResponseEntity<String> Demoname(){
		return ResponseEntity.ok("hi Badrinadh"); 
	}

	@GetMapping(value = "GetAllUsers")
	public List<User> getAllUsers() {
		return service.getAllUsers();
	}

//	@PutMapping(value = "AddPujaToUserCartList/{pujaId}/{userId}")
//	public User AddPujaToUserCartList(@PathVariable(name = "pujaId") Long PujaId,
//			@PathVariable(name = "userId") Long UserId) {
//		return service.AddPujatoUserCartList(UserId, PujaId);
//	}
	
	@PutMapping(value = "AddProducttoUserCartList/{ProductId}/{userId}")
	public List<CartItem> AddProducttoUserCartList(@PathVariable(name = "ProductId") Long ProductId,
			@PathVariable(name = "userId") Long UserId) {
		return service.AddProducttoUserCartList(UserId, ProductId);
	}
	@PutMapping(value = "AddProducttoUserWishList/{ProductId}/{userId}")
	public WishList AddProducttoUserWishList(@PathVariable(name = "ProductId") Long ProductId,
			@PathVariable(name = "userId") Long UserId) throws Exception {
		return service.addProductToUserWishList(UserId, ProductId);
	}
	@GetMapping(value = "getUserCartList/{userId}")
	public CartList getUserCartList(@PathVariable(name = "userId") Long userId) throws Exception {
		return service.getUserCartList(userId);
	}
	@GetMapping(value = "getUserWishList/{userId}")
	public WishList getUserWishList(@PathVariable(name = "userId") Long userId) throws Exception {
		return service.getUserWishList(userId);
	}

//	@GetMapping(value = "getUserPujaAppointments/{userId}")
//	public List<PujaAppointment> getUserPujaAppointments(@PathVariable(name = "userId") Long userId) {
//		return pujaAppointmentRepository.findByPujaId(userId);
//	}
	@GetMapping(value = "getUserPujaAppointments/{userId}")
	public List<PujaAppointment> getUserPujaAppointments(@PathVariable(name = "userId") Long userId) {
		return service.UserPujaAppointmnets(userId);
	}
	
	@DeleteMapping(value = "clearUserCartList/{userId}")
	public CartList clearUserCartList(@PathVariable(name = "userId") Long userId) throws Exception {
		return service.clearUserCartList(userId);
	}
	
	@PostMapping(value = "AddCartItem")
	public CartItem saveCartItem(@RequestBody CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}
	@GetMapping(value = "getUserById/{userId}")
	public User getUserById(@PathVariable(name = "userId") Long UserId) throws Exception {
		return service.getUserById(UserId);
	}
	@PostMapping(value = "/import-FAKEUSERS",consumes = "multipart/form-data")
    public String importProducts(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty. Please upload a valid CSV file.";
        }
        try {
            return service.importFAKEUsers(file);
        } catch (Exception e) {
            return "Error during file import: " + e.getMessage();
        }
    }
	@DeleteMapping(value = "DeleteUser/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable(name = "userId") Long userId) {
	    try {
	        service.deleteUser(userId);
	        return ResponseEntity.ok("Delete successful");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
	    }
	}

}
