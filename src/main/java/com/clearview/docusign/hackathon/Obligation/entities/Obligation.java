package com.clearview.docusign.hackathon.Obligation.entities;

import com.clearview.docusign.hackathon.Agreement.entities.Agreement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "obligation")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Obligation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long obligationId;

    private String description;

    private LocalDateTime dueDate;

    private String assignedTo;

    private String status;

    @ManyToOne
    @JoinColumn(name = "agreement_id")
    private Agreement agreement;
}
