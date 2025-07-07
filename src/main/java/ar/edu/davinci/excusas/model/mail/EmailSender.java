package ar.edu.davinci.excusas.model.mail;

public interface EmailSender {
    void enviarEmail(String emailDestino, String emailOrigen, String asunto, String cuerpo);
}
