package org.udg.pds.springtodo.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.udg.pds.springtodo.entity.Solicitud;

import java.io.IOException;

public class JsonSolicitudSerializer extends JsonSerializer<Solicitud> {

    @Override
    public void serialize(Solicitud solicitud, JsonGenerator gen, SerializerProvider provider)
        throws IOException {

        gen.writeStartObject();

        gen.writeNumberField("id", solicitud.getId());
        gen.writeStringField("estat", solicitud.getEstat());

        if (solicitud.getPagament() != null) {
            gen.writeObjectFieldStart("pagament");
            gen.writeNumberField("id", solicitud.getPagament().getId());
            gen.writeNumberField("amount", solicitud.getPagament().getAmount());
            gen.writeStringField("payment_method", solicitud.getPagament().getPaymentMethod());
            gen.writeStringField("status", solicitud.getPagament().getStatus().name());
            gen.writeEndObject();
        }

        if (solicitud.getUsuari() != null) {
            gen.writeObjectFieldStart("user");
            gen.writeNumberField("id", solicitud.getUsuari().getId());
            gen.writeStringField("name", solicitud.getUsuari().getName());
            gen.writeEndObject();
        }

        gen.writeEndObject();
    }
}
