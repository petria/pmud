package org.freakz.pmud.common.objects;

import org.junit.Assert;
import org.junit.Test;

public class PMudPObjectTest {

    @Test
    public void testPMudObjectInstance() {
        Zone zone = new Zone();
        Assert.assertEquals(1, zone.getId());
    }


}
