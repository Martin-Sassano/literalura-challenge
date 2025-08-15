package com.literalura.challenge.literalura.challenge.Repository;

import com.literalura.challenge.literalura.challenge.Model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByIdiomaIgnoreCase(String idioma);
    List<Libro> findByAutorNombreContainingIgnoreCase(String autor);
    boolean existsByTituloIgnoreCase(String titulo);
    Optional<Libro> findFirstByTituloIgnoreCase(String titulo);
}
