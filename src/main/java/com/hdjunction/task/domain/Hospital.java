package com.hdjunction.task.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hospital {

    @Id
    @Column(name = "hospital_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hospital_name", length = 45, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String sanatoriumNumber;

    @Column(name = "hospital_director", length = 10, nullable = false)
    private String director;

    @OneToMany(mappedBy = "hospital")
    private List<Patient> patients = new ArrayList<>();

    @OneToMany(mappedBy = "hospital")
    private List<Visit> visits = new ArrayList<>();

    public static Hospital of(){
        return new Hospital();
    }
}
