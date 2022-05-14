package com.vaggelis.livetranslationcrud;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) return 1.0; // both strings are zero length

        LevenshteinDistance x = new LevenshteinDistance();
        return (longerLength - x.apply(longer, shorter)) / (double) longerLength;
    }

    public static List<String> groupBySimilarity(List<String> list){
        List<String> newList = new ArrayList<>();
        int count = 1;
        String item = list.get(0);
        boolean entered = false;
        for (int i = 1; i < list.size(); i++){
            //System.out.println("SIMILARITY OF '" + item + "' and '" + list.get(i) + "' is " + similarity(item, list.get(i)));
            if (similarity(item, list.get(i)) >= 0.7){
                entered = false;
                count += 1;
                if (item.length() > list.get(i).length()){
                    item = list.get(i);
                }
            } else {
                // If this condition isn't met then we need to do these steps outside the loop
                entered = true;
                String x = item + " *" + count;
                newList.add(x);

                item = list.get(i);
                count = 1;
            }
        }
        if (!entered) {
            String x = item + " *" + count;
            newList.add(x);
        }

        return newList;
    }
}
