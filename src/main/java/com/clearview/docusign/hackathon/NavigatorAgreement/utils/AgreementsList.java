package com.clearview.docusign.hackathon.NavigatorAgreement.utils;

import java.util.List;

public class AgreementsList {
    private List<AgreementSummary> agreements;
    private PaginationInfo pagination;

    public AgreementsList() {
    }

    public List<AgreementSummary> getAgreements() {
        return this.agreements;
    }

    public PaginationInfo getPagination() {
        return this.pagination;
    }

    public void setAgreements(List<AgreementSummary> agreements) {
        this.agreements = agreements;
    }

    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AgreementsList)) return false;
        final AgreementsList other = (AgreementsList) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$agreements = this.getAgreements();
        final Object other$agreements = other.getAgreements();
        if (this$agreements == null ? other$agreements != null : !this$agreements.equals(other$agreements))
            return false;
        final Object this$pagination = this.getPagination();
        final Object other$pagination = other.getPagination();
        if (this$pagination == null ? other$pagination != null : !this$pagination.equals(other$pagination))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AgreementsList;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $agreements = this.getAgreements();
        result = result * PRIME + ($agreements == null ? 43 : $agreements.hashCode());
        final Object $pagination = this.getPagination();
        result = result * PRIME + ($pagination == null ? 43 : $pagination.hashCode());
        return result;
    }

    public String toString() {
        return "AgreementsList(agreements=" + this.getAgreements() + ", pagination=" + this.getPagination() + ")";
    }
}