import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class TestFileWriter {
    @Test
    public void testWriteFile_exception() throws Exception {
        Method method = null;
        method = FileWriter.class.getDeclaredMethod("writeFile", new Class[]{String.class, String.class});
        method.setAccessible(true);
        FileWriter fw = new FileWriter();
        try {
            method.invoke(fw, "./FileWriter.java", "");
        } catch (Exception e) {
            return;
        }
        fail();
    }

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testWriteFile_pass() throws Exception {
        String fileName = "./deleteMe.txt";
        String fileContent = "oh my test!";
        File output = new File(fileName);
        output.delete();

        Method method = null;
        method = FileWriter.class.getDeclaredMethod("writeFile", new Class[]{String.class, String.class});
        method.setAccessible(true);
        method.invoke(new FileWriter(), fileName, fileContent);

        Scanner sc = new Scanner(output);
        String actual = sc.nextLine();
        assertEquals(actual, fileContent);
    }
}