package test.java.cn.edu.sustech;


import main.java.cn.edu.sustech.Container;
import main.java.cn.edu.sustech.ContainerImpl;
import main.java.cn.edu.sustech.ServiceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.java.cn.edu.sustech.testclasses.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("WeakerAccess")
public class ContainerImplTest {

    private Container container;

    @BeforeEach
    public void setup() {
        this.container = new ContainerImpl();
    }

    @Test
    public void createNewContainer() {
        new ContainerImpl();
    }

    @Test
    public void testRegister() {
        container.register(A.class);
    }

    @Test
    public void testRegisterWithImpl() {
        container.register(E.class, EImpl.class);
    }

    @Test
    public void testRegisterNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            container.register(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            container.register(null, null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            container.register(E.class, null);
        });
    }

    @Test
    public void testResolveNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            container.resolve(null);
        });
    }

    @Test
    public void testRegisterAbstractClassOrInterface() {
        assertThrows(IllegalArgumentException.class, () -> {
            container.register(F.class);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            container.register(E.class);
        });
    }

    @Test
    public void testResolveWithNoDependency() {
        container.register(A.class);
        A instance = container.resolve(A.class);
        assertNotNull(instance);
    }

    @Test
    public void testWithConstructorDependency() {
        container.register(B.class);
        container.register(C.class);
        container.register(D.class);
        B instance = container.resolve(B.class);
        assertNotNull(instance);
        assertNotNull(instance.getCDep());
        assertNotNull(instance.getDDep());
    }

    @Test
    public void testWithAnnotationDependency() {
        container.register(G.class);
        container.register(C.class);
        container.register(D.class);
        G instance = container.resolve(G.class);
        assertNotNull(instance);
        assertNotNull(instance.getCDep());
        assertNotNull(instance.getDDep());
    }

    @Test
    public void testWithDependencyNotFound() {
        container.register(B.class);
        container.register(C.class);
        var exception = assertThrows(ServiceNotFoundException.class, () -> {
            container.resolve(B.class);
        });
    }

    @Test
    public void testServiceNotFound() {
        var exception = assertThrows(ServiceNotFoundException.class, () -> {
            container.resolve(A.class);
        });
    }

    @Test
    public void testMixedDependencies() {
        container.register(H.class);
        container.register(C.class);
        container.register(D.class);
        H instance = container.resolve(H.class);
        assertNotNull(instance);
        assertNotNull(instance.getCDep());
        assertNotNull(instance.getDDep());
    }

    @Test
    public void testImplTypeForInterface() {
        container.register(E.class, EImpl.class);
        E instance = container.resolve(E.class);
        assertNotNull(instance);
        assertTrue(instance instanceof EImpl);
    }

    @Test
    public void testImplTypeForAbstractClass() {
        container.register(F.class, FEnhanced.class);
        F instance = container.resolve(F.class);
        assertNotNull(instance);
        assertTrue(instance instanceof FEnhanced);
    }

    @Test
    public void testDependencyImpl() {
        container.register(K.class);
        container.register(E.class, EImpl.class);
        container.register(F.class, FEnhanced.class);
        K instance = container.resolve(K.class);
        assertNotNull(instance);
        assertNotNull(instance.getEDep());
        assertNotNull(instance.getFDep());
        assertTrue(instance.getEDep() instanceof EImpl);
        assertTrue(instance.getFDep() instanceof FEnhanced);
    }

    @Test
    public void testRegisterClassWithMultipleConstructors() {
        assertThrows(IllegalArgumentException.class, () -> {
            container.register(L.class);
        });
    }

    @Test
    public void testFieldWithoutInject() {
        container.register(M.class);
        container.register(C.class);
        M instance = container.resolve(M.class);
        assertNotNull(instance);
        assertNull(instance.getCDep());
    }

    @Test
    public void testInjectFieldsInSuperClass(){
        container.register(P.class, PEnhanced.class);
        container.register(Q.class);
        P instance = container.resolve(P.class);
        assertNotNull(instance);
        assertTrue(instance instanceof PEnhanced);
        assertNotNull(instance.getQDep());
    }

}
