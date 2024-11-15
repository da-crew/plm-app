package com.studentgroup.app.model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.studentgroup.app.model.Truck;

public class TruckFieldSerializer extends JsonSerializer<Truck> {

    @Override
    public void serialize(Truck truck, JsonGenerator gen, SerializerProvider serializer) throws IOException {
        gen.writeString(truck.getTruckNumber());
    }
    
}
