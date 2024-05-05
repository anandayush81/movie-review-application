package dev.ayush.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class User extends BaseModel {
    private String email;
    private String username;
    private String hashedPassword;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
    private String address;
    private String phone;
}

