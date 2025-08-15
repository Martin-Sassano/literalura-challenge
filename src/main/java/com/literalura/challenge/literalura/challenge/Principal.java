package com.literalura.challenge.literalura.challenge;

import com.literalura.challenge.literalura.challenge.Service.LibroService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;



@Component
public class Principal implements CommandLineRunner {

    private final LibroService libroService;

    public Principal(LibroService libroService) {
        this.libroService = libroService;
    }

    @Override
    public void run(String... args) {
        mostrarMenu();
    }

    private void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 12) {
            System.out.println("\n📚 BIENVENIDO A LITERALURA 📚");
            System.out.println("Seleccione una opción:");
            System.out.println("1 - Buscar libro por título (insertar)");
            System.out.println("2 - Mostrar todos los libros guardados");
            System.out.println("3 - Mostrar autores guardados");
            System.out.println("4 - Buscar libros por autor");
            System.out.println("5 - Buscar libros por idioma");
            System.out.println("6 - Listar autores vivos en un año");
            System.out.println("7 - Ver cantidad de libros por idioma");
            System.out.println("8 - Ver estadísticas de descargas");
            System.out.println("9 - Ver Top 10 libros más descargados");
            System.out.println("10 - Buscar autor por nombre");
            System.out.println("11 - Listar autores por rango de nacimiento");
            System.out.println("12 - Salir");
            System.out.print("Opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());

                switch (opcion) {
                    case 1 -> buscarLibro(scanner);
                    case 2 -> libroService.mostrarLibros();
                    case 3 -> libroService.mostrarAutores();
                    case 4 -> buscarPorAutor(scanner);
                    case 5 -> buscarPorIdioma(scanner);
                    case 6 -> autoresVivosEnAnio(scanner);
                    case 7 -> mostrarEstadisticasPorIdioma(scanner);
                    case 8 -> libroService.mostrarEstadisticasDeDescargas();
                    case 9 -> libroService.mostrarTop10Libros();
                    case 10 -> buscarAutorPorNombre(scanner);
                    case 11 -> listarAutoresPorNacimiento(scanner);
                    case 12 -> System.out.println("¡Gracias por usar LiterAlura! Hasta la próxima 📖");
                    default -> System.out.println("⚠️ Opción inválida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Entrada inválida. Ingrese un número del 1 al 12.");
            } catch (Exception e) {
                System.out.println("❌ Ocurrió un error: " + e.getMessage());
            }
        }
        
    }

    private void buscarLibro(Scanner scanner) {
        System.out.print("Ingrese el título del libro a buscar: ");
        String titulo = scanner.nextLine().trim();

        if (entradaVacia(titulo, "título")) return;

        libroService.buscarYGuardarLibro(titulo);
    }

    private void buscarPorAutor(Scanner scanner) {
        System.out.print("Ingrese el nombre del autor: ");
        String autor = scanner.nextLine().trim();

        if (entradaVacia(autor, "autor")) return;

        libroService.buscarLibrosPorAutor(autor);
    }

    private void buscarPorIdioma(Scanner scanner) {
        System.out.print("Ingrese el idioma (código ISO, ej: 'en', 'es', 'fr'): ");
        String idioma = scanner.nextLine().trim();

        if (entradaVacia(idioma, "idioma")) return;

        libroService.mostrarLibrosPorIdioma(idioma);
    }

    private void autoresVivosEnAnio(Scanner scanner) {
        System.out.print("Ingrese el año para buscar autores vivos: ");
        try {
            int anio = Integer.parseInt(scanner.nextLine().trim());
            libroService.mostrarAutoresVivosEnAnio(anio);
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Ingrese un número válido para el año.");
        }
    }

    private void mostrarEstadisticasPorIdioma(Scanner scanner) {
        System.out.println("Seleccione un idioma:");
        System.out.println("1 - Español (es)");
        System.out.println("2 - Inglés (en)");
        System.out.print("Opción: ");
        String opcion = scanner.nextLine().trim();

        String idioma = switch (opcion) {
            case "1" -> "es";
            case "2" -> "en";
            default -> {
                System.out.println("❌ Opción inválida.");
                yield null;
            }
        };

        if (idioma != null) {
            libroService.mostrarCantidadLibrosPorIdioma(idioma);
        }
    }

    private void buscarAutorPorNombre(Scanner scanner) {
        System.out.print("Ingrese el nombre del autor: ");
        String nombreAutor = scanner.nextLine().trim();
        if (entradaVacia(nombreAutor, "nombre del autor")) return;
        libroService.buscarAutorPorNombre(nombreAutor);
    }

    private void listarAutoresPorNacimiento(Scanner scanner) {
        try {
            System.out.print("Desde (año): ");
            int desde = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Hasta (año): ");
            int hasta = Integer.parseInt(scanner.nextLine().trim());
            libroService.listarAutoresPorNacimiento(desde, hasta);
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Ingrese años válidos.");
        }
    }

    private boolean entradaVacia(String entrada, String campo) {
        if (entrada.isBlank()) {
            System.out.println("⚠️ El " + campo + " no puede estar vacío.");
            return true;
        }
        return false;
    }
}

