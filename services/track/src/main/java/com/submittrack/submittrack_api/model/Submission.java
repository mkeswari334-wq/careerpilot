package com.submittrack.submittrack_api.model;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;

@Entity
@Table(name = "submissions")
public class Submission {

    public Submission() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vendor is required")
    private String vendor;          // e.g. infotech

    @NotBlank(message = "Client is required")
    private String client;          // e.g. Wells Fargo

    @NotBlank(message = "Role is required")
    private String role;            // e.g. Senior react + angular developer

    @NotNull(message = "Rate is required")
    @Positive(message = "Rate must be greater than 0")
    private Double rate;            // e.g. 60.0(per hour)

    @NotBlank(message = "Employment Type is required")
    private String employmentType; // c2c or w2

    @NotBlank(message = "Status is required")
    private String status;          // Submitted, interview, offer, Rejected

    public Submission(Long id, String vendor, String client, String role, Double rate, String employmentType, String status) {
        this.id = id;
        this.vendor = vendor;
        this.client = client;
        this.role = role;
        this.rate = rate;
        this.employmentType = employmentType;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getVendor() {
        return vendor;
    }

    public String getClient() {
        return client;
    }

    public String getRole() {
        return role;
    }

    public Double getRate() {
        return rate;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


