**Name**: 孙挺

**Student ID**: 11710108



#### Step I: Determine Logic Expression  

```java
if (a || (b && c))  
```



#### Step 2: Get Predicate Coverage  

```java
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class TestCheckIt {
    @Test
    public void testAIsFalseAndBIsFalseAndCIsFalse() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(false, false, false);
        assertEquals("P isn't true"+System.getProperty("line.separator"), os.toString());
    }

    @Test
    public void testAIsTrueAndBIsTrueAndCIsTrue() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(true, true, true);
        assertEquals("P is true" + System.getProperty("line.separator"), os.toString());
    }
}

```



#### Step 3: Get Clauses Coverage  

```java
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class TestCheckIt {
    @Test
    public void testAIsFalseAndBIsFalseAndCIsFalse() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(false, false, false);
        assertEquals("P isn't true"+System.getProperty("line.separator"), os.toString());
    }

    @Test
    public void testAIsTrueAndBIsTrueAndCIsTrue() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(true, true, true);
        assertEquals("P is true" + System.getProperty("line.separator"), os.toString());
    }
}
```



#### Step 4: Get CombinatinalCoverage  

```java
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class TestCheckIt {
    @Test
    public void testAIsFalseAndBIsFalseAndCIsFalse() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(false, false, false);
        assertEquals("P isn't true"+System.getProperty("line.separator"), os.toString());
    }

    @Test
    public void testAIsFalseAndBIsFalseAndCIsTrue() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(false, false, true);
        assertEquals("P isn't true" + System.getProperty("line.separator"), os.toString());
    }

    @Test
    public void testAIsFalseAndBIsTrueAndCIsFalse() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(false, true, false);
        assertEquals("P isn't true" + System.getProperty("line.separator"), os.toString());
    }

    @Test
    public void testAIsFalseAndBIsTrueAndCIsTrue() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(false, true, true);
        assertEquals("P is true" + System.getProperty("line.separator"), os.toString());
    }

    @Test
    public void testAIsTrueAndBIsFalseAndCIsFalse() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(true, false, false);
        assertEquals("P is true" + System.getProperty("line.separator"), os.toString());
    }

    @Test
    public void testAIsTrueAndBIsFalseAndCIsTrue() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(true, false, true);
        assertEquals("P is true" + System.getProperty("line.separator"), os.toString());
    }

    @Test
    public void testAIsTrueAndBIsTrueAndCIsFalse() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(true, true, false);
        assertEquals("P is true" + System.getProperty("line.separator"), os.toString());
    }

    @Test
    public void testAIsTrueAndBIsTrueAndCIsTrue() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(true, true, true);
        assertEquals("P is true" + System.getProperty("line.separator"), os.toString());
    }
}
```



#### Step 5: Get Correlated Active Clause Coverage  

Major clause: a

```java
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class TestCheckIt {
    @Test
    public void testAIsFalseAndBIsFalseAndCIsFalse() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(false, false, false);
        assertEquals("P isn't true"+System.getProperty("line.separator"), os.toString());
    }

    @Test
    public void testAIsTrueAndBIsTrueAndCIsTrue() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(true, true, true);
        assertEquals("P is true" + System.getProperty("line.separator"), os.toString());
    }

}
```



#### Step 6: Get Restricted Active Clause Coverage  

Major clause: a

```java
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class TestCheckIt {    
	@Test
    public void testAIsTrueAndBIsTrueAndCIsTrue() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(true, true, true);
        assertEquals("P is true" + System.getProperty("line.separator"), os.toString());
    }

    @Test
    public void testAIsFalseAndBIsTrueAndCIsTrue() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        CheckIt.checkIt(false, true, true);
        assertEquals("P is true" + System.getProperty("line.separator"), os.toString());
    }
}
```