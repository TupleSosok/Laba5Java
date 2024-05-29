import injectors.Injector;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Bean injected = new Injector().inject(new Bean());
        injected.doManyThings();
    }
}
