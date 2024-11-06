package com.studentgroup.app.model;

import com.studentgroup.app.Misc;
import com.studentgroup.app.model.enums.Role;
import com.studentgroup.app.webservices.UserInfo;
import jakarta.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;


@Entity
@Table(name = "EMPLOYEE")
public class EmployeeUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EMP_ID")
    private Long id;

    private String username;
    private String firstname;
    private String lastname;
    private String passwordHash;
    private String salt;

    @Enumerated(EnumType.STRING)
    Role role = Role.UNKNOWN;

    @OneToMany()
    private List<ProductOrder> orders;

    @OneToMany(mappedBy = "employee")
    private List<ActionLog> actionLogs;

    //constructors
    public EmployeeUser() {}

    public EmployeeUser(String username, String firstname, String lastname, String password, Role role) throws NoSuchAlgorithmException {
        this.username = username;
        this.role = role;
        this.firstname = firstname;
        this.lastname = lastname;

        this.salt = Misc.genSalt();
        this.passwordHash = Misc.hashPassword(password, this.salt);
    }

    public EmployeeUser(UserInfo userInfo, String password) throws NoSuchAlgorithmException {
        this.username = userInfo.getUsername();
        this.role = userInfo.getRole();
        this.firstname = userInfo.getFirstname();
        this.lastname = userInfo.getLastname();

        this.salt = Misc.genSalt();
        this.passwordHash = Misc.hashPassword(password, this.salt);
    }


    //misc methods
    public boolean verify(String password) throws Exception {
        return Misc.hashPassword(password, salt).equals(passwordHash);
    }

    public String toString() {
        return "{ username: " + username + ", passwordHash: " + passwordHash + ", salt: " + salt + ", role: " + role.toString() + " }";
    }

    //getters and setters
    public List<ProductOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ProductOrder> orders) {
        this.orders = orders;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
