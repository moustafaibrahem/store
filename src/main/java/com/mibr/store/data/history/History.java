package com.mibr.store.data.history;

import com.mibr.store.data.category.Category;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private String categoryName;

    private int quantity;

    @CreationTimestamp
    private LocalDateTime date;

    private String status;
}
