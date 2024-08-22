package com.mibr.store.data.category;

import com.mibr.store.data.history.History;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int quantity;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<History> history;

}
