package com.vaggelis.livetranslationcrud.controller;

import com.vaggelis.livetranslationcrud.StringWithCounter;
import com.vaggelis.livetranslationcrud.Utils;
import com.vaggelis.livetranslationcrud.model.Translation;
import com.vaggelis.livetranslationcrud.repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.AbstractMultiResolutionImage;
import java.util.*;

@CrossOrigin(origins="http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TranslationController {
    @Autowired
    TranslationRepository translationRepository;
    @GetMapping("/translations")
    public ResponseEntity<List<Translation>> getAllTranslations(@RequestParam(required = false) String userid){
        try{
            List<Translation> translations = new ArrayList<>();
            if (userid == null)
                translations.addAll(translationRepository.findAll());
            else
                translations.addAll(translationRepository.findByUserid(userid));
            if (translations.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(translations, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/translations")
    public ResponseEntity<Translation> createTranslation(@RequestBody Translation translation){
        try {
            Translation _translation = translationRepository.save(new Translation(translation.getTimestamp(),
                    translation.getLocation(), translation.getUserid(), translation.getOriginaltext(), translation.getTextlang(),
                    translation.getTranslatedtext(), translation.getTranslatedtextlang()));
            return new ResponseEntity<>(_translation, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/popularlanguages")
    public ResponseEntity<List<StringWithCounter>> popularLanguages(@RequestParam boolean translatedLanguage) {
        try {
            List<Translation> translations = new ArrayList<>(translationRepository.findAll());
            if (translations.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            List<String> languages = new ArrayList<>();
            List<Integer> count = new ArrayList<>();

            for (Translation tr : translations) {
                if (!translatedLanguage) {
                    if (!languages.contains(tr.getTextlang())) {
                        languages.add(tr.getTextlang());
                        count.add(0);
                    }
                    int i = languages.indexOf(tr.getTextlang());
                    count.set(i, count.get(i) + 1);
                } else {
                    if (!languages.contains(tr.getTranslatedtextlang())) {
                        languages.add(tr.getTranslatedtextlang());
                        count.add(0);
                    }
                    int i = languages.indexOf(tr.getTranslatedtextlang());
                    count.set(i, count.get(i) + 1);
                }
            }

            List<StringWithCounter> combinedList = new ArrayList<>();
            for (int i= 0; i < languages.size(); i++) {
                combinedList.add(new StringWithCounter(languages.get(i), count.get(i)));
            }

            Comparator<StringWithCounter> counterComparator = (s1, s2) -> Integer.compare(s2.getCounter(), s1.getCounter());
            combinedList.sort(counterComparator);

            return new ResponseEntity<>(combinedList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/userstotal")
    public ResponseEntity<List<StringWithCounter>> usersTotalTranslations() {
        try {
            List<Translation> translations = new ArrayList<>(translationRepository.findAll());
            if (translations.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            List<String> users = new ArrayList<>();
            List<Integer> count = new ArrayList<>();

            for (Translation tr : translations) {
                if (!users.contains(tr.getUserid())) {
                    users.add(tr.getUserid());
                    count.add(0);
                }
                int i = users.indexOf(tr.getUserid());
                count.set(i, count.get(i) + 1);
            }

            List<StringWithCounter> combinedList = new ArrayList<>();
            for (int i= 0; i < users.size(); i++) {
                combinedList.add(new StringWithCounter(users.get(i), count.get(i)));
            }

            Comparator<StringWithCounter> counterComparator = (s1, s2) -> Integer.compare(s2.getCounter(), s1.getCounter());
            combinedList.sort(counterComparator);

            return new ResponseEntity<>(combinedList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/populartranslations")
    public ResponseEntity<List<StringWithCounter>> getPopularTranslations() {
        try {
            List<Translation> translations = new ArrayList<>(translationRepository.findAll());

            if (translations.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            Map<String, Integer> map = new HashMap<>();
            List<String> outputArray = new ArrayList<>();

            for (Translation current : translations) {
                int count = map.getOrDefault(current.toString(), 0);
                map.put(current.toString(), count + 1);
                outputArray.add(current.toString());
                System.out.println("TEXT: " + current + " COUNT: " + count);
            }

            // We multiply by 1000 because similarity function returns a double from 0 to 1
            // and comparator needs integers
            Comparator<String> levenshteinComp = (o1, o2) -> (int) Utils.similarity(o1, o2) * 1000;
            outputArray.sort(levenshteinComp);

            List<StringWithCounter> groupedList = Utils.groupBySimilarity(outputArray);

            System.out.println("OUTPUT ARRAY IS: " + outputArray);
            System.out.println("GROUPED ARRAY IS: " + groupedList);

            // Sort based on how many times each string is present
            Comparator<StringWithCounter> lastCharComp = (a, b) -> Integer.compare(b.getCounter(), a.getCounter());
            groupedList.sort(lastCharComp);


            return new ResponseEntity<>(groupedList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
