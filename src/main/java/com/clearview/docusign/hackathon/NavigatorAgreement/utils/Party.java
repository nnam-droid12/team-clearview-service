package com.clearview.docusign.hackathon.NavigatorAgreement.utils;

import java.util.Map;

public class Party {
    private String id;
    private String name;
    private String role;
    private Map<String, Object> attributes;

    public Party() {
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getRole() {
        return this.role;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Party)) return false;
        final Party other = (Party) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$role = this.getRole();
        final Object other$role = other.getRole();
        if (this$role == null ? other$role != null : !this$role.equals(other$role)) return false;
        final Object this$attributes = this.getAttributes();
        final Object other$attributes = other.getAttributes();
        if (this$attributes == null ? other$attributes != null : !this$attributes.equals(other$attributes))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Party;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $role = this.getRole();
        result = result * PRIME + ($role == null ? 43 : $role.hashCode());
        final Object $attributes = this.getAttributes();
        result = result * PRIME + ($attributes == null ? 43 : $attributes.hashCode());
        return result;
    }

    public String toString() {
        return "Party(id=" + this.getId() + ", name=" + this.getName() + ", role=" + this.getRole() + ", attributes=" + this.getAttributes() + ")";
    }
}
