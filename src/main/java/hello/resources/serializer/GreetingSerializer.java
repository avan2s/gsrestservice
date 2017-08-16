package hello.resources.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import hello.Greeting;

import java.io.IOException;

public class GreetingSerializer extends StdSerializer<Greeting> {

    public GreetingSerializer() {
        this(null);
    }

    protected GreetingSerializer(Class<Greeting> t) {
        super(t);
    }

    @Override
    public void serialize(Greeting value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("greetingMessage", value.getContent());
        gen.writeEndObject();
    }
}
