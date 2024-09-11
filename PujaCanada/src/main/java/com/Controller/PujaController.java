package com.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.Model.Puja;
import com.Service.PujaService;
import com.dto.PujaAppointmentDto;
import com.dto.PujaDTO;
import com.dto.PujaResponse;
import org.apache.commons.codec.binary.Base64;

@RestController
@CrossOrigin
@RequestMapping("puja")
public class PujaController {

	@Autowired
	private PujaService pujaService;

//	@PostMapping(value = "AddNewPuja")
//	public Puja AddPuja(@RequestPart("puja") PujaDTO pujaDTO,@RequestPart("image") MultipartFile image) throws IOException {
//		Puja puja = new Puja();
//		puja.setName(pujaDTO.getName());
//        puja.setDescription(pujaDTO.getDescription());
//        puja.setPrice(pujaDTO.getPrice());
//        puja.setDiscount(pujaDTO.getDiscount());
//        puja.setImage(image.getBytes());
//        return pujaService.CreateNewPuja(puja);
//	}

	@PostMapping(value = "AddnewPuja", consumes = "multipart/form-data")
	public String createPuja(@RequestParam String name, @RequestParam String description, @RequestParam float price,
			@RequestParam int discount, @RequestParam("image") MultipartFile file) {
		try {
			byte[] image = file.getBytes();
			Puja puja = new Puja();
			puja.setName(name);
			puja.setDescription(description);
			puja.setDiscount(discount);
			puja.setPrice(price);
			puja.setImage(image);
			pujaService.CreateNewPuja(puja);
			return "Puja created successfully";
		} catch (IOException e) {
			e.printStackTrace();
			return "Error uploading image";
		}
	}

//	@PostMapping(value = "BookPujaAppointmnet")
//	public Puja BookPujaAppointMent(@RequestBody PujaAppointmentDto appointmentDto) {
//		return pujaService.AddAppointment(appointmentDto);
//	}

	@PutMapping(value = "ChangeDiscount/{pujaId}/{discount}")
	public Puja ChangeDiscount(@PathVariable(name = "pujaId") Long pujaId,
			@PathVariable(name = "discount") int discount) throws Exception {
		return pujaService.ChangeDiscount(pujaId, discount);
	}

	@GetMapping(value = "GetAllPujas")
	public List<Puja> GetAllPujas() {
		return pujaService.GetAllPujas();
	}

	@GetMapping("/getImage/{pujaId}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long pujaId) throws Exception {

		Puja puja = pujaService.getPujaById(pujaId);
		if (puja != null) {
			byte[] imageBytes = puja.getImage();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);
			return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping(value = "/import-pujas", consumes = "multipart/form-data")
	public String importProducts(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return "File is empty. Please upload a valid CSV file.";
		}
		try {
			return pujaService.importPujas(file);
		} catch (Exception e) {
			return "Error during file import: " + e.getMessage();
		}
	}

	@PostMapping(value = "/upload-zip-images", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadImages(@RequestParam("file") MultipartFile file) {
		try {
			pujaService.uploadImagesFromZip(file);
			return ResponseEntity.ok("Images uploaded successfully.");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error uploading images: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
		}
	}

	@GetMapping(value = "getPujaByID/{pujaId}")
	public Puja getpujaById(@PathVariable(name = "pujaId") Long pujaId) throws Exception {
		return pujaService.getPujaById(pujaId);
	}

	@PostMapping(value = "UpdatePuja", consumes = "multipart/form-data")
	public String UpdatePuja(@RequestParam Long id, @RequestParam(value = "name",required = false) String name, @RequestParam(value = "description",required = false) String description,
			@RequestParam(value = "price",defaultValue = "0.0") float price, @RequestParam(value = "discount", defaultValue = "0") int discount, @RequestParam(value = "image",required = false) MultipartFile file) throws NumberFormatException, Exception {
		return pujaService.UpdatePuja(id, name, description, price, discount, file);
	}

}
