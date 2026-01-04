package org.example.ecommerce.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Entity

@Data
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mobileNo;

    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String gender;
    private LocalDate dateOfBirth;

    private boolean status;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnore
//    @ToString.Exclude
    private List<Roles> roles;
}

