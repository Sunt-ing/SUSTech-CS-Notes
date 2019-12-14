package test.java.cn.edu.sustech.testclasses;

public class B {

    private C cDep;

    public C getCDep() {
        return cDep;
    }

    public D getDDep() {
        return dDep;
    }

    private D dDep;

    public B(C cDep, D dDep) {

        this.cDep = cDep;
        this.dDep = dDep;
    }
}
