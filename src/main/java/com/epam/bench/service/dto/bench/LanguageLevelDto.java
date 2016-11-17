package com.epam.bench.service.dto.bench;

/**
 * Created by Tetiana_Antonenko.
 */
public class LanguageLevelDto {

    private String language;
    private String speaking;
    private String writing;

    public LanguageLevelDto() {
    }

    public LanguageLevelDto(String language, String speaking, String writing) {
        this.language = language;
        this.speaking = speaking;
        this.writing = writing;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSpeaking() {
        return speaking;
    }

    public void setSpeaking(String speaking) {
        this.speaking = speaking;
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }
}
