package test.java.cn.edu.sustech.testclasses;

public class K {

    private E eDep;
    private F fDep;

    public K(E eDep, F fDep){

        this.eDep = eDep;
        this.fDep = fDep;
    }

    public E getEDep() {
        return eDep;
    }

    public F getFDep() {
        return fDep;
    }
}
