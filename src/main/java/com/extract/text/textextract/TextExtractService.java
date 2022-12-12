package com.extract.text.textextract;

import org.apache.tomcat.util.codec.binary.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class TextExtractService {

    private final Logger logger =
            LoggerFactory.getLogger(TextExtractService.class);

    @Autowired
    private RestTemplates restTemplate;
    @Autowired
    private TextExtractRepository repo;
    public List<NewsRelease> getAllNewsReleases(){
        return repo.findAll();
    }

    public String getPressReleaseFromHappy(){
        String url="https://www.happiestminds.com/news-and-events/press-releases/";

        ResponseEntity<String> message = restTemplate.getForEntity(url,String.class);
        List<String> messages = Arrays.stream(message.getBody().split("\\n")).toList();
        List<String> headings = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for(String msg:messages){
            if(msg.matches("(.*)p class=\"box_title\"(.*)")){
                //Pattern pattern = Pattern.compile("\">(.*)</a>");
                //pattern.matcher(msg).results().map(s->s.group(1)).forEach(System.out::println);

                //System.out.println(msg.replaceAll("(.*)\">",""));
                msg=msg.replaceAll("(.*)\">","");
                msg=msg.replaceAll("</a>(.*)","");
                headings.add(msg);
                //System.out.println("msg: "+msg);

            } else if (msg.matches("(.*)p class=\"tech_date\"(.*)")) {
                msg=msg.replaceAll("(.*)\">","");
                msg=msg.replaceAll("</(.*)","");
                dates.add(msg);
                //System.out.println("Day: "+msg);

            }
            for(int i=0;i<headings.size();i++){
                NewsRelease newsRelease = new NewsRelease();
                if(i<dates.size()){
                    newsRelease.setPublishedDate(dates.get(i));
                }
                newsRelease.setHeading(headings.get(i));
                repo.save(newsRelease);
            }

        }

        //System.out.println("Fetched values from Happy: "+message.getBody());
        //logger.info(message);
        return "Successfully fetched";
    }

    public String getPressReleaseFromHappyScrape() {
        String url = "https://www.happiestminds.com/news-and-events/press-releases/";
        try {
            Document doc = Jsoup.connect(url).get();
            //System.out.println(doc.getElementById("wrap_page"));
            Elements elements = doc.getElementsByClass("tech_box");
            for(Element element:elements){
                NewsRelease nr = new NewsRelease();
                System.out.println((element.getElementsByClass("box_title").text()));
                System.out.println(element.getElementsByClass("tech_date").text());
                nr.setHeading(element.getElementsByClass("box_title").first().text());
                nr.setPublishedDate(element.getElementsByClass("tech_date").first().text());
                repo.save(nr);

            }

        }catch (IOException e){
            System.out.println(e);
        }
        return "Successful.";
    }
    public List<String> getUrlsFromHappy(){
        String url = "https://www.happiestminds.com/news-and-events/press-releases/";
        Connection con = Jsoup.connect(url);
        con.timeout(1000);
        try{
            Document doc = con.get();
            //Elements elements = doc.getElementsByTag("a");
            Elements elements = doc.select("a[href]");
            elements.stream().forEach(element -> {
                System.out.println(element.absUrl("href"));
            });
        }catch (IOException e){
            e.printStackTrace();
        }
        return Arrays.asList("Success","full");
    }

    public List<String> getUrlsByLevel(int level){
        String url = "https://www.happiestminds.com/news-and-events/press-releases/";
        //int level=2;
        System.out.println("Levels:"+level);
        crawl(level,url,new ArrayList<String>());

        return Arrays.asList("Success","fully Retrived");
    }
    public void crawl(Integer level,String url,ArrayList<String> visited){
        if(level>=0){
            System.out.println("Level:"+level);
            Document doc = request(url,visited);
            System.out.println("Returned Doc");
            //Document doc = con.get();
            if(doc!=null){
                System.out.println("Inside Doc!=Null");
                Elements elements = doc.select("a[href]");
                elements.stream().forEach(element -> {
                    String link = element.absUrl("href");
                    if(!visited.contains(link)){
                        visited.add(link);
                        System.out.println("Level "+level+":"+element.absUrl("href"));
                        crawl(level-1,link,visited);
                    }
                });
            }

        }
    }
    public Document request(String url,ArrayList<String> visited){
        try{
            System.out.println("Inside Try");
            System.out.println("Url:"+url);

            Connection con = Jsoup.connect(url);
            Document doc = con.get();
            System.out.println("Connection Status"+con.response().statusCode());
            if(con.response().statusCode()==200){
                //System.out.println("Link: "+url);
                System.out.println();
                visited.add(url);

                return doc;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
