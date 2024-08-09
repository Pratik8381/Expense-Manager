package com.service;
import com.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		message.setFrom("your-email@example.com");

		mailSender.send(message);
	}
	@Autowired
	private JavaMailSender javaMailSender;

	public void sendEmail(Email email)
	{

		try {

			MimeMessage mmessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mhelper = new MimeMessageHelper(mmessage);

			mhelper.setFrom("ContactManager");
			mhelper.setTo(email.getTo());
			mhelper.setSubject(email.getSubject());
			mhelper.setText(email.getMessage());
			mmessage.setContent(email.getMessage(),"text/html");


			javaMailSender.send(mmessage);



		}catch(Exception e) {
			e.printStackTrace();
		}

	}
}

