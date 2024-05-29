import example.SomeInterface;
import example.SomeOtherInterface;
import injectors.AutoInjectable;
import lombok.Getter;

@Getter
public class Bean {
    @AutoInjectable
    private SomeInterface some;
    @AutoInjectable
    private SomeOtherInterface other;

    public void doManyThings(){
        some.doSomething();
        other.doSomeOther();
    }
}