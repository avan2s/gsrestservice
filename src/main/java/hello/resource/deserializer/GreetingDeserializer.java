package hello.resource.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import hello.Greeting;

import java.io.IOException;

public class GreetingDeserializer extends StdDeserializer<Greeting> {


    public GreetingDeserializer() {
        this(null);
    }

    protected GreetingDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Greeting deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        int id = (Integer) ((IntNode) node.get("id")).numberValue();
        String itemName = node.get("content").asText();

        return new Greeting(id, itemName);
    }
}
