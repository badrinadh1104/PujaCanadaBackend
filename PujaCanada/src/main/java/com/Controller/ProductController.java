package com.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Model.Product;
import com.Model.Puja;
import com.Service.ProductService;
import com.dto.ProductDTO;

@RestController
@RequestMapping("Product")
@CrossOrigin
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
//	@PostMapping(value = "AddProduct")
//	public Product AddProduct(@RequestBody Product product) throws Exception {
//		return productService.AddProduct(product);
//	}
	
//	@PostMapping(value = "AddProductWithImage", consumes = "multipart/form-data")
//    public ResponseEntity<?> uploadProduct(
//            @RequestPart("product") ProductDTO productDto,
//            @RequestPart("image") MultipartFile image) throws Exception {
//		try {
//            // Convert MultipartFile to byte array
//            byte[] imageBytes = image.getBytes();
//
//            // Create a Product instance from ProductDto
//            Product product = new Product();
//            product.setName(productDto.getName());
//            product.setCategory(productDto.getCategory());
//            product.setPrice(productDto.getPrice());
//            product.setDiscount(productDto.getDiscount());
//            product.setAvailable(productDto.isAvailable());
//            product.setQuantity(productDto.getQuantity());
//            product.setImage(imageBytes);
//
//            // Save the product using the service
//            productService.AddProduct(product);
//
//            return ResponseEntity.ok("Product uploaded successfully!");
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while uploading image.");
//        }
//	}
	
	
	@PostMapping(value = "AddProductWithImage", consumes = "multipart/form-data",produces = "text/plain")
	public ResponseEntity<?> uploadProduct(
	        @RequestParam("name") String name,
	        @RequestParam("category") String category,
	        @RequestParam("price") float price,
	        @RequestParam("discount") int discount,
	        @RequestParam("quantity") int quantity,
	        @RequestParam("available") boolean available,
	        @RequestParam("image") MultipartFile image) throws Exception {
	    try {
	        // Convert MultipartFile to byte array
	        byte[] imageBytes = image.getBytes();

	        // Create a Product instance from the request parameters
	        Product product = new Product();
	        product.setName(name);
	        product.setCategory(category);
	        product.setPrice(price);
	        product.setDiscount(discount);
	        product.setAvailable(available);
	        product.setQuantity(quantity);
	        product.setImage(imageBytes);

	        // Save the product using the service
	        productService.AddProduct(product);

	        return ResponseEntity.ok("Product uploaded successfully!");
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while uploading image.");
	    }
	}

	
	
	@PutMapping("/{productId}/discount")
    public ResponseEntity<Product> changeDiscount(
            @PathVariable Long productId,
            @RequestParam int discount) {
        try {
            Product updatedProduct = productService.ChangeDiscount(productId, discount);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
	
	@PutMapping("/{productId}/quantity")
    public ResponseEntity<Product> changeQuantity(
            @PathVariable Long productId,
            @RequestParam int quantity) {
        try {
            Product updatedProduct = productService.changeQuantity(productId, quantity);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
	@GetMapping(value = "GetAllProducts")
	public ResponseEntity<List<Product>> GetAllProducts(){
		List<Product> products = productService.getAllproducts();
		return ResponseEntity.ok(products);
	}
	@GetMapping("/getImage/{productId}")
	public ResponseEntity<byte[]> getImage(@PathVariable(name="productId") Long ProductId) throws Exception {
		
		Product product = productService.getProductById(ProductId);
		if(product != null) {
			 byte[] imageBytes = product.getImage();
			 HttpHeaders headers = new HttpHeaders();
			 headers.setContentType(MediaType.IMAGE_JPEG);
			 return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
		}else {
			 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	   
	}
	
	@PostMapping(value = "/import-products",consumes = "multipart/form-data")
    public String importProducts(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty. Please upload a valid CSV file.";
        }
        try {
            return productService.importProducts(file);
        } catch (Exception e) {
            return "Error during file import: " + e.getMessage();
        }
    }
	
	 @DeleteMapping("/{productId}")
	    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) throws Exception {
		 return productService.DeleteProduct(productId);
	 }
	 @PostMapping("/upload-images")
	    public ResponseEntity<String> uploadProductImages(@RequestParam("files") MultipartFile[] files) {
	        try {
	            productService.uploadImagesToDatabase(files);
	            return ResponseEntity.ok("Images uploaded successfully and products updated.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading images: " + e.getMessage());
	        }
	    }
	 
//	 
//	 @PostMapping(value  = "/upload-images-zip",consumes = "multipart/form-data")
//	    public ResponseEntity<String> uploadProductImagesZip(@RequestParam("file") MultipartFile file) {
//	        if (file.isEmpty()) {
//	            return ResponseEntity.badRequest().body("File is empty. Please upload a valid ZIP file.");
//	        }
//
//	        try {
//	            Path tempDir = Files.createTempDirectory("");
//	            File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
//	            file.transferTo(tempFile);
//
//	            productService.extractAndProcessZip(tempFile, tempDir);
//
//	            return ResponseEntity.ok("Images uploaded successfully and products updated.");
//	        } catch (Exception e) {
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading images: " + e.getMessage());
//	        }
//	    }
	 
	 @PostMapping(value = "/upload-zip-images",consumes = "multipart/form-data")
	    public ResponseEntity<String> uploadImages(@RequestParam("file") MultipartFile file) {
	        try {
	            productService.uploadImagesFromZip(file);
	            return ResponseEntity.ok("Images uploaded successfully.");
	        } catch (IOException e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body("Error uploading images: " + e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body("Unexpected error: " + e.getMessage());
	        }
	    }
	 
	 @GetMapping(value = "getProductById/{prodId}")
	 public Product getProductById(@PathVariable(name = "prodId") Long prodId) throws Exception {
		 return productService.getProductById(prodId);
	 }

}
