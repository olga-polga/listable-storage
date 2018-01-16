package olg.gcp.listable.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class CloudStorageHelperTest {

    @Test
    public void appendToFilename() {
        String original = "dhaskdjfkj.jpg";
        String edited = CloudStorageHelper.appendToFilename(original, "abcd");
         System.out.print(edited);
    }
}