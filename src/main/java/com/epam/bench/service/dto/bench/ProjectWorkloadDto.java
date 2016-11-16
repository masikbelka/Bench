package com.epam.bench.service.dto.bench;

/**
 * Created by Tetiana_Antonenko1
 */
public class ProjectWorkloadDto {
    private String id;
    private String name;
    private String type;
    private String current;
    private int workload;

    public ProjectWorkloadDto() {
    }

    public ProjectWorkloadDto(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.current = "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public int getWorkload() {
        return workload;
    }

    public void setWorkload(int workload) {
        this.workload = workload;
    }
}
