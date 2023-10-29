package mingo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@RestController
public class TestContorller {

    @GetMapping("/get-time")
    public String getTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @GetMapping("get-lotto")
    public Set<Integer> getLotto() {
        Random random = new Random();
        Set<Integer> uniqueNumbers = new HashSet<>();

        while (uniqueNumbers.size() < 6) {
            int randomNumber = random.nextInt(45) + 1;
            uniqueNumbers.add(randomNumber);
        }

        return uniqueNumbers;
    }

}
