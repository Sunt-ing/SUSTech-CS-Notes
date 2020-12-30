import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class IteratorTest {

   private List<String> list;
   private Iterator<String> iterator;


   //setUp() – Creates a list with two strings – Initializes an iterator
   @Before
   public void setUp() {
      list = new ArrayList<String>();
      list.add ("one");
      list.add ("two");
      iterator = list.iterator();
   }


   // Tests for hasNext() with (C1,C5) = {TT, FT,TF}
   /*
    *  C1: iterator has more values
    * C5: collection in consistent state while iterator in use
    *
    * */
   @Test public void testHasNext_TT()
   {
      assertTrue (iterator.hasNext());
   }


   @Test public void testHasNext_FT()
   {
      int i = list.size();
      while(i>0){
         iterator.next();
         i--;
      }
      assertFalse (iterator.hasNext());
   }


   @Test(expected= ConcurrentModificationException.class)
   public void testHasNext_TF()
   {
      iterator.next();
      list.add ("three");
      iterator.next();
   }



   // Tests for Iterator method next()
   /*
    *  C1: iterator has more values
    * C2: iterator returns a non-null object reference
    * C5: collection in consistent state while iterator in use
    *
    * */

   // C1-T, C2-T, C5-T
   @Test public void testNext_TTT()
   {
      assertEquals ("one", iterator.next());
      iterator.remove();
   }

   // C1-F, C2-F, C5-T
   @Test(expected= NoSuchElementException.class)
   public void testNext_FFT()
   {
      int i = list.size();
      while(i>0){
         iterator.next();
         i--;
      }
      assertNull(iterator.next());// now empty
   }

   //  C1-T, C2-F, C5-T
   @Test public void testNext_TFT()
   {
      List<String> nullList = new ArrayList<String>();
      nullList.add (null);
      Iterator<String> iterator = nullList.iterator();
      assertNull(iterator.next());
   }

   //  C1-T, C2-F, C5-F
   @Test(expected=ConcurrentModificationException.class)
   public void testNext_TFF()
   {
      List<String> nullList = new ArrayList<String>();
      nullList.add (null);
      Iterator<String> iterator = nullList.iterator();
      nullList.add ("what");
      assertNull(iterator.next());
   }

   /*
    * C1: iterator has more values
    * C2: iterator returns a non-null object reference
    * C3: remove() is supported
    * C4: remove() precondition is satisfied
    * C5: collection in consistent state while iterator in use
    */

   //  C1-T, C2-T, C3-T, C4-T, C5-T
   @Test public void testRemove_TTTTT()
   {
      assertEquals("one",iterator.next());
      iterator.remove();
      assertFalse (list.contains ("one"));
   }

   //  C1-F, C2-F, C3-T, C4-T, C5-T
   @Test public void testRemove_FFTTT()
   {

      int i = list.size();
      while(i>0){
         iterator.next();
         i--;
      }
      iterator.remove();
      assertFalse (list.contains ("two"));
   }

   // C1-T, C2-F, C3-T, C4-T, C5-T
   @Test public void testRemove_TFTTT()
   {
      List<String> nullList = new ArrayList<String>();
      nullList.add (null);
      nullList.add ("last");
      Iterator<String> iterator = nullList.iterator();
      iterator.next();        // consume null; iterator not empty
      iterator.remove();      // remove null from list
      assertFalse (nullList.contains (null));
   }

   //  C1-T, C2-T, C3-F, C4-T, C5-T
   @Test(expected=UnsupportedOperationException.class)
   public void testRemove_TTFTT()
   {
      list = Collections.unmodifiableList (list);
      iterator = list.iterator();
      iterator.next();   // consume first element to make C4 true
      iterator.remove();
   }

   // C1-T, C2-T, C3-T, C4-F, C5-T
   @Test (expected=IllegalStateException.class)
   public void testRemove_TTTFT()
   {
      iterator.remove();
   }


   // C1-T, C2-T, C3-T, C4-T, C5-F
   @Test (expected=ConcurrentModificationException.class)
   public void testRemove_TTTTF()
   {
      iterator.next();
      list.add ("more");
      iterator.next();
   }


}
