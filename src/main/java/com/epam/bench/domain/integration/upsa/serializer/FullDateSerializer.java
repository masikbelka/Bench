package com.epam.bench.domain.integration.upsa.serializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Created by Tetiana_Antonenko1
 */
public class FullDateSerializer extends JsonSerializer<Date> {
    public FullDateSerializer() {
    }

    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = formatter.format(date);
        jsonGenerator.writeString(formattedDate);
    }

    public Class<Date> handledType() {
        return Date.class;
    }
}
