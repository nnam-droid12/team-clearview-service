package com.clearview.docusign.hackathon.NavigatorAgreement.utils;

public class PaginationInfo {
    private int totalItems;
    private int currentPage;
    private int itemsPerPage;
    private String nextPageUrl;

    public PaginationInfo() {
    }

    public int getTotalItems() {
        return this.totalItems;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getItemsPerPage() {
        return this.itemsPerPage;
    }

    public String getNextPageUrl() {
        return this.nextPageUrl;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PaginationInfo)) return false;
        final PaginationInfo other = (PaginationInfo) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getTotalItems() != other.getTotalItems()) return false;
        if (this.getCurrentPage() != other.getCurrentPage()) return false;
        if (this.getItemsPerPage() != other.getItemsPerPage()) return false;
        final Object this$nextPageUrl = this.getNextPageUrl();
        final Object other$nextPageUrl = other.getNextPageUrl();
        if (this$nextPageUrl == null ? other$nextPageUrl != null : !this$nextPageUrl.equals(other$nextPageUrl))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PaginationInfo;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getTotalItems();
        result = result * PRIME + this.getCurrentPage();
        result = result * PRIME + this.getItemsPerPage();
        final Object $nextPageUrl = this.getNextPageUrl();
        result = result * PRIME + ($nextPageUrl == null ? 43 : $nextPageUrl.hashCode());
        return result;
    }

    public String toString() {
        return "PaginationInfo(totalItems=" + this.getTotalItems() + ", currentPage=" + this.getCurrentPage() + ", itemsPerPage=" + this.getItemsPerPage() + ", nextPageUrl=" + this.getNextPageUrl() + ")";
    }
}
