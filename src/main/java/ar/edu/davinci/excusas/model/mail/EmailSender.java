package ar.edu.davinci.excusas.model.mail;

public interface EmailSender {
    void enviarEmail(String destinatario, String remitente, String asunto, String cuerpo);
}
