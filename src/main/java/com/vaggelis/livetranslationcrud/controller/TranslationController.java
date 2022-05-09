package com.vaggelis.livetranslationcrud.controller;

import com.vaggelis.livetranslationcrud.Utils;
import com.vaggelis.livetranslationcrud.model.Translation;
import com.vaggelis.livetranslationcrud.repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/populartranslations")
    public ResponseEntity<List<String>> getPopularTranslations(){
        try {
            List<Translation> translations = new ArrayList<>(translationRepository.findAll());

            if (translations.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            Map<String, Integer> map = new HashMap<>();
            List<String> outputArray = new ArrayList<>();

            for (Translation current : translations){
                int count = map.getOrDefault(current.toString(), 0);
                map.put(current.toString(), count+1 );
                outputArray.add(current.toString());
                System.out.println("TEXT: " + current + " COUNT: " + count);
            }


            Comparator<String> levenshteinComp = (o1, o2) -> (int) Utils.similarity(o1, o2)*1000; // to *1000 einai epeidh to similarity exei times apo 0 ews 1
            outputArray.sort(levenshteinComp);

            List<String> groupedList = Utils.groupBySimilarity(outputArray);

            System.out.println("OUTPUT ARRAY IS: " + outputArray);
            System.out.println("GROUPED ARRAY IS: " + groupedList);

            // Sort me vash to poses fores emfanizetai to kathe string
            Comparator<String> lastCharComp = (a, b) -> Integer.compare(b.charAt(b.length() -1), a.charAt(a.length()-1));
            groupedList.sort(lastCharComp);



            return new ResponseEntity<>(groupedList, HttpStatus.OK);
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
}
