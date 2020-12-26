## MP2-part2

Name: Sun Ting（孙挺）

Student ID：11710108

Selected app：https://github.com/ankidroid/Anki-Android

Code to be analyzed: 

```java
static String[] splitTags(String tags) {
    if (tags == null) {
        return null;
    }
    return tags.trim().split("\\s+");
}
```

- Parameters: tags

- Identify the characteristic according to Interface-based Input Domain Modeling

  - Characteristic  

    - tags is null
    - tags is empty
    - tags's length > 0
    
  - Test Input
  
    - tags = null
    - tags = ""
    - tags = "aa bb"
  
  - JUnit Test

    - We use JUnit5 for convenience
  
    - ```java
      class UtilsTest {
          private Duration duration;
          
          void testSplitTags() {
              String str=null;
              assertNull(Utils.splitTags(str));
              assertArrayEquals({""}, Utils.splitTags(""));
              assertArrayEquals({"aa", "bb"}, Utils.splitTags("aa bb"));
        }
      }
      ```
    
    
  
- Identify the characteristic according to Functionality-based Input Domain Modeling  

  - Characteristic  

    - tags has no tag
    - tags has 1 tag
    - tags has more than 1 tags
    
  - Test Input
  
    - tags = "      "
    - tags = "aa"
    - tags = "aa bb cc"
  
  - JUnit Test

    - We use JUnit5 for convenience
  
    - ```java
      class UtilsTest {
          private Duration duration;
          
          void testSplitTags() { 
              assertArrayEquals({""}, Utils.splitTags("      "));
              assertArrayEquals({"aa"}, Utils.splitTags("aa"));
              assertArrayEquals({"aa", "bb", "cc"}, Utils.splitTags("aa bb cc"));
        }
      }
      ```
    
    













