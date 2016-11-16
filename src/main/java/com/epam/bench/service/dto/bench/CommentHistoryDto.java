package com.epam.bench.service.dto.bench;

/**
 * Created by Tetiana_Antonenko1.
 */
public class CommentHistoryDto {
    private String user;
    private String when;
    private String value;

    public CommentHistoryDto() {
    }

    public CommentHistoryDto(String user, String when, String value) {
        this.user = user;
        this.when = when;
        this.value = value;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
