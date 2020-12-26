**Name**: 孙挺

**Student ID**: 11710108



#### Selected method

```java
public static String refineMXDText(boolean flag, String input){
    if(flag && (input == null || input.length > 10))
        return "";
    return input;
}
```



#### Clause Coverage  

```java
@Test
public void TestTrueFlagAndNullInput(){ 
    assertEquals("",ShowTool.refineMXDText(true, null));
}

@Test
public void TestTrueFlagAndEmptyInput(){ 
    assertEquals("",ShowTool.refineMXDText(true, ""));
}

@Test
public void TestTrueFlagAndInputLongerThan10(){ 
    assertEquals("0123456789",ShowTool.refineMXDText(true, "012345678910"));
}

@Test
public void TestTrueFlagAndNormalInput(){ 
    assertEquals("qaq",ShowTool.refineMXDText(true, "qaq"));
}

@Test
public void TestFalseFlagAndNullInput(){ 
    assertEquals(null,ShowTool.refineMXDText(false, null));
}

@Test
public void TestFalseFlagAndEmptyInput(){ 
    assertEquals("",ShowTool.refineMXDText(false, ""));
}

@Test
public void TestFalseFlagAndInputLongerThan10(){ 
    assertEquals("012345678910",ShowTool.refineMXDText(false, "012345678910"));
}

@Test
public void TestFalseFlagAndNormalInput(){ 
    assertEquals("qaq",ShowTool.refineMXDText(false, "qaq"));
}
```


#### Predicate Coverage  

```java
@Test
public void TestTrueFlagAndNullInput(){
    // predicate to be true
    assertEquals("",ShowTool.refineMXDText(true, null));
}

@Test
public void TestTrueFlagAndNormalInput(){
    // predicate to be false
    assertEquals("qaq",ShowTool.refineMXDText(true, "qaq"));
}
```



#### CACC  

Major clause: flag

Minor clause: input == null, input.length > 10

```java
@Test
public void TestTrueFlagAndNullInput(){
    // predicate to be true
    assertEquals("",ShowTool.refineMXDText(true, null));
}

@Test
public void TestFalseFlagAndNormalInput(){
    // predicate to be false
    assertEquals(null,ShowTool.refineMXDText(false, null));
}
```

#### RACC  

Major clause: flag

Minor clause: input == null, input.length > 10

```java
@Test
public void TestTrueFlagAndNullInput(){
    // predicate to be true
    assertEquals("",ShowTool.refineMXDText(true, null));
}

@Test
public void TestFalseFlagAndNullInput(){
    // predicate to be false
    assertEquals(null,ShowTool.refineMXDText(false, null));
}
```



#### Can you translate the tests from unit level to GUI level? If you cannot get the input from unit level to GUI level, explain why.  

No. 

Reason: It's a util method for some special output and thus we cannot test it from unit level to GUI level.