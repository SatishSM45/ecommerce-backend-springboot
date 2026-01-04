package org.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.ecommerce.enums.Role_Type;

@Entity
@Data
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name="role")
    private Role_Type role;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
//    @ToString.Exclude
    private User user;
    private boolean status;
}
