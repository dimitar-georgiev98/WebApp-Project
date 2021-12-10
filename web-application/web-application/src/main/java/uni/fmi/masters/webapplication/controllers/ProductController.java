package uni.fmi.masters.webapplication.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import uni.fmi.masters.webapplication.entities.ProductEntity;
import uni.fmi.masters.webapplication.entities.UserEntity;
import uni.fmi.masters.webapplication.repositories.ProductRepository;

@RestController
public class ProductController {

	ProductRepository productRepo;

	public ProductController(ProductRepository productRepo) {
		this.productRepo = productRepo;
		
	}

	@PostMapping(path = "/add")
	public String addProduct(
			@RequestParam(value = "name") String name,
			@RequestParam(value = "description") String description, 
			@RequestParam(value = "price") double price,
			HttpSession session) {

		UserEntity user = (UserEntity) session.getAttribute("user");

		if (user != null) {

			ProductEntity product = new ProductEntity();

			product.setName(name);
			product.setDescription(description);
			product.setPrice(price);
			product.setUser(user);

			product = productRepo.saveAndFlush(product);

			if (product != null) {
				return String.valueOf(product.getId());
			}

			return "Product not inserted!";
		}
		return "Error: there is no logged user!";
	}

	@PostMapping(path = "/update")
	public String updateProduct(@RequestParam(value = "productId") int id, @RequestParam(value = "name") String name,
			@RequestParam(value = "description") String description, @RequestParam(value = "price") double price,
			HttpSession session) {

		UserEntity user = (UserEntity) session.getAttribute("user");

		if (user != null) {

			Optional<ProductEntity> optionalProduct = productRepo.findById(id);

			if (optionalProduct.isPresent()) {

				ProductEntity product = optionalProduct.get();

				product.setId(id);
				product.setName(name);
				product.setDescription(description);
				product.setPrice(price);
				product.setUser(user);

				product = productRepo.saveAndFlush(product);

				if (product != null) {
					return String.valueOf(product.getId());
				}
			}

			return "Product not inserted!";
		}
		return "Error: there is no logged user!";
	}
	
	
	@GetMapping(path="/product/all")
	public ResponseEntity<List<ProductEntity>> getAllProducts(HttpSession session){
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		
		if(user != null) {
			List<ProductEntity> products = productRepo.findByUser(user);
			
			return new ResponseEntity<List<ProductEntity>>(products, HttpStatus.OK);
		}
		return new ResponseEntity<List<ProductEntity>>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
	}
	
	
	@DeleteMapping(path="/product/delete")
	public ResponseEntity<Boolean> deleteProduct(
			@RequestParam(value = "id") int id,
			HttpSession session){
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		
		if(user == null) {
			return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
		}
		
		Optional<ProductEntity> optionalProduct = productRepo.findById(id);
		
		if(optionalProduct.isPresent()) {
			ProductEntity product = optionalProduct.get();
			
			if(product.getUser().getId() == user.getId()) {
				productRepo.delete(product);
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			}else {
				return new ResponseEntity<Boolean>(false, HttpStatus.FORBIDDEN);
			}
		
		}else {
			return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
		}
		
	}
}