package com.shiryaeva.wyrgorod.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Table(name = "item")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Column(name = "price", precision = 19, scale = 2)
    private BigDecimal price;

    @Enumerated
    @Column(name = "medium")
    private Medium medium;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;

}