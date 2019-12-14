package observer1;
import java.util.*;
public interface subject {
    public void registerObserver(Object o);
    public void notifyObservers(List al);
}
