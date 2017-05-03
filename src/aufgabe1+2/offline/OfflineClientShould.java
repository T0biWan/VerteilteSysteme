package offline;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OfflineClientShould {
   private OfflineClient oc;

   @Before
   public void setUp() throws Exception {
      oc = new OfflineClient();
   }

   @After
   public void tearDown() throws Exception {

   }

   @Test
   public void foo() throws Exception {}

//    @Test
//    public void inputIsWithinBoundaries() throws Exception {
//        int min = oc.minimum;
//        int max = oc.maximum;
//
//        int leftOutside = min -1;
//        int leftInside = min;
//        int middle = max / 2; // muss irgendwie mit min addiert werden, falls min = 3 oder min = -10
//        int rightInside = max;
//        int rightOutside = max+1;
//
////        assertThat(inputIsWithinBoundaries(leftOutside), is(equalTo(false)));
////        assertThat(inputIsWithinBoundaries(leftInside), is(equalTo(true)));
////        assertThat(inputIsWithinBoundaries(leftOutside), is(equalTo(true)));
////        assertThat(inputIsWithinBoundaries(leftOutside), is(equalTo(true)));
////        assertThat(inputIsWithinBoundaries(leftOutside), is(equalTo(false)));
//    }

}
