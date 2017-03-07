package com.techo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

/**
 * Created by srikanth on 2017/03/07.
 */
@Component
public class Menu {

    private final Logger log = LoggerFactory.getLogger(Menu.class);

    @PostConstruct
    public void init() {

        BufferedReader br = null;
        try {

            final Resource resource = new ClassPathResource("data.txt");
            final InputStream resourceInputStream = resource.getInputStream();

            br = new BufferedReader(
                    new InputStreamReader(resourceInputStream));
            String line = br.readLine();
            String[] split = line.split(" ");
            final int timeInMins = new Integer(split[0]);
            HashMap<Integer, Integer> map = new HashMap();

            while ((line = br.readLine()) != null) {

                split = line.split(" ");

                map.put(new Integer(split[0]),
                        new Integer(split[1]));

            }

            final int maxSatisfaction = getMaxSatisfaction(map, timeInMins);

            log.info("Max satisfaction - {} in {} minutes time ", maxSatisfaction, timeInMins);


        } catch (Exception ex) {
            log.error("Exception occurred parsing file" + ex);

        } finally {
            try {
                br.close();
            } catch (IOException e) {
                log.error("Exception occurred closing stream", e);
            }
        }

    }

    /**
     * Sorts descending by satisfaction level and returns max satisfaction.
     */
    public int getMaxSatisfaction(final Map<Integer, Integer> map,
                                  final int timeInMins) {

        int maxSatisfaction = 0;

        final Map<Integer, Integer> treeMap = new TreeMap<>(
                (o1, o2) -> {
                    if (o1 < o2) {
                        return 1;
                    } else if (o1 > o2) {
                        return -1;
                    } else {
                        return 0;
                    }
                });

        treeMap.putAll(map);

        int leftTimeInMins = timeInMins;

        for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {

            if (entry.getValue() == leftTimeInMins) {
                maxSatisfaction = maxSatisfaction + entry.getKey();
                break;
            }

            if (entry.getValue() < leftTimeInMins) {
                maxSatisfaction = maxSatisfaction + entry.getKey();
            } else {
                maxSatisfaction += (leftTimeInMins * entry.getKey()) / entry.getValue();
                break;
            }

            leftTimeInMins = leftTimeInMins - entry.getValue();
        }


        return maxSatisfaction;

    }

}

