package com.inkloomai.backend.model;

public class SubmissionFinding {
    private String id;
    private String sev;
    private String category;
    private String claim;
    private String why;
    private String source;
    private String quote;
    private String fix;
    private String state;
    private String productName;
    private String companyName;
    private String ruleType;

    public SubmissionFinding() {}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSev() { return sev; }
    public void setSev(String sev) { this.sev = sev; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getClaim() { return claim; }
    public void setClaim(String claim) { this.claim = claim; }
    public String getWhy() { return why; }
    public void setWhy(String why) { this.why = why; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getQuote() { return quote; }
    public void setQuote(String quote) { this.quote = quote; }
    public String getFix() { return fix; }
    public void setFix(String fix) { this.fix = fix; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRuleType() { return ruleType; }
    public void setRuleType(String ruleType) { this.ruleType = ruleType; }
}
