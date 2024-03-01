package es.hack2progress.speech;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TextToSpeechService {
    public byte[] textToSpeech(String traduccion) {
         /* 
         * Invoca a synesthesiam/opentts en el puerto 5500 pasándole el texto: traduccion
         * recibiendo de vuelta el fichero wav
         */
        
        String url = "http://localhost:5500/api/tts?voice=larynx:nl";
        String json = "{\"text\":\"" + traduccion + "\"}";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

//      Obtiene un array de bytes con el fichero wav
        byte[] ficheroWavBytes = restTemplate.postForObject(url, json, byte[].class);
        return ficheroWavBytes;
    }
}