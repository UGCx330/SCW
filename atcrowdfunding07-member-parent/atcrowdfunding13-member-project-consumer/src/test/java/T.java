import org.junit.Test;

public class T {
    private int a = 1;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    @Test
    public void t1() {
        T t = new T();
        int a1 = t.getA();
        a1 = 2;
        int a2 = t.getA();
        System.out.println(a2);
    }
}
