package com.clearview.docusign.hackathon.NavigatorAgreement.utils;

public class RelatedAgreement {
    private String id;
    private String relationship;
    private String title;

    public RelatedAgreement() {
    }

    public String getId() {
        return this.id;
    }

    public String getRelationship() {
        return this.relationship;
    }

    public String getTitle() {
        return this.title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RelatedAgreement)) return false;
        final RelatedAgreement other = (RelatedAgreement) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$relationship = this.getRelationship();
        final Object other$relationship = other.getRelationship();
        if (this$relationship == null ? other$relationship != null : !this$relationship.equals(other$relationship))
            return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RelatedAgreement;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $relationship = this.getRelationship();
        result = result * PRIME + ($relationship == null ? 43 : $relationship.hashCode());
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        return result;
    }

    public String toString() {
        return "RelatedAgreement(id=" + this.getId() + ", relationship=" + this.getRelationship() + ", title=" + this.getTitle() + ")";
    }
}