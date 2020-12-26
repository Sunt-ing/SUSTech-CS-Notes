package test.java.cn.edu.sustech.testclasses;


import main.java.cn.edu.sustech.Inject;

public class P {

    @Inject
    private Q qDep;

    public Q getQDep(){
        return this.qDep;
    }
}
