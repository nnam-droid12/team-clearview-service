package com.clearview.docusign.hackathon.NavigatorAgreement.utils;

import java.util.Map;

class LegalProvision {
    private String id;
    private String type;
    private String content;
    private Map<String, Object> attributes;

    public LegalProvision() {
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getContent() {
        return this.content;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LegalProvision)) return false;
        final LegalProvision other = (LegalProvision) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) return false;
        final Object this$attributes = this.getAttributes();
        final Object other$attributes = other.getAttributes();
        if (this$attributes == null ? other$attributes != null : !this$attributes.equals(other$attributes))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LegalProvision;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $type = this.getType();
        result = result * PRIME + ($type == null ? 43 : $type.hashCode());
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
        final Object $attributes = this.getAttributes();
        result = result * PRIME + ($attributes == null ? 43 : $attributes.hashCode());
        return result;
    }

    public String toString() {
        return "LegalProvision(id=" + this.getId() + ", type=" + this.getType() + ", content=" + this.getContent() + ", attributes=" + this.getAttributes() + ")";
    }
}
