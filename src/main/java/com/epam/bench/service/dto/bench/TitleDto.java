package com.epam.bench.service.dto.bench;

/**
 * Created by Tetiana_Antonenko1
 */
public class TitleDto {
    private String shortTitle;
    private String fullTitle;

    public TitleDto() {
    }

    public TitleDto(String shortTitle, String fullTitle) {
        this.shortTitle = shortTitle;
        this.fullTitle = fullTitle;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    @Override
    public String toString() {
        return shortTitle;
    }
}
