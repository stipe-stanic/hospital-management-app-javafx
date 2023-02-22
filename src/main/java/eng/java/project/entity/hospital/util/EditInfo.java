package eng.java.project.entity.hospital.util;

import eng.java.project.entity.hospital.core.Doctor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class EditInfo<T, R> implements Serializable {
    T afterEditObject;
    R user;
    List<String> preEditList;
    List<String> afterEditList;
    LocalDate date;
    LocalTime time;


    public EditInfo(T afterEditObject, R user, List<String> preEditList, List<String> afterEditList, LocalDate date, LocalTime time) {
        this.afterEditObject = afterEditObject;
        this.user = user;
        this.preEditList = preEditList;
        this.afterEditList = afterEditList;
        this.date = date;
        this.time = time;
    }

    public T getAfterEditObject() {
        return afterEditObject;
    }

    public void setAfterEditObject(T afterEditObject) {
        this.afterEditObject = afterEditObject;
    }

    public R getUser() {
        return user;
    }

    public void setUser(R user) {
        this.user = user;
    }

    public List<String> getPreEditList() {
        return preEditList;
    }

    public void setPreEditList(List<String> preEditList) {
        this.preEditList = preEditList;
    }

    public List<String> getAfterEditList() {
        return afterEditList;
    }

    public void setAfterEditList(List<String> afterEditList) {
        this.afterEditList = afterEditList;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
