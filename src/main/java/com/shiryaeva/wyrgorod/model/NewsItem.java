package com.shiryaeva.wyrgorod.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Table(name = "news_item")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date date;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    public NewsItem(Date date, String title, String description, Artist artist) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.artist = artist;
    }
}