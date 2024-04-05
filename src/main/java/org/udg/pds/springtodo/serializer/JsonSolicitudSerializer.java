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
        // Assumim que cada Solicitud té un únic Pagament associat i que volem incloure algunes propietats bàsiques
        if (solicitud.getPagament() != null) {
            gen.writeObjectFieldStart("pagament");
            gen.writeNumberField("id", solicitud.getPagament().getId());
            gen.writeNumberField("preuFinal", solicitud.getPagament().getPreuFinal());
            gen.writeEndObject();
        }
        // Aquí podríem incloure informació sobre els missatges relacionats, usuari, etc.

        gen.writeEndObject();
    }
}
