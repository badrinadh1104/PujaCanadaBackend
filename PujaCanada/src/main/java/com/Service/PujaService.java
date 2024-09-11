package com.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import com.Model.Priest;
import com.Model.Product;
import com.Model.Puja;
import com.Model.PujaAppointment;
import com.Model.User;
import com.Repository.ImportStatusRepository;
import com.Repository.PriestRepository;
import com.Repository.PujaAppointmentRepository;
import com.Repository.PujaRepository;
import com.Repository.UserRepository;
import com.dto.ImportStatus;
import com.dto.PujaAppointmentDto;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import jakarta.transaction.Transactional;

@Service
public class PujaService {

	@Autowired
	private PujaRepository pujaRepository;

	@Autowired
	private PriestRepository priestRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PujaAppointmentRepository pujaAppointmentRepository;
	
	@Autowired
    private ImportStatusRepository importStatusRepository;

	public Puja CreateNewPuja(Puja puja) {
		return pujaRepository.save(puja);
	}

//	public Puja AddAppointment(PujaAppointmentDto pujaAppointmentDto) {
//		Puja puja = pujaRepository.findById(pujaAppointmentDto.getPujaId()).get();
//		User user = userRepository.findById(pujaAppointmentDto.getUserId()).get();
//		if (puja != null) {
//			PujaAppointment pujaAppointment = new PujaAppointment();
//			pujaAppointment.setAppointmentDate(pujaAppointmentDto.getAppointmentDate());
//			pujaAppointment.setPuja(puja);
//			pujaAppointment.setConfirmed(false);
//			pujaAppointment.setUserPujaAppointment(user);
//			pujaAppointmentRepository.save(pujaAppointment);
//			return pujaRepository.save(puja);
//
//		} else {
//			throw new NotFoundException(
//					"puja with Id : " + pujaAppointmentDto.getPujaId() + " is not Found in our Database");
//		}
//	}

//	public Priest AddPujaToPriest(Long pujaId, Long priestId) {
//		Puja puja = pujaRepository.findById(pujaId).get();
//		Priest priest = priestRepository.findById(priestId).get();
//		if (priest != null) {
//			if (puja != null) {
//				try {
//					priest.AddpujastoPriest(puja);
//				} catch (Exception e) {
//					System.out.println(e);
//				}
//				return priestRepository.save(priest);
//			} else {
//				throw new NotFoundException("PujaNotFound");
//			}
//		} else {
//			throw new NotFoundException("Priest NotFound");
//		}
//	}
	
//	to edit the Discount Of Puja	
	public Puja ChangeDiscount(Long pujaId,int discount) throws Exception {
		Puja puja = pujaRepository.findById(pujaId).orElseThrow(()-> new Exception("Puja Not Found With Id "+pujaId));
		puja.setDiscount(discount);
		return pujaRepository.save(puja);
	}
	
//	to get All Pujas
	public List<Puja> GetAllPujas(){
		return pujaRepository.findAll();
	}
	
	
	public Puja  getPujaById(Long pujaId) throws Exception {
		Puja puja = pujaRepository.findById(pujaId).orElseThrow(()-> new Exception("Puja Not Found With Id "+pujaId));
		return puja;
	}
	
	
	@Transactional
    public synchronized String importPujas(MultipartFile file) {
        if (isAlreadyImported()) {
            return "Import already completed.";
        }

        try {
            // Perform the import logic
        	performPujaImport(file);
            markAsImported("PUJAS");
            return "Pujas imported successfully.";
        } catch (Exception e) {
            return "Error importing pujas: " + e.getMessage();
        }
    }

    private boolean isAlreadyImported() {
        return importStatusRepository.existsByImportType("PUJAS");
    }
	
	
	
	 private void markAsImported(String importType) {
	        ImportStatus status = new ImportStatus();
	        status.setImportType(importType);
	        status.setStatus(true);
	        importStatusRepository.save(status);
	    }
	 
	private void performPujaImport(MultipartFile file) throws IOException, CsvException {
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
			CSVReader csvReader = new CSVReader(bufferedReader);
			List<String[]> rows = csvReader.readAll();
			rows.remove(0); //skip the header
			System.out.println(rows);
			for(String [] row:rows) {
				Puja puja= new Puja();
				puja.setName(row[0]);
				puja.setDescription(row[1]);
				puja.setPrice(Float.parseFloat(row[2]));
				puja.setDiscount(Integer.parseInt(row[3]));
				Puja puja2 = pujaRepository.findByName(puja.getName());
				if(puja != null) {
					pujaRepository.save(puja);
				}else {
					System.out.println("Product Already Exist");
				}
				
			}
		}
	}
	
	@Transactional
    public void uploadImagesFromZip(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String fileName = new File(entry.getName()).getName(); // Extract file name only
                String pujaName = fileName.substring(0, fileName.lastIndexOf('.')); // Extract product name

                // Trim and normalize the product name (e.g., replace underscores with spaces)
                pujaName = pujaName.replace('_', ' ').trim();
                System.out.println(pujaName);
                // Assuming a method exists to find a product by name
                Puja puja = pujaRepository.findByName(pujaName);
                if (puja != null) {
                	if(puja.getImage()== null) {
                		 byte[] imageData = zis.readAllBytes();
                         puja.setImage(imageData);
                         pujaRepository.save(puja);
                	}else {
                		System.out.println("puja Image  Already Exist ");
                	}
                   
                } else {
                    System.out.println("Puja not found for name: " + pujaName);
                }
            }
        
        } catch (Exception e) {
            throw new RuntimeException("Error uploading images: " + e.getMessage(), e);
        }
    }
	
	
	public String UpdatePuja( Long id,  String name,  String description,
			 float price,  int discount,  MultipartFile file) throws NumberFormatException, Exception {
		System.out.println(id+"    "+name+"        "+description);
		Puja puja = pujaRepository.findById(id).orElseThrow(()-> new Exception("Puja Not Found"));
		 if (name != null && !name.trim().isEmpty() && name != "") {
	            puja.setName(name);
	        }

		 else if (description != null && !description.trim().isEmpty() && description !="" &&description != "" ) {
	            puja.setDescription(description);
	        }
		 else if ( discount<0) {
            puja.setDiscount(discount);
        }
		 else if ( price<0) {
            puja.setPrice(price);
        }
		 else if(file != null) {
			try {
				byte[] image = file.getBytes();
				puja.setImage(image);
			} catch (IOException e) {
				e.printStackTrace();
				return "Error uploading image";
			}
		}
		 else {
			 throw new Exception("ALL FEILDS ARE EMPTY");
		 }
		
		pujaRepository.save(puja);
		return "Update SuccessFull";
		
	}

}

