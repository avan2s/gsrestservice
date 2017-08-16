package hello;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hello.resource.deserializer.GreetingDeserializer;
import hello.resources.serializer.GreetingSerializer;

@JsonSerialize(using = GreetingSerializer.class)
@JsonDeserialize(using = GreetingDeserializer.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Greeting {

    private long id;
    private String content;

    private Greeting parent;

    public Greeting() {
        this.parent = this;
    }

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
        this.parent = this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Greeting getParent() {
        return parent;
    }

    public void setParent(Greeting parent) {
        this.parent = parent;
    }
}
