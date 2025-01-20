package com.clearview.docusign.hackathon.Agreement.entities;

import com.clearview.docusign.hackathon.Milestone.entities.Milestone;
import com.clearview.docusign.hackathon.Obligation.entities.Obligation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agreement")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long agreementId;

    private String docuSignEnvelopeId;

    private String status;

    private LocalDateTime signedDate;

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL)
    private List<Milestone> milestones = new ArrayList<>();

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL)
    private List<Obligation> obligations = new ArrayList<>();
}
