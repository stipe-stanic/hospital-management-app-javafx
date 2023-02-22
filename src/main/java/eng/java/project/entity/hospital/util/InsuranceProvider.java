package eng.java.project.entity.hospital.util;

import eng.java.project.entity.Entity;
import eng.java.project.entity.hospital.core.Patient;

import java.time.LocalDate;
import java.util.List;

public class InsuranceProvider extends Entity {
    private String name;
    private String adress;
    private String phoneNumber;
    private LocalDate expirationDate;

    public InsuranceProvider(Long id, String name, String adress, String phoneNumber, LocalDate expirationDate) {
        super(id);
        this.name = name;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getExpireDate() {
        return expirationDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expirationDate = expireDate;
    }
}
