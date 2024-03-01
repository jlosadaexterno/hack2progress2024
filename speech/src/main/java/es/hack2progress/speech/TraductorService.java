package es.hack2progress.speech;

import java.util.List;
import java.util.Map;

import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.ai.ollama.api.OllamaApi.Message.Role;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TraductorService {
    public void pullImage() {
        /* Primero comprobamos si invocar mediante RestTemplate a
         * curl http://localhost:11434/api/tags 
         * contiene en el json de respuesta un $.models[].name con valor gemma:2b
          */
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:11434/api/tags";
        String json = restTemplate.getForObject(url, String.class);
        if (json.contains("gemma:2b")) {
            return;
        }

        /*  Efectuar mediante RestTemplate el equivalente a 
                curl http://localhost:11434/api/pull -d '{
                    "name": "gemma:2b"
                }'
        */
        RestTemplate restTemplate2 = new RestTemplate();
        String url2 = "http://localhost:11434/api/pull";
        String json2 = "{\"name\": \"gemma:2b\"}";
        restTemplate2.postForObject(url2, json2, String.class);
    }

    public String translate(String english) {
        OllamaApi ollamaApi =
            new OllamaApi("http://localhost:11434");

        // Sync request
        ChatRequest request = ChatRequest.builder("gemma:2b")
            .withStream(false) // not streaming
            .withMessages(List.of(
                    Message.builder(Role.SYSTEM)
                        .withContent("You are professional translator. You are translating into Dutch.")
                        .build(),
                    Message.builder(Role.USER)
                        .withContent("Translate \"### " + english + " ###\" to Dutch.")
                        .build()))

            .withOptions(OllamaOptions.create().withTemperature(0.0f))
            .build();

        ChatResponse response = ollamaApi.chat(request);
        String respuesta = response.message().content();
        /* El texto contine pares de cadenas "###" con un texto intermedio. Extraigo de la respuesta una cadena embebida de la forma "### respuesta ###",
         * pero que no sea igual al valor de la variable english. Obvio el espacio en blanco que hay después del primer ### y antes del ### de cada par
         * todos los valores que no estén entre los pares
         */
        String[] partes = respuesta.split("###");
        for (int i = 1; i < partes.length; i += 2) {
            if (!partes[i].trim().equals(english)) {
                return partes[i].trim();
            }
        }

        return "NONE";
    }

}
