package com.vaggelis.livetranslationcrud;

import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.*;

public class Utils {

    public static List<StringWithCounter> getWordCount(List<String> arr)
    {
        // create a list to store unique words and another one to store their frequency
        List<String> words = new ArrayList<>();
        List<Integer> counter = new ArrayList<>();

        // iterate through array of words
        for (String s : arr) {
            if (!words.contains(s)) {
                words.add(s);
                counter.add(0);
            }
            int i = words.indexOf(s);
            counter.set(i, counter.get(i) + 1);
        }

        List<StringWithCounter> finalList = new ArrayList<>();
        for (int i = 0; i < words.size(); i++){
            finalList.add(new StringWithCounter(words.get(i), counter.get(i)));
        }

        return finalList;
    }
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

    public static List<StringWithCounter>  groupBySimilarity(List<String> list){
        List<StringWithCounter> newList = new ArrayList<>();
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
                newList.add(new StringWithCounter(item, count));

                item = list.get(i);
                count = 1;
            }
        }
        if (!entered) {
            newList.add(new StringWithCounter(item, count));
        }

        // Remove the strings with a counter of 1
        newList.removeIf(swc -> swc.getCounter() == 1);

        return newList;
    }
}
