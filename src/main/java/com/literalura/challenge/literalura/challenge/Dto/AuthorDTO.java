package com.literalura.challenge.literalura.challenge.Dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorDTO {

    @JsonAlias("name")
    private String nombre;

    @JsonAlias("birth_year")
    private Integer anioNacimiento;

    @JsonAlias("death_year")
    private Integer anioMuerte;

    // Getters
    public String getNombre() {
        return nombre;
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public Integer getAnioMuerte() {
        return anioMuerte;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public void setAnioMuerte(Integer anioMuerte) {
        this.anioMuerte = anioMuerte;
    }

    @Override
    public String toString() {
        return nombre + " (" + anioNacimiento + " - " + anioMuerte + ")";
    }
}

