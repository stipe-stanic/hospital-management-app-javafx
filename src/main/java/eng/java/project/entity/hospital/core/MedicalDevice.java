package eng.java.project.entity.hospital.core;

import eng.java.project.entity.Entity;

import java.time.LocalDate;

public class MedicalDevice extends Entity implements CoreObject {
    private String name;
    private String manufacturerName;
    private Integer modelNumber;
    private Long serialNumber;
    private String typeOfDevice;
    private LocalDate dateOfPurchase;
    private LocalDate expirationDate;

    public MedicalDevice(Long id, String name, String manufacturerName, Integer modelNumber, Long serialNumber, String typeOfDevice, LocalDate dateOfPurchase, LocalDate expirationDate) {
        super(id);
        this.name = name;
        this.manufacturerName = manufacturerName;
        this.modelNumber = modelNumber;
        this.serialNumber = serialNumber;
        this.typeOfDevice = typeOfDevice;
        this.dateOfPurchase = dateOfPurchase;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public Integer getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(Integer modelNumber) {
        this.modelNumber = modelNumber;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTypeOfDevice() {
        return typeOfDevice;
    }

    public void setTypeOfDevice(String typeOfDevice) {
        this.typeOfDevice = typeOfDevice;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(LocalDate dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }
}
