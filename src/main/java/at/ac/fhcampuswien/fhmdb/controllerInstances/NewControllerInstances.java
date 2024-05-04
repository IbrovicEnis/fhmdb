package at.ac.fhcampuswien.fhmdb.controllerInstances;


import javafx.util.Callback;

public class NewControllerInstances implements Callback<Class<?>, Object> {
    private static NewControllerInstances instance;
    private NewControllerInstances() {

    }
    public static NewControllerInstances getInstance() {
        if (instance == null) {
            instance = new NewControllerInstances();
        }
        return instance;
    }
    @Override
    public Object call(Class<?> aClass) {
        try{
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}


