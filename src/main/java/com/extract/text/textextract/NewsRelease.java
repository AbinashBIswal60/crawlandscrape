package com.extract.text.textextract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Table(name = "press_release")
public class NewsRelease {
    /*
    @Id
    @GeneratedValue
    @Column(name = "heading_id")
    private int headingNo;
     */
    @Id
    private String heading;
    @Column(name = "publish_date")
    private String publishedDate;

    public NewsRelease() {
    }

    public NewsRelease(String heading, String publishedDate) {
        this.heading = heading;
        this.publishedDate = publishedDate;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

}
