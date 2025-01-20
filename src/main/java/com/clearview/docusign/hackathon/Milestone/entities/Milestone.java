package com.clearview.docusign.hackathon.Milestone.entities;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "milestone")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long milestoneId;

    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;
}