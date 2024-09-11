package com.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import com.Exceptions.UserAlreadyExistException;
import com.Model.CartItem;
import com.Model.CartList;
import com.Model.Product;
import com.Model.Puja;
import com.Model.PujaAppointment;
import com.Model.PujaUserOrder;
import com.Model.User;
import com.Model.UserAddress;
import com.Model.WishList;
import com.Repository.CartItemRepository;
import com.Repository.CartListRepository;
import com.Repository.ImportStatusRepository;
import com.Repository.ProductRepository;
import com.Repository.PujaAppointmentRepository;
import com.Repository.PujaRepository;
import com.Repository.PujaUserOrderRepository;
import com.Repository.UserAddressRepository;
import com.Repository.UserRepository;
import com.Repository.WishListRepository;
import com.dto.ImportStatus;
import com.dto.UserSignup;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private WishListRepository wishListRepository;

	@Autowired
	private CartListRepository cartListRepository;

	@Autowired
	private PujaRepository pujaRepository;

	@Autowired
	private PujaUserOrderRepository orderRepository;

	@Autowired
	private UserAddressRepository userAddressRepository;

	@Autowired
	private PujaAppointmentRepository pujaAppointmentRepository;

	@Autowired
	private CartItemRepository cartItemRepository;
	
	
	
	
	@Autowired
    private ImportStatusRepository importStatusRepository;

//	to register a new User 

	public User RegisterUser(UserSignup signup) throws UserAlreadyExistException {
		User pujauser = userRepository.findByEmail(signup.getEmail());
		if (pujauser == null) {
//			logger.info("created a user "+user.getEmail());
			WishList wishList = new WishList();
			wishListRepository.save(wishList);
			CartList cartList = new CartList();
			cartListRepository.save(cartList);
			UserAddress userAddress = new UserAddress(signup.getAddress(), signup.getCity(), signup.getProvince(),
					signup.getCountry(), signup.getZipcode());
			User user = new User();
			user.setUserWishList(wishList);
			user.setUserCartList(cartList);
			user.setEmail(signup.getEmail());
			user.setPassword(signup.getPassword());
			user.setRole(signup.getRole());
			user.setPhone(signup.getPhone());
			user.setUserName(signup.getUserName());
			userAddressRepository.save(userAddress);
			user.setUserAddress(userAddress);
			return userRepository.save(user);
		} else {
			throw new UserAlreadyExistException("User with " + signup.getEmail() + " already Exist in our database");

		}
	}

//	add a product to the userWishlist
	public WishList addProductToUserWishList(Long userId, Long productId) throws Exception {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User does not exist with User ID " + userId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new NotFoundException("Product does not exist with Product ID " + productId));

		WishList wishList = user.getUserWishList();

		if (wishList.getUserWishlistProducts().contains(product)) {
			throw new Exception("Product already exists in your wishlist");
		}

		wishList.AddProducttowishlist(product);
		wishListRepository.save(wishList);
		userRepository.save(user);

		return wishList;
	}

//	Addpuja to the UserWishlist

//	public User AddPujatoUserWishList(Long userId, Long pujaId) {
//		User user = userRepository.findById(userId).get();
//		Puja puja = pujaRepository.findById(pujaId).get();
//		if (user != null) {
//			if (puja != null) {
//				user.getUserWishList().AddPujatoWishlistPuja(puja);
//				wishListRepository.save(user.getUserWishList());
//				return userRepository.save(user);
//			} else {
//				throw new NotFoundException("Puja does not Exist with Puja ID " + pujaId);
//			}
//		} else {
//			throw new NotFoundException("User does not Exist with User ID " + userId);
//		}
//
//	}

//	add a product to the usercartlist

	public List<CartItem> AddProducttoUserCartList(Long userId, Long productId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User does not exist with User ID " + userId));
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new NotFoundException("Product does not exist with Product ID " + productId));
		boolean productexistIncart = false;
		WishList wishList = user.getUserWishList();
		wishList.getUserWishlistProducts().remove(product);
		wishListRepository.save(wishList);

		for (CartItem item : user.getUserCartList().getCartItems()) {
			if (item.getProduct().getId().equals(productId)) {
				item.setQuantity(item.getQuantity() + 1);
				float discount = product.getPrice() * (product.getDiscount() / 100.0f);
//				System.out.println(discount);
				item.setTotal(item.getTotal() + (product.getPrice() - discount));
				productexistIncart = true;
				break;
			}
		}
		if (!productexistIncart) {
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setQuantity(1);
			float discount = product.getPrice() * (product.getDiscount() / 100.0f);
			System.out.println(discount);
			cartItem.setTotal(product.getPrice() - discount);
			cartItem.setCartList(user.getUserCartList());
			user.getUserCartList().AddCartItem(cartItem);
		}
		float cartListTotal = CalculateTotalPay(user.getUserCartList());
		user.getUserCartList().setCartTotal(cartListTotal);
		userRepository.save(user);
		return user.getUserCartList().getCartItems();
	}
	
	
	public float CalculateTotalPay(CartList cartList) {
		float totalamount = 0;
		if (cartList.getCartItems() != null) {
			for (CartItem cartItem : cartList.getCartItems()) {
				totalamount = totalamount + cartItem.getTotal();
			}
		}
		if (cartList.getUserCartListPujas() != null) {
			for (PujaAppointment appointment : cartList.getUserCartListPujas()) {
				totalamount = totalamount + appointment.getPujaFee();
			}
		}
		return totalamount;
	}

//	adding puja to user Cartlist

