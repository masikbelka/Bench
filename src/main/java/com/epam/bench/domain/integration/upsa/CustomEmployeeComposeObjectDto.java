package com.epam.bench.domain.integration.upsa;

/**
 * Created by Tetiana_Antonenko1
 */
public class CustomEmployeeComposeObjectDto extends EmployeeComposeObject{
    CustomEmployeeSimpleViewDto customEmployeeSimpleViewDto;

    public CustomEmployeeSimpleViewDto getEmployeeSimpleView() {
        return customEmployeeSimpleViewDto;
    }

    public void setEmployeeSimpleView(CustomEmployeeSimpleViewDto customEmployeeSimpleViewDto) {
        this.customEmployeeSimpleViewDto = customEmployeeSimpleViewDto;
    }
}
