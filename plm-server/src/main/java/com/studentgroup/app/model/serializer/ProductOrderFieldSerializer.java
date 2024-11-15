package com.studentgroup.app.model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.studentgroup.app.model.ProductOrder;

public class ProductOrderFieldSerializer extends JsonSerializer<ProductOrder> {

    @Override
    public void serialize(ProductOrder productOrder, JsonGenerator gen, SerializerProvider serializer) throws IOException {
        gen.writeString(productOrder.getBLNumber());
    }
    
}