//	!!!!!!!!!!!!!!copied to Appointment service
//	public User AddPujatoUserCartList(Long userId, Long pujaId) {
//		User user = userRepository.findById(userId).get();
//		Puja puja = pujaRepository.findById(pujaId).get();
//		if (user != null) {
//			if (puja != null) {
//				user.getUserCartList().AddPujatoCartlistPuja(puja);
//				cartListRepository.save(user.getUserCartList());
//				return userRepository.save(user);
//			} else {
//				throw new NotFoundException("puja does not Exist with Puja ID " + pujaId);
//			}
//		} else {
//			throw new NotFoundException("User does not Exist with User ID " + userId);
//		}
//	}

//	to get the user cartList
	public CartList getUserCartList(Long userId) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User Not Exist with Id" + userId));
		return user.getUserCartList();
	}

	public WishList getUserWishList(Long userId) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User Not Exist with Id" + userId));
		return user.getUserWishList();
	}

//	user checkout
//	public PujaUserOrder UserCheckout(Long userId) {
//		User user = userRepository.findById(userId).get();
//		if (user != null) {
//			PujaUserOrder order = new PujaUserOrder();
//			order.setUser(user);
//			for(Product p :user.getUserCartList().getUserCartlistProducts()) {
//				order.AddtouserOrderedProducts(p);
//			}
//			for(Puja puja :user.getUserCartList().getUserCartListPujas()) {
//				order.AddtouserOrderedPujas(puja);
//			}
////			to remove the products from cartlist
//			for(Product prod :user.getUserCartList().getUserCartlistProducts()) {
//				user.getUserCartList().getUserCartlistProducts().remove(prod);
//			}
//			for(Puja pujad :user.getUserCartList().getUserCartListPujas()) {
//				user.getUserCartList().getUserCartListPujas().remove(order);
//			}
//			user.AddOrder(order);
//			cartListRepository.save(user.getUserCartList());
//			orderRepository.save(order);
//			userRepository.save(user);
//			return orderRepository.save(order);
//			
//		}else {
//			throw new NotFoundException("User does not Exist with User ID " + userId);
//		}
//	}

//	to get All Users
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

//	to get All User Appointments

	public List<PujaAppointment> UserPujaAppointmnets(Long UserId) {
		User user = userRepository.findById(UserId).get();
		return user.getPujaAppointments();
	}

	public CartList clearUserCartList(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User does not exist with User ID " + userId));
		for (CartItem cartItem : user.getUserCartList().getCartItems()) {
			cartItem.setCartList(null);
			cartItemRepository.save(cartItem); // Update the cartItem to reflect the change
		}
		user.getUserCartList().getCartItems().clear();
		user.getUserCartList().getUserCartListPujas().clear();
		cartListRepository.save(user.getUserCartList());
		userRepository.save(user);
		return user.getUserCartList();
	}
	public WishList clearUserWishList(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User does not exist with User ID " + userId));
		user.getUserWishList().getUserWishlistProducts().clear();
		user.getUserWishList().getUserPujas().clear();
		userRepository.save(null);
		return user.getUserWishList();
		
	}

	public void deleteUserCartList(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException("User does not exist with User ID " + userId));
		user.getUserCartList().getCartItems().clear();
		user.getUserCartList().getUserCartListPujas().clear();
		cartListRepository.save(user.getUserCartList());
		userRepository.save(user);
	}

	
	public User getUserById(Long UserId) throws Exception {
		User user = userRepository.findById(UserId).orElseThrow(()-> new Exception("User Not found With Id "+UserId));
		return user;
	}
	
	@Transactional
    public synchronized String importFAKEUsers(MultipartFile file) {
        if (isAlreadyImported()) {
            return "Import already completed.";
        }

        try {
            // Perform the import logic
            ImportFakeUserData(file);
            markAsImported("USERS");
            return "USERS imported successfully.";
        } catch (Exception e) {
            return "Error importing USERS: " + e.getMessage();
        }
    }
	
	private boolean isAlreadyImported() {
        return importStatusRepository.existsByImportType("USERS");
    }
	
	 private void markAsImported(String importType) {
	        ImportStatus status = new ImportStatus();
	        status.setImportType(importType);
	        status.setStatus(true);
	        importStatusRepository.save(status);
	    }
	public void ImportFakeUserData(MultipartFile file) throws IOException, CsvException {
		try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
			CSVReader csvReader = new CSVReader(bufferedReader);
			List<String[]> rows = csvReader.readAll();
			rows.remove(0);
			for(String[] row:rows) {
				User user= new User();
				UserAddress address = new UserAddress();
				user.setUserName(row[0]);
				user.setEmail(row[1]);
				user.setPhone(row[2]);
				user.setPassword(row[3]);
				user.setRole(row[4]);
				address.setAddress(row[5]);
				address.setCity(row[6]);
				address.setProvince(row[7]);
				address.setCountry(row[8]);
				address.setZipcode(row[9]);
				
				userAddressRepository.save(address);
				user.setUserAddress(address);
				userRepository.save(user);
				
			}
		}
	}
	
	public String DeletUser(Long UserID) throws Exception {
		
		try {
			User user = userRepository.findById(UserID).orElseThrow(()-> new Exception("User Not found With Id "+UserID));
			userRepository.delete(user);
			return "Delete SuccessFull";
		}catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
	}
	
	public void deleteUser(Long userId) {
//		try {
//	        User user = userRepository.findById(userId)
//	            .orElseThrow(() -> new RuntimeException("User not found with Id " + userId));
////	        clearUserCartList(userId);
////	        clearUserWishList(userId);
//	        userRepository.delete(user);
//	        System.out.println("User deleted successfully");
//	    } catch (Exception e) {
//	        System.err.println("Error deleting user: " + e.getMessage());
//	    }
//	}
		User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found with Id " + userId));
		userRepository.delete(user);
		}
}
