package util;


/**
 * Universal container I use to get object from lambda-expressions.
 */
public class Container {
    private Object obj;

    public Object get() {
        return obj;
    }

    public void set(Object obj) {
        this.obj = obj;
    }

    public Container(Object obj) {
        this.obj = obj;
    }

    public Container() {
    }
}
