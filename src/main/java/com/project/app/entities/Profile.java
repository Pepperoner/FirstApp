package com.project.app.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profilePicture;

    private Long likes;

    private Long dislikes;

    private String achievements;

    private String information;

    private String skills;

    @OneToOne
    private User user;

    public Profile() {
    }

    public Profile(User user) {
        this.user = user;
    }
}
