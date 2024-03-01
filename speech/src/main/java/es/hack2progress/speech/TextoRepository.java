package es.hack2progress.speech;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TextoRepository extends JpaRepository<Texto, Long>{
    // Añade un método findByEnglish que devuelva un Texto
    Texto findByEnglish(String english);
}
