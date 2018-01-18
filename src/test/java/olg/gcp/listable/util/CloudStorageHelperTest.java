package olg.gcp.listable.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class CloudStorageHelperTest {

    @Test
    public void getOriginalImageUrl() {
        String original = "https://lh3.googlecontent.com/DaNXfYX_zs4LxVu2Z7oYHMzelDTbnMTX=s200-c";
        String edited = original.replaceAll("(.*)=.*", "$1");
        assertEquals("https://lh3.googlecontent.com/DaNXfYX_zs4LxVu2Z7oYHMzelDTbnMTX", edited);

        original = "https://lh3=googlecontent.com/DaNXfYX_zs4LxVu2Z7oYHMzelDTbnMTX=s200-c";
        edited = original.replaceAll("(.*)=.*", "$1");
        assertEquals("https://lh3=googlecontent.com/DaNXfYX_zs4LxVu2Z7oYHMzelDTbnMTX", edited);
    }
}