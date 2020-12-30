import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class TestPrimitiveParameters {

    @Test
    public void go() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        PrintStream originalOut = System.out;
        System.setOut(ps);

        PrimitiveParameters.go();
        String separator = System.getProperty("line.separator");
        String actual = os.toString();
        String expected = "In method go. x: 3 y: 2" + separator +
                "in method falseSwap. x: 3 y: 2" + separator +
                "in method falseSwap. x: 2 y: 3" + separator +
                "in method go. x: 3 y: 2" + separator +
                "in method moreParameters. a: 3 b: 2" + separator +
                "in method moreParameters. a: 6 b: 12" + separator +
                "in method falseSwap. x: 12 y: 6" + separator +
                "in method falseSwap. x: 6 y: 12" + separator +
                "in method moreParameters. a: 6 b: 12" + separator +
                "in method go. x: 3 y: 2" + separator;
        assertEquals(expected, actual);

        System.setOut(originalOut);
    }

    @Test
    public void falseSwap() {
        PrintStream originalOut = System.out;
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);

        PrimitiveParameters.falseSwap(0, 1);
        String separator = System.getProperty("line.separator");
        String expected = "in method falseSwap. x: 0 y: 1" + separator +
                "in method falseSwap. x: 1 y: 0" + separator;
        String actual = os.toString();
        assertEquals(expected, actual);

        System.setOut(originalOut);
    }

    @Test
    public void moreParameters() {
        PrintStream originalOut = System.out;
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);

        PrimitiveParameters.moreParameters(0, 1);
        String separator = System.getProperty("line.separator");
        String expected = "in method moreParameters. a: 0 b: 1" + separator +
                "in method moreParameters. a: 0 b: 12" + separator +
                "in method falseSwap. x: 12 y: 0" + separator +
                "in method falseSwap. x: 0 y: 12" + separator +
                "in method moreParameters. a: 0 b: 12" + separator;
        String actual = os.toString();
        assertEquals(expected, actual);

        System.setOut(originalOut);

    }
}