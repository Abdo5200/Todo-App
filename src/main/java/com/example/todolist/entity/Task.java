package com.example.todolist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "task")
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;


    @Getter
    @Setter
    @Column(name = "title", nullable = false)
    private String title;


    @Getter
    @Setter
    @Column(name = "description", nullable = true)
    private String description;


    @Getter
    @Setter
    @Column(name = "label", nullable = false)
    private String label;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user;

    public Task() {

    }

    public Task(String title, String description, String label, User user) {
        this.title = title;
        this.description = description;
        this.label = label;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", label='" + label + '\'' +
                ", user=" + user +
                '}';
    }
}
