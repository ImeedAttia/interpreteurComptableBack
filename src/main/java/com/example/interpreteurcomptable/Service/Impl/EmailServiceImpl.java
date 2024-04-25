package com.example.interpreteurcomptable.Service.Impl;

import com.example.interpreteurcomptable.Entities.User;
import com.example.interpreteurcomptable.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendEmailWithTemplate(User user) {
        // Créer une instance de MimeMessage à l'aide de la fonction JavaMailSender
        MimeMessage message = emailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    "UTF-8");

            // Définir l'adresse électronique du destinataire
            helper.setTo(user.getEmail());

            // Définir l'objet de l'e-mail
            helper.setSubject("Subscription Alert");

            // Charger le modèle de courrier électronique à partir d'un fichier ou d'une
            // chaîne de caractères
            String emailTemplate ="<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Security Alert: Account Created</title>\n" +
                    "    <style>\n" +
                    "        .container {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            padding: 20px;\n" +
                    "            background-color: #f9f9f9;\n" +
                    "            color: #333;\n" +
                    "            border: 1px solid #ddd;\n" +
                    "            border-radius: 5px;\n" +
                    "            max-width: 600px;\n" +
                    "            margin: auto;\n" +
                    "        }\n" +
                    "        h2 {\n" +
                    "            color: #f44336;\n" +
                    "        }\n" +
                    "        .info {\n" +
                    "            font-size: 14px;\n" +
                    "            line-height: 1.5;\n" +
                    "        }\n" +
                    "        .highlight {\n" +
                    "            font-weight: bold;\n" +
                    "            color: #f44336;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <h2>Security Alert</h2>\n" +
                    "        <p>Hello {{username}},</p>\n" +
                    "        <p class=\"info\">\n" +
                    "            A new account has been created using your email address.\n" +
                    "            If you did this, no action is required.\n" +
                    "        </p>\n" +
                    "        <p class=\"info\">\n" +
                    "            <strong>Account Name:</strong> {{email}}\n" +
                    "            <br>\n" +
                    "            <strong>Creation Time:</strong> {{date}}\n" +
                    "        </p>\n" +
                    "        <p class=\"info\">\n" +
                    "            If you did not create this account, please change your password immediately and contact our support team for further assistance.\n" +
                    "        </p>\n" +
                    "        <p>Best regards,<br>The Security Team</p>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";
            // Remplacer les espaces réservés par des valeurs réelles
            // Get current date
            Date currentDate = new Date();

            // Format the date as a string
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(currentDate);

            emailTemplate = emailTemplate.replace("{{date}}", currentDate.toString());
            emailTemplate = emailTemplate.replace("{{email}}", user.getEmail());
            emailTemplate = emailTemplate.replace("{{username}}", user.getLastName() + " " + user.getFirstName());

            // Définir le contenu de l'e-mail en HTML
            helper.setText(emailTemplate, true);

            // Envoyer l'email
            emailSender.send(message);
        } catch (MessagingException e) {
            // Handle messaging exceptions
            throw new RuntimeException(e);
        }
    }
}
