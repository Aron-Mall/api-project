package com.armal.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "form")
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqFormId")
    @SequenceGenerator(name = "seqFormId", sequenceName = "seq_form_id", allocationSize = 1)
    @Column(name = "id")
    private Long formId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_personal_detail_id", referencedColumnName = "id")
    private PersonalDetails personalDetails;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_address_id", referencedColumnName = "id")
    private Address address;

    @JsonIgnore
    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "finalized")
    private boolean finalized;

    public Form() {}

    public Form(Long formId, PersonalDetails personalDetails, Address address,String sessionId, boolean finalized) {
        this.formId = formId;
        this.personalDetails = personalDetails;
        this.address = address;
        this.sessionId = sessionId;
        this.finalized = finalized;
    }


    public Long getFormId() {
        return formId;
    }

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public Address getAddress() {
        return address;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Form form = (Form) o;
        return finalized == form.finalized && Objects.equals(formId, form.formId) && Objects.equals(personalDetails, form.personalDetails) && Objects.equals(address, form.address);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(formId);
        result = 31 * result + Objects.hashCode(personalDetails);
        result = 31 * result + Objects.hashCode(address);
        result = 31 * result + Boolean.hashCode(finalized);
        return result;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
