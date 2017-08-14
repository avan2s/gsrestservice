package hello;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class SomePrototypeComponent {
    public SomePrototypeComponent() {
    }
}
