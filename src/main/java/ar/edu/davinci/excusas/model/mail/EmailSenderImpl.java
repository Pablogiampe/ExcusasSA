package ar.edu.davinci.excusas.model.mail;

import org.springframework.stereotype.Component;

@Component
public class EmailSenderImpl implements EmailSender {

    @Override
    public void enviarEmail(String destinatario, String remitente, String asunto, String cuerpo) {
        // Simulación de envío de email
        System.out.println("=== EMAIL ENVIADO ===");
        System.out.println("De: " + remitente);
        System.out.println("Para: " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println("Cuerpo: " + cuerpo);
        System.out.println("====================");
    }
}
