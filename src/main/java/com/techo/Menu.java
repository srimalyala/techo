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
     * Returns the max satisfaction
     */
    public int getMaxSatisfaction(final Map<Integer, Integer> map,
                                  final int timeInMins) {

        int[] satisfactions = new int[100];
        int[] times = new int[100];
        int index = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {

            satisfactions[index] = entry.getKey();
            times[index] = entry.getValue();
            index++;

        }

        return getMaxSatisfaction(satisfactions, times, timeInMins);

    }


    /**
     * Uses knapsack algorithm
     * @param satisfactions
     * @param times
     * @param timeInMins
     * @return
     */
    public static int getMaxSatisfaction(int satisfactions[], int times[], int timeInMins) {
        int N = times.length;
        int[][] V = new int[N + 1][timeInMins + 1];

        for (int col = 0; col <= timeInMins; col++) {
            V[0][col] = 0;
        }

        for (int row = 0; row <= N; row++) {
            V[row][0] = 0;
        }
        for (int item = 1; item <= N; item++) {
            for (int weight = 1; weight <= timeInMins; weight++) {
                if (times[item - 1] <= weight) {
                    V[item][weight] = Math.max(satisfactions[item - 1] + V[item - 1][weight - times[item - 1]], V[item - 1][weight]);
                } else {
                    V[item][weight] = V[item - 1][weight];
                }
            }
        }

        return V[N][timeInMins];
    }

}

