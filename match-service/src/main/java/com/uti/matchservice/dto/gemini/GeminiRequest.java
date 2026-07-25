package com.uti.matchservice.dto.gemini;

import java.util.List;

//La API de Gemini tiene una estructura JSON un poco anidada
//(tiene listas dentro de objetos). La mejor forma
//de mapear esto en Java es usando records anidados.

public record GeminiRequest(List<Content> contents) {

    public record Content(List<Part> parts) {}

    public record Part(String text) {}
}