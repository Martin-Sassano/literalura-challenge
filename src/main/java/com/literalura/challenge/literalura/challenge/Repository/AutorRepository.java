package com.literalura.challenge.literalura.challenge.Repository;

import com.literalura.challenge.literalura.challenge.Model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombre(String nombre);

    //List<Autor> findByAnioNacimientoLessThanEqualAndAnioMuerteGreaterThanEqualOrAnioMuerteIsNull(Integer anio, Integer anio2);

    List<Autor> findByAnioNacimientoLessThanEqualAndAnioMuerteGreaterThanEqual(Integer nacimientoAntesDe, Integer muerteDespuesDe);

    List<Autor> findByAnioNacimientoLessThanEqualAndAnioMuerteIsNull(Integer nacimientoAntesDe);

    Optional<Autor> findByNombreIgnoreCase(String nombre);

    List<Autor> findByAnioNacimientoBetween(Integer desde, Integer hasta);
    List<Autor> findByAnioMuerteBefore(Integer anio);
}