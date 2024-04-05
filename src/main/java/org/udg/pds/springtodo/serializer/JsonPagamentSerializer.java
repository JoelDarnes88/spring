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
        gen.writeNumberField("preuFinal", pagament.getPreuFinal());
        gen.writeNumberField("metode", pagament.getMetode());

        // Si vols incloure informació addicional o relacionada, pots fer-ho aquí.
        if (pagament.getSolicitud() != null) {
            gen.writeObjectFieldStart("solicitud");
            gen.writeNumberField("id", pagament.getSolicitud().getId());
            gen.writeEndObject();
        }

        gen.writeEndObject();
    }
}
