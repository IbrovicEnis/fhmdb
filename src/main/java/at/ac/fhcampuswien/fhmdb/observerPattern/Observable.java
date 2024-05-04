package at.ac.fhcampuswien.fhmdb.observerPattern;

public interface Observable {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String messages);
}

