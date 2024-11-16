package com.studentgroup.app.model.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.studentgroup.app.model.EmployeeUser;

public class EmployeeUserFieldSerializer extends JsonSerializer<EmployeeUser> {

    @Override
    public void serialize(EmployeeUser user, JsonGenerator gen, SerializerProvider serializer) throws IOException {
        gen.writeString(user.getUsername());
    }
    
}