package com.epam.bench.service.dto.bench;

/**
 * Created by Tetiana_Antonenko
 */
public class ProposedPositionsDto {

    private String status;
    private String name;
    private String type;
    private String id;

    public ProposedPositionsDto() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
