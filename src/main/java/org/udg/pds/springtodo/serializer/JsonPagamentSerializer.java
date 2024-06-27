package org.udg.pds.springtodo.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.udg.pds.springtodo.entity.Pagament;

import java.io.IOException;

public class JsonPagamentSerializer extends JsonSerializer<Pagament> {

    @Override
    public void serialize(Pagament pagament, JsonGenerator gen, SerializerProvider provider)
        throws IOException {

        gen.writeStartObject();

        gen.writeNumberField("id", pagament.getId());
        gen.writeNumberField("amount", pagament.getAmount());
        gen.writeStringField("payment_method", pagament.getPaymentMethod());
        gen.writeStringField("status", pagament.getStatus().name());

        if (pagament.getChat() != null) {
            gen.writeObjectFieldStart("chat");
            gen.writeNumberField("id", pagament.getChat().getId());
            gen.writeEndObject();
        }

        gen.writeEndObject();
    }
}
