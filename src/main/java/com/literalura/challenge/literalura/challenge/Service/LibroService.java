package com.literalura.challenge.literalura.challenge.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.literalura.challenge.literalura.challenge.Dto.AuthorDTO;
import com.literalura.challenge.literalura.challenge.Dto.BookDTO;
import com.literalura.challenge.literalura.challenge.Dto.BookResponse;
import com.literalura.challenge.literalura.challenge.Model.Autor;
import com.literalura.challenge.literalura.challenge.Model.Libro;
import com.literalura.challenge.literalura.challenge.Repository.AutorRepository;
import com.literalura.challenge.literalura.challenge.Repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class LibroService {

    private final LibroRepository libroRepo;
    private final AutorRepository autorRepo;

    public LibroService(LibroRepository libroRepo, AutorRepository autorRepo) {
        this.libroRepo = libroRepo;
        this.autorRepo = autorRepo;
    }

    public void buscarYGuardarLibro(String tituloBuscado) {
        try {
            String encoded = tituloBuscado.replace(" ", "%20");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://gutendex.com/books/?search=" + encoded))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            BookResponse bookResponse = mapper.readValue(response.body(), BookResponse.class);

            if (bookResponse.getResults().isEmpty()) {
                System.out.println("No se encontraron libros con ese título.");
                return;
            }

            BookDTO libroDto = bookResponse.getResults().get(0);

            String titulo = libroDto.getTitulo();
            String idioma = libroDto.getIdiomas() != null && !libroDto.getIdiomas().isEmpty()
                    ? libroDto.getIdiomas().get(0)
                    : "desconocido";
            int descargas = libroDto.getDescargas() != null ? libroDto.getDescargas() : 0;

            AuthorDTO autorDto = libroDto.getAutores().isEmpty() ? null : libroDto.getAutores().get(0);
            if (autorDto == null) {
                System.out.println("El libro no tiene autores registrados.");
                return;
            }

            Autor autor;
            Optional<Autor> autorOptional = autorRepo.findByNombre(autorDto.getNombre());

            if (autorOptional.isPresent()) {
                autor = autorOptional.get(); // Este ya está managed
            } else {
                Autor nuevoAutor = new Autor(
                        autorDto.getNombre(),
                        autorDto.getAnioNacimiento(),
                        autorDto.getAnioMuerte()
                );
                autor = autorRepo.save(nuevoAutor); // IMPORTANTE: Usamos el que devuelve save()
            }

            // Verifica si ya existe el libro
            Optional<Libro> libroExistente = libroRepo.findFirstByTituloIgnoreCase(titulo);
            if (libroExistente.isPresent()) {
                System.out.println("⚠️ El libro ya está en la base de datos.");
                return;
            }

            // Crear y guardar libro
            Libro libro = new Libro(titulo, idioma, descargas, autor);
            libroRepo.save(libro);

            System.out.println("✅ Libro guardado exitosamente:\n" + libro);

        } catch (Exception e) {
            System.out.println("❌ Error al consultar o guardar el libro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void mostrarLibros() {
        libroRepo.findAll().forEach(libro ->
                System.out.println(libro.getTitulo() + " - " + libro.getAutor().getNombre()));
    }

    public void mostrarAutores() {
        autorRepo.findAll().forEach(autor ->
                System.out.println(autor.getNombre() + " (" + autor.getAnioNacimiento() + " - " + autor.getAnioMuerte() + ")"));
    }

    public void buscarLibrosPorAutor(String nombreAutor) {
        var libros = libroRepo.findByAutorNombreContainingIgnoreCase(nombreAutor);
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros de ese autor.");
        } else {
            libros.forEach(libro ->
                    System.out.println(libro.getTitulo() + " - " + libro.getAutor().getNombre()));
        }
    }

    public void mostrarLibrosPorIdioma(String idioma) {
        var libros = libroRepo.findByIdiomaIgnoreCase(idioma);
        if (libros.isEmpty()) {
            System.out.println("⚠️ No se encontraron libros en el idioma: " + idioma);
        } else {
            System.out.println("📘 Libros encontrados en idioma: " + idioma);
            libros.forEach(libro -> System.out.println("\n" + libro));
        }
    }

    public void mostrarAutoresVivosEnAnio(int anio) {
        if (anio <= 0 || anio > java.time.Year.now().getValue()) {
            System.out.println("⚠️ Año inválido. Introduce un año válido (ej. 1900 - 2025).");
            return;
        }

        // Autores fallecidos después del año o sin fallecimiento
        List<Autor> vivosConMuerteRegistrada = autorRepo
                .findByAnioNacimientoLessThanEqualAndAnioMuerteGreaterThanEqual(anio, anio);

        List<Autor> vivosSinMuerteRegistrada = autorRepo
                .findByAnioNacimientoLessThanEqualAndAnioMuerteIsNull(anio);

        List<Autor> autoresVivos = Stream.concat(
                vivosConMuerteRegistrada.stream(),
                vivosSinMuerteRegistrada.stream()
        ).toList();

        if (autoresVivos.isEmpty()) {
            System.out.println("😔 No se encontraron autores vivos en el año " + anio);
        } else {
            System.out.println("📜 Autores vivos en el año " + anio + ":");
            autoresVivos.forEach(autor -> System.out.println(" - " + autor));
        }
    }

    public void mostrarCantidadLibrosPorIdioma(String idioma) {
        List<Libro> librosPorIdioma = libroRepo.findByIdiomaIgnoreCase(idioma);

        System.out.println("📚 Hay " + librosPorIdioma.size() + " libro(s) en idioma '" + idioma + "' en la base de datos.");
    }

//    public void contarLibrosPorIdiomaConStreams(String idioma) {
//        long cantidad = libroRepo.findAll().stream()
//                .filter(libro -> libro.getIdioma().equalsIgnoreCase(idioma))
//                .count();
//
//        System.out.println("📚 (Stream) Hay " + cantidad + " libro(s) en idioma '" + idioma + "'");
//    }

    public void mostrarEstadisticasDeDescargas() {
        var libros = libroRepo.findAll();

        if (libros.isEmpty()) {
            System.out.println("⚠️ No hay libros en la base de datos.");
            return;
        }

        DoubleSummaryStatistics stats = libros.stream()
                .mapToDouble(Libro::getDescargas)
                .summaryStatistics();

        System.out.println("📈 Estadísticas de descargas:");
        System.out.println("🔢 Total de libros: " + stats.getCount());
        System.out.println("📉 Mínimo: " + (int) stats.getMin());
        System.out.println("📈 Máximo: " + (int) stats.getMax());
        System.out.println("📊 Promedio: " + (int) stats.getAverage());
    }

    public void mostrarTop10Libros() {
        var libros = libroRepo.findAll();

        var top10 = libros.stream()
                .sorted(Comparator.comparing(Libro::getDescargas).reversed())
                .limit(10)
                .toList();

        System.out.println("🏆 Top 10 libros más descargados:");
        top10.forEach(libro -> System.out.println(" - " + libro.getTitulo() + " (" + libro.getDescargas() + " descargas)"));
    }

    public void buscarAutorPorNombre(String nombre) {
        var autorOpt = autorRepo.findByNombreIgnoreCase(nombre);

        if (autorOpt.isPresent()) {
            var autor = autorOpt.get();
            System.out.println("👤 Autor encontrado:");
            System.out.println(" - " + autor);
            System.out.println("📚 Libros:");
            autor.getLibros().forEach(libro -> System.out.println("   • " + libro.getTitulo()));
        } else {
            System.out.println("❌ Autor no encontrado en la base de datos.");
        }
    }

    public void listarAutoresPorNacimiento(int desde, int hasta) {
        var autores = autorRepo.findByAnioNacimientoBetween(desde, hasta);

        if (autores.isEmpty()) {
            System.out.println("❌ No se encontraron autores nacidos entre " + desde + " y " + hasta);
        } else {
            System.out.println("📜 Autores nacidos entre " + desde + " y " + hasta + ":");
            autores.forEach(a -> System.out.println(" - " + a));
        }
    }
}
