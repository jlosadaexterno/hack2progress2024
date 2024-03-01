package es.hack2progress.speech;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

@SpringBootTest
public class TraductorServiceTest {

    @Autowired
    private TraductorService traductorService;

    @Test
    public void testTranslate() {
        String english = "How are you?";
        String expectedTranslation = "Wie gaat het met je?";

        String translation = traductorService.translate(english);

        assertEquals(expectedTranslation, translation, "La traducción no es correcta");
    }
}