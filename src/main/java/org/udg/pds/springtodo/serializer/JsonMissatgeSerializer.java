package org.udg.pds.springtodo.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.udg.pds.springtodo.entity.Missatge;

import java.io.IOException;

public class JsonMissatgeSerializer extends JsonSerializer<Missatge> {

    @Override
    public void serialize(Missatge missatge, JsonGenerator gen, SerializerProvider provider)
        throws IOException {

        gen.writeStartObject();

        gen.writeNumberField("id", missatge.getId());
        gen.writeStringField("text", missatge.getMissatge());
        gen.writeObjectField("dataEnviament", missatge.getDataEnviament());
        if (missatge.getPropietari() != null) {
            gen.writeBooleanField("propietari", missatge.getPropietari());
        }

        // Podríem incloure informació de la sol·licitud associada aquí (només l'ID de la sol·licitud per evitar referències circulars, etc.)
        if (missatge.getSolicitud() != null) {
            gen.writeObjectFieldStart("solicitud");
            gen.writeNumberField("id", missatge.getSolicitud().getId());
            gen.writeEndObject();
        }

        gen.writeEndObject();
    }
}
