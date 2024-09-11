package com.Email;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.Model.CartItem;
import com.Model.CartList;
import com.Model.PujaAppointment;
import com.Model.User;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;


@Service
public class JavaEmailSender {
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
    private Configuration freemarkerConfig;
	
	public void sendInvoiceEmail(String to, String subject, String text, byte[] pdfBytes) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        ByteArrayResource pdfAttachment = new ByteArrayResource(pdfBytes);
        helper.addAttachment("invoice.pdf", pdfAttachment);

        mailSender.send(message);
    }
	
	public void sendEmail(User user,CartList cartList, byte[] pdfBytes) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
       
        helper.setTo("imsaikumar@gmail.com");
        helper.setSubject("Your Spiritual Experience Awaits with PUJACANADA: Order and Appointment Confirmation");
        float total =0;
        for(CartItem cartItem :cartList.getCartItems()) {
        	total+=cartItem.getTotal();
        }
        for(PujaAppointment appointment :cartList.getUserCartListPujas()) {
        	total+=appointment.getPujaFee();
        }
        
        Map<String, Object> model = new HashMap<>();
        model.put("username", user.getUserName());
        model.put("products", cartList.getCartItems());
        model.put("appointments", cartList.getUserCartListPujas());
        model.put("totalAmount", total);

        Template t = freemarkerConfig.getTemplate("email-template.ftl");
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

        helper.setText(text, true);
        ByteArrayResource pdfAttachment = new ByteArrayResource(pdfBytes);
        helper.addAttachment("PujaCanada_invoice.pdf", pdfAttachment);

        mailSender.send(message);
        
//        for(CartItem cartItem :cartList.getCartItems()) {
//        	cartItem.getProduct().getName();
//        	cartItem.getQuantity();
//        	cartItem.getProduct().getPrice();
//        	cartItem.getProduct().getDiscount();
//        	cartItem.getTotal();
//        }
//        
//        for(PujaAppointment appointment :cartList.getUserCartListPujas()) {
//        	appointment.getPuja().getName();
//        	appointment.getAppointmentDate().getTime();
//        	appointment.getAppointmentDate().getDate();
//        	appointment.getPujaPriest().getUser().getUserName();
//        	appointment.isCompletedStatus();
//        	appointment.getPuja().getPrice();
//        }
    }
	

}
