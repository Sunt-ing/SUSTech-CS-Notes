package test.java.cn.edu.sustech.testclasses;


import main.java.cn.edu.sustech.Inject;

public class G {

    public C getCDep() {
        return cDep;
    }

    public D getDDep() {
        return dDep;
    }

    @Inject
    private C cDep;

    @Inject
    private D dDep;

}

