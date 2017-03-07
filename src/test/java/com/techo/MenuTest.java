package com.techo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by srikanth on 2017/03/07.
 */
public class MenuTest {

    Menu menu;

    Map<Integer, Integer> map = null;

    @Before
    public void setup() {
        /**
         * TODO - Autowire instead of hardcode
         */
        menu = new Menu();

        map = new HashMap<>();

        map.put(5, 2);
        map.put(8, 4);
        map.put(10, 5);
    }

    /**
     * Positive - Expects max satisfaction 18 in 8 minutes
     */
    @Test
    public void testGetMaxSatisfaction_Positive() {


        final int maxSatisfaction = menu.getMaxSatisfaction(map, 9);

        Assert.assertEquals(18, maxSatisfaction);


    }


    /**
     * Negative - Expects max satisfaction 18 in 6 minutes i.e incorrect , should be 12
     */
    @Test
    public void testGetMaxSatisfaction_Negative() {


        final int maxSatisfaction = menu.getMaxSatisfaction(map, 6);

        Assert.assertNotEquals(18, maxSatisfaction);


    }
}


