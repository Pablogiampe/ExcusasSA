package ar.edu.davinci.excusas.model.mail;

import ar.edu.davinci.excusas.model.excepciones.ErrorProcesamiento;

public class EmailSenderImpl implements EmailSender {

    @Override
    public void enviarEmail(String emailDestino, String emailOrigen, String asunto, String cuerpo) {

        try {
            System.out.println("=== EMAIL ENVIADO ===");
            System.out.println("De: " + emailOrigen);
            System.out.println("Para: " + emailDestino);
            System.out.println("Asunto: " + asunto);
            System.out.println("Cuerpo: " + cuerpo);
            System.out.println("====================");
        } catch (Exception e) {
            throw new ErrorProcesamiento("Error al enviar el email", e);
        }
    }

}
