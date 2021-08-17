package com.gsr.library.libraryapp.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "role")
@Data
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "permission_role", joinColumns = {
//            @JoinColumn(name = "role_id", referencedColumnName = "id")}, inverseJoinColumns = {
//            @JoinColumn(name = "permission_id", referencedColumnName = "id")})
    @JoinTable(name = "permission_role", joinColumns = @JoinColumn(name = "permission_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Permission> permissions;

}