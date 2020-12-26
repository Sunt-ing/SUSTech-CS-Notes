package observer3;

public interface subject {
    public void registerObserver(Ball o);
    public void notifyObservers(char ch);
}
