package com.literalura.challenge.literalura.challenge.Dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO {

    @JsonAlias("title")
    private String titulo;

    @JsonAlias("authors")
    private List<AuthorDTO> autores;

    @JsonAlias("languages")
    private List<String> idiomas;

    @JsonAlias("download_count")
    private Integer descargas;

    // Getters y Setters

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<AuthorDTO> getAutores() {
        return autores;
    }

    public void setAutores(List<AuthorDTO> autores) {
        this.autores = autores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return "Título: " + titulo +
                "\nAutor(es): " + autores +
                "\nIdioma: " + (idiomas != null && !idiomas.isEmpty() ? idiomas.get(0) : "Desconocido") +
                "\nDescargas: " + descargas;
    }
}