//@Component
//public class Principal implements CommandLineRunner {
//
//    private final LibroService libroService;
//
//    public Principal(LibroService libroService) {
//        this.libroService = libroService;
//    }
//
//    @Override
//    public void run(String... args) {
//        mostrarMenu();
//    }
//
//    private void mostrarMenu() {
//        Scanner scanner = new Scanner(System.in);
//        int opcion = -1;
//
//        while (opcion != 12) {
//            System.out.println("\n📚 BIENVENIDO A LITERALURA 📚");
//            System.out.println("Seleccione una opción:");
//            System.out.println("1 - Buscar libro por título (insertar)");
//            System.out.println("2 - Mostrar todos los libros guardados");
//            System.out.println("3 - Mostrar autores guardados");
//            System.out.println("4 - Buscar libros por autor");
//            System.out.println("5 - Buscar libros por idioma");
//            System.out.println("6 - Listar autores vivos en un año");
//            System.out.println("7 - Ver cantidad de libros por idioma");
//            System.out.println("8 - Ver estadísticas de descargas");
//            System.out.println("9 - Ver Top 10 libros más descargados");
//            System.out.println("10 - Buscar autor por nombre");
//            System.out.println("11 - Listar autores por rango de nacimiento");
//            System.out.println("12 - Salir");
//            System.out.print("Opción: ");
//
//            try {
//                opcion = Integer.parseInt(scanner.nextLine().trim());
//
//                switch (opcion) {
//                    case 1 -> buscarLibro(scanner);
//                    case 2 -> libroService.mostrarLibros();
//                    case 3 -> libroService.mostrarAutores();
//                    case 4 -> buscarPorAutor(scanner);
//                    case 5 -> buscarPorIdioma(scanner);
//                    case 6 -> autoresVivosEnAnio(scanner);
//                    case 7 -> mostrarEstadisticasPorIdioma(scanner);
//                    case 8 -> libroService.mostrarEstadisticasDeDescargas();
//                    case 9 -> libroService.mostrarTop10Libros();
//                    case 10 -> {
//                        System.out.print("Ingrese el nombre del autor: ");
//                        String nombreAutor = scanner.nextLine().trim();
//                        libroService.buscarAutorPorNombre(nombreAutor);
//                    }
//                    case 11 -> {
//                        System.out.print("Desde (año): ");
//                        int desde = Integer.parseInt(scanner.nextLine());
//                        System.out.print("Hasta (año): ");
//                        int hasta = Integer.parseInt(scanner.nextLine());
//                        libroService.listarAutoresPorNacimiento(desde, hasta);
//                    }
//                    case 12 -> System.out.println("¡Gracias por usar LiterAlura! Hasta la próxima 📖");
//                    default -> System.out.println("⚠️ Opción inválida. Intente nuevamente.");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("⚠️ Entrada inválida. Ingrese un número del 1 al 12.");
//            } catch (Exception e) {
//                System.out.println("❌ Ocurrió un error: " + e.getMessage());
//            }
//        }
//
//        scanner.close();
//    }
//
//    private void buscarLibro(Scanner scanner) {
//        System.out.print("Ingrese el título del libro a buscar: ");
//        String titulo = scanner.nextLine().trim();
//
//        if (titulo.isEmpty()) {
//            System.out.println("⚠️ El título no puede estar vacío.");
//            return;
//        }
//
//        libroService.buscarYGuardarLibro(titulo);
//    }
//
//    private void buscarPorAutor(Scanner scanner) {
//        System.out.print("Ingrese el nombre del autor: ");
//        String autor = scanner.nextLine().trim();
//
//        if (autor.isEmpty()) {
//            System.out.println("⚠️ El nombre del autor no puede estar vacío.");
//            return;
//        }
//
//        libroService.buscarLibrosPorAutor(autor);
//    }
//    private void buscarPorIdioma(Scanner scanner) {
//        System.out.print("Ingrese el idioma (código ISO, ej: 'en', 'es', 'fr'): ");
//        String idioma = scanner.nextLine().trim();
//        if (idioma.isEmpty()) {
//            System.out.println("⚠️ El idioma no puede estar vacío.");
//            return;
//        }
//        libroService.mostrarLibrosPorIdioma(idioma);
//    }
//
//    private void autoresVivosEnAnio(Scanner scanner) {
//        System.out.print("Ingrese el año para buscar autores vivos: ");
//        try {
//            int anio = Integer.parseInt(scanner.nextLine().trim());
//            libroService.mostrarAutoresVivosEnAnio(anio);
//        } catch (NumberFormatException e) {
//            System.out.println("⚠️ Ingrese un número válido para el año.");
//        }
//    }
//    private void mostrarEstadisticasPorIdioma(Scanner scanner) {
//        System.out.println("Seleccione un idioma:");
//        System.out.println("1 - Español (es)");
//        System.out.println("2 - Inglés (en)");
//        System.out.print("Opción: ");
//        String opcion = scanner.nextLine().trim();
//
//        String idioma = switch (opcion) {
//            case "1" -> "es";
//            case "2" -> "en";
//            default -> {
//                System.out.println("❌ Opción inválida.");
//                yield null;
//            }
//        };
//
//        if (idioma != null) {
//            libroService.mostrarCantidadLibrosPorIdioma(idioma);
//        }
//    }
//}
