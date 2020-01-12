package observer2;

import java.util.List;

public interface subject {
    public void registerObserver(Ball o);
    public void notifyObservers(char ch);
}
