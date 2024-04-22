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
            helper.setSubject("Object");

            // Charger le modèle de courrier électronique à partir d'un fichier ou d'une
            // chaîne de caractères
            String emailTemplate = "<!DOCTYPE html>\r\n" + "<html lang=\"fr\">\r\n" + "<head>\r\n"
                    + "    <meta charset=\"UTF-8\">\r\n"
                    + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                    + "    <style>\r\n" + "        body {\r\n" + "            font-family: Arial, sans-serif;\r\n"
                    + "            background-color: #f4f4f4;\r\n" + "        }\r\n" + "\r\n"
                    + "        .container {\r\n" + "            max-width: 600px;\r\n"
                    + "            margin: 0 auto;\r\n" + "            padding: 20px;\r\n"
                    + "            background-color: #ffffff;\r\n" + "            border-radius: 5px;\r\n"
                    + "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\r\n" + "        }\r\n" + "\r\n"
                    + "        h1 {\r\n" + "            color: #333;\r\n" + "        }\r\n" + "\r\n" + "        p {\r\n"
                    + "            color: #555;\r\n" + "        }\r\n" + "\r\n" + "        .btn {\r\n"
                    + "            display: inline-block;\r\n" + "            padding: 10px 20px;\r\n"
                    + "            background-color: #007BFF;\r\n" + "            color: #fff;\r\n"
                    + "            text-decoration: none;\r\n" + "            border-radius: 3px;\r\n"
                    + "            margin-top: 10px;\r\n" + "        }\r\n" + "\r\n" + "        .btn:hover {\r\n"
                    + "            background-color: #0056b3;\r\n" + "        }\r\n" + "    </style>\r\n"
                    + "</head>\r\n" + "<body>\r\n" + "    <div class=\"container\">\r\n"
                    + "        <h1>Bonjour, {{recipientName}}</h1>\r\n"
                    + "        <p>Vous avez telechrger un PDF.</p>\r\n"
                    + "        <p>Merci de nous informer en cas de problem.</p>\r\n"
                    + "        <p>Votre nom d'utilisateur : {{username}}</p>\r\n"
                    + "        <p>Votre adresse e-mail : {{email}}</p>\r\n" + "    </div>\r\n" + "</body>\r\n"
                    + "</html>\r\n";

            // Remplacer les espaces réservés par des valeurs réelles
            emailTemplate = emailTemplate.replace("{{recipientName}}", user.getLastName() + " " + user.getFirstName());
            emailTemplate = emailTemplate.replace("{{username}}", user.getLastName() + " " + user.getFirstName());
            emailTemplate = emailTemplate.replace("{{email}}", user.getEmail());

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
