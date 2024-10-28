package com.studentgroup.app.model;

import com.studentgroup.app.Misc;

import jakarta.persistence.*;
import java.security.NoSuchAlgorithmException;


@Entity
public class EmployeeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String username;

    private String passwordHash;
    private String salt;
    private String token;

    @Enumerated(EnumType.ORDINAL)
    Role role;


    public EmployeeUser() {}

    public EmployeeUser(String username, String password, Role role) throws NoSuchAlgorithmException {
        this.username = username;
        this.role = role;
        this.token = Misc.genToken();
        this.salt = Misc.genSalt();
        this.passwordHash = Misc.hashPassword(password, this.salt);
    }

    public boolean verify(String password) throws Exception {
        return Misc.hashPassword(password, salt).equals(passwordHash);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toString() {
        return "{ username: " + username + ", passwordHash: " + passwordHash + ", salt: " + salt + ", role: " + role.toString() + " }";
    }

}
