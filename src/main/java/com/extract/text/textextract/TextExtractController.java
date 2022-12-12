package com.extract.text.textextract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TextExtractController {
    @Autowired
    private TextExtractService service;
    @GetMapping("/press-releases")
    public List<NewsRelease> getAllNewsReleases(){
        return service.getAllNewsReleases();
    }
    @PostMapping("/getfromHappy")
    public String getPressReleaseFromHappy(){
        String message = service.getPressReleaseFromHappy();
        return "Fetched Successfully";
    }

    @PostMapping("/getfromHappy1")
    public String getPressReleaseFromHappyScrape(){
        String message = service.getPressReleaseFromHappyScrape();
        return "Fetched Successfully";
    }

    @GetMapping("/getUrlsFromHappy")
    public List<String> getUrlsFromHappy(){
        return service.getUrlsFromHappy();
    }

    @GetMapping("/getUrlsFromHappy/{levels}")
    public List<String> getUrlsByLevel(@PathVariable int levels){
        return service.getUrlsByLevel(levels);
    }

}
