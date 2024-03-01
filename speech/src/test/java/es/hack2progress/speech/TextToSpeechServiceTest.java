package es.hack2progress.speech;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest
public class TextToSpeechServiceTest {

    @Autowired
    private TextToSpeechService textToSpeechService;

    @Container
    public GenericContainer opentts = new GenericContainer("synesthesiam/opentts:nl")
        .withExposedPorts(5500);

    @Test
    public void testTts() throws Exception {
        String texto = "Hoe laat is het";

        byte[] ficheroWavBytes = textToSpeechService.textToSpeech(texto);
        assertNotNull(ficheroWavBytes, "Recibo el fichero wav");
        assertTrue(ficheroWavBytes.length > 0, "Su longitud es mayor que 0");  

        // String url = "http://" + opentts.getHost() + ":" + opentts.getMappedPort(5500) + "/api/tts?voice=larynx:nl";
        // String json = "{\"text\":\"" + texto + "\"}";
        // String ficheroWav = "/tmp/ficherowav.wav";

        // RestTemplate restTemplate = new RestTemplate();
        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity<String> entity = new HttpEntity<>(json, headers);
        // ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, entity, byte[].class);

        // assertEquals(200, response.getStatusCodeValue());
        // assertTrue(response.getBody().length > 0);

        // Files.write(Paths.get(ficheroWav), response.getBody());

        // assertTrue(Files.exists(Paths.get(ficheroWav)));
        // assertTrue(Files.size(Paths.get(ficheroWav)) > 0);
    }
}