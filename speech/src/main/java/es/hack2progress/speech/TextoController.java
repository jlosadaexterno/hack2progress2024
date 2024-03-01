package es.hack2progress.speech;    

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.ai.ollama.api.OllamaApi.Message.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.Decoder.Text;

@RestController
public class TextoController {

    private static Logger logger = LoggerFactory.getLogger(TextoController.class);

    @Autowired
    private TraductorService traductorService;

    @Autowired
    private TextToSpeechService textToSpeechService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private TextoRepository textoRepository;

    @GetMapping("/text-to-speech/{id}")
    public void textToSpeech(@PathVariable("id") long id, HttpServletResponse response) {

        Texto texto = textoRepository.findById(id).get();
        if (texto == null || !texto.isTranslated()) {
            response.setStatus(404);
            return;
        }

        String traduccion = texto.getDutch();
        byte[] ficheroWavBytes = textToSpeechService.textToSpeech(traduccion);
        /* Establece el body de la respuesta, el timpo mime y como nombre de fichero fichero.wav */
        response.setHeader("Content-Disposition", "attachment; filename="+ id + ".wav");
        response.setHeader("Content-Type", "audio/wav");
        response.setHeader("Content-Length", String.valueOf(ficheroWavBytes.length));
        try {
            response.getOutputStream().write(ficheroWavBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/pull-model")
    public void pullModel() {
        this.traductorService.pullImage();
    }

    /* Expone un método post que recibe una frase en inglés, comprueba si ya existe
     * en caso de que no exista, la añade al repositorio.
     */
    @PostMapping("/add-text")
    public void addText(@RequestBody String english) {
        /* Reemplaza los '+' por ' ' */
        english = english.replace("+", " ");
        Texto texto = textoRepository.findByEnglish(english);
        logger.info(String.format("Texto: %s %b", english , texto!=null));
        if (texto == null) {
            textoRepository.save(new Texto(english, false, ""));
        }
        kafkaTemplate.send("myTopic", english);
        logger.info(String.format("Texto: %s publicado", english)); 
    }

    /* Lista los Texto */
    @GetMapping("/list-text")
    public List<Texto> listText() {
        return textoRepository.findAll();
    }

    @KafkaListener(topics = "myTopic", groupId = "myGroup")
    public void listen(String message) {
        logger.info("Received message: " + message);

        Texto texto = textoRepository.findByEnglish(message);   

        /* En desarrollo estoy borrando la BBDD */
        if (texto == null || texto.isTranslated()) {
            return;
        }

        String dutch = this.traductorService.translate(message);

        texto.setDutch(dutch);
        texto.setTranslated(true);
        textoRepository.save(texto);

        logger.info("Texto: " + message + " traducido a " + dutch);
    }
}