package com.Service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.Model.Product;
import com.Model.Puja;
import com.Repository.ImportStatusRepository;
import com.Repository.ProductRepository;
import com.dto.ImportStatus;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
	
	@Autowired
	public ProductRepository productRepository;
	
	@Autowired
    private ImportStatusRepository importStatusRepository;
	
//	Import Products From CSV
	 @Transactional
	    public synchronized String importProducts(MultipartFile file) {
	        if (isAlreadyImported()) {
	            return "Import already completed.";
	        }

	        try {
	            // Perform the import logic
	            performImport(file);
	            markAsImported("PRODUCTS");
	            return "Products imported successfully.";
	        } catch (Exception e) {
	            return "Error importing products: " + e.getMessage();
	        }
	    }

	    private boolean isAlreadyImported() {
	        return importStatusRepository.existsByImportType("PRODUCTS");
	    }

	    private void markAsImported(String importType) {
	        ImportStatus status = new ImportStatus();
	        status.setImportType(importType);
	        status.setStatus(true);
	        importStatusRepository.save(status);
	    }

	    private void performImport(MultipartFile file) throws IOException, CsvException {
	    	   try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
	               CSVReader csvReader = new CSVReader(reader) ;
	               List<String[]> rows = csvReader.readAll();
	               rows.remove(0); // Skip header
	               System.out.println(rows);
	               for (String[] row : rows) {
	                   Product product = new Product();
	                   product.setName(row[0]);
	                   product.setCategory(row[1]);
	                   product.setPrice(Float.parseFloat(row[2]));
	                   product.setDiscount(Integer.parseInt(row[3]));
	                   product.setAvailable(Boolean.parseBoolean(row[4]));
	                   product.setQuantity(Integer.parseInt(row[5]));
//	                   product.setImageUrl(row[6]); // Assuming image URLs or paths are provided
	                // Load image bytes from file
//	                   byte[] imageBytes = loadImageBytes(imageFilePath);
	                   productRepository.save(product);
	               }
	               
//	               hasRun = true;
	               
	           }
	    	   
	    }
//	    private byte[] loadImageBytes(String imageFilePath) throws IOException {
//	        return Files.readAllBytes(Paths.get(imageFilePath));
//	    }
	
	
//	Add Product
	public Product AddProduct(Product product) throws Exception {
		Product product2 = productRepository.findByName(product.getName());
		if(product2 == null) {
			return productRepository.save(product);	
		}else {
			throw new Exception("Product Already Exist");
		}
		
	}
	
//	Change Discount
	public Product ChangeDiscount(Long productId,int discount) throws Exception {
		Product product = productRepository.findById(productId).orElseThrow((()-> new Exception("Product Not Found")));
		product.setDiscount(discount);
		return productRepository.save(product);
			
	}
	
//	Change Product stock 
	public Product changeQuantity(Long productId,int quantity) throws Exception {
		Product product = productRepository.findById(productId).orElseThrow((()-> new Exception("Product Not Found")));
		product.setQuantity(quantity);
		return productRepository.save(product);
	}
	
//	get All Products 
	
	public List<Product> getAllproducts(){
		return productRepository.findAll();
	}
	
	public Product getProductById(Long ProductId) throws Exception {
		Product product= productRepository.findById(ProductId).orElseThrow(()-> new Exception("Product Not Found With Id "+ProductId));
		return product;
	}
	
	public ResponseEntity<String> DeleteProduct(Long productId) throws Exception{
		Product product= getProductById(productId);
		productRepository.delete(product);
		return new ResponseEntity<String>("Deleted Successfully",HttpStatus.OK);
	}
	
	
//	 @Transactional
//	    public void uploadImagesToDatabase(MultipartFile[] files) throws IOException {
//	        for (MultipartFile file : files) {
//	            String originalFilename = file.getOriginalFilename();
//	            if (originalFilename != null) {
//	                String productName = originalFilename.substring(0, originalFilename.lastIndexOf('.')); // Get product name from file name
//	                byte[] imageData = file.getBytes(); // Get the image bytes
//
//	                // Assuming a method exists to find a product by name
//	                Product product = productRepository.findByName(productName);
//	                if (product != null) {
//	                    product.setImage(imageData);
//	                    productRepository.save(product);
//	                } else {
//	                    System.out.println("Product not found for image: " + originalFilename);
//	                }
//	            }
//	        }
//	    }
	
	
	@Transactional
    public void uploadImagesToDatabase(MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null) {
                String productName = originalFilename.substring(0, originalFilename.lastIndexOf('.')); // Get product name from file name
                byte[] imageData = file.getBytes(); // Get the image bytes

                // Assuming a method exists to find a product by name
                Product product = productRepository.findByName(productName);
                if (product != null) {
                    product.setImage(imageData);
                    productRepository.save(product);
                } else {
                    System.out.println("Product not found for image: " + originalFilename);
                }
            }
        }
    }
	
//	@Transactional
//    public void extractAndProcessZip(File zipFile, Path tempDir) throws IOException {
//        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
//            ZipEntry zipEntry;
//            while ((zipEntry = zis.getNextEntry()) != null) {
//                File extractedFile = new File(tempDir.toFile(), zipEntry.getName());
//                Files.copy(zis, extractedFile.toPath());
//
//                String productName = extractedFile.getName().substring(0, extractedFile.getName().lastIndexOf('.'));
//                byte[] imageData = Files.readAllBytes(extractedFile.toPath());
//
//                Product product = productRepository.findByName(productName);
//                if (product != null) {
//                    product.setImage(imageData);
//                    productRepository.save(product);
//                }
//
//                zis.closeEntry();
//            }
//        }
//    }
	
	
	@Transactional
    public void uploadImagesFromZip(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String fileName = new File(entry.getName()).getName(); // Extract file name only
                String productName = fileName.substring(0, fileName.lastIndexOf('.')); // Extract product name

                // Trim and normalize the product name (e.g., replace underscores with spaces)
                productName = productName.replace('_', ' ').trim();
                System.out.println(productName);
                // Assuming a method exists to find a product by name
                Product product = productRepository.findByName(productName);
                if (product != null) {
                	if(product.getImage()== null) {
                		 byte[] imageData = zis.readAllBytes();
                         product.setImage(imageData);
                         productRepository.save(product);
                	}else {
                		System.out.println("Product Image  Already Exist ");
                	}
                   
                } else {
                    System.out.println("Product not found for name: " + productName);
                }
            }
        
        } catch (Exception e) {
            throw new RuntimeException("Error uploading images: " + e.getMessage(), e);
        }
    }

//    private String sanitizeFileName(String fileName) {
//        // Remove the file extension
//        String name = fileName.substring(0, fileName.lastIndexOf('.'));
//
//        // Replace spaces and special characters (adjust according to your requirements)
//        return name.replaceAll("[^a-zA-Z0-9]", "_");
//    }
//
//    private byte[] readImageFile(InputStream inputStream) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int len;
//        while ((len = inputStream.read(buffer)) > -1) {
//            baos.write(buffer, 0, len);
//        }
//        baos.flush();
//        return baos.toByteArray();
//    }
	

}
