package eng.java.project.main.controller;

import eng.java.project.entity.hospital.core.*;
import eng.java.project.entity.hospital.util.EditInfo;
import eng.java.project.main.GlobalUser;
import eng.java.project.main.controller.entity.view.DoctorController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EditHistoryController {
    public TableColumn<EditInfo<CoreObject, GlobalUser>, String> entityColumn;
    public TableColumn<EditInfo<CoreObject, GlobalUser>, String> oldDataColumn;
    public TableColumn<EditInfo<CoreObject, GlobalUser>, String> newDataColumn;
    public TableColumn<EditInfo<CoreObject, GlobalUser>, String> userColumn;
    public TableColumn<EditInfo<CoreObject, GlobalUser>, String> roleColumn;
    public TableColumn<EditInfo<CoreObject, GlobalUser>, String> dateColumn;
    public TableColumn<EditInfo<CoreObject, GlobalUser>, String> timeColumn;
    public TableView<EditInfo<CoreObject, GlobalUser>> editHistoryTableView;

    private static final Logger logger = LoggerFactory.getLogger(EditHistoryController.class);
    private List<EditInfo<CoreObject, GlobalUser>> editInfoList = new ArrayList<>();
    private static final String SERIALIZED_FILE_NAME_DOCTOR = "files\\edited_doctor_object.dat";
    private static final String SERIALIZED_FILE_NAME_PATIENT = "files\\edited_patient_object.dat";

    public void initialize() {
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(SERIALIZED_FILE_NAME_DOCTOR))) {
            try {
                EditInfo<CoreObject, GlobalUser> object = (EditInfo<CoreObject, GlobalUser>) in.readObject();
                editInfoList.add(object);
            } catch (EOFException ex) {
                String msg = "Reached end of file when deserializing 'EditInfo' objects";
                logger.info(msg, ex);
            }
        } catch (IOException | ClassNotFoundException ex) {
            String msg = "Error occurred while attempting deserialization of 'EditInfo' object";
            logger.error(msg, ex);
        }

        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(SERIALIZED_FILE_NAME_PATIENT))) {
            try {
                EditInfo<CoreObject, GlobalUser> object = (EditInfo<CoreObject, GlobalUser>) in.readObject();
                editInfoList.add(object);
            } catch (EOFException ex) {
                String msg = "Reached end of file when deserializing 'EditInfo' objects";
                logger.info(msg, ex);
            }
        } catch (IOException | ClassNotFoundException ex) {
            String msg = "Error occurred while attempting deserialization of 'EditInfo' object";
            logger.error(msg, ex);
        }

        entityColumn.setCellValueFactory(data -> {
            EditInfo<CoreObject, GlobalUser> object = data.getValue();
            if (object.getAfterEditObject() instanceof Doctor doctor) {
                return new SimpleStringProperty(doctor.getTitle() + " " + doctor.getName() + " " + doctor.getSurname());
            } else if (object.getAfterEditObject() instanceof Patient patient) {
                return new SimpleStringProperty(patient.getName() + " " + patient.getSurname());
            } else if (object.getAfterEditObject() instanceof MedicalDevice medicalDevice) {
                return new SimpleStringProperty(medicalDevice.getName() + " " + medicalDevice.getModelNumber());
            } else if (object.getAfterEditObject() instanceof Department department) {
                return new SimpleStringProperty(department.getName());
            } else {
                return null;
            }
        });

        oldDataColumn.setCellValueFactory(data -> new SimpleStringProperty(String.join("\n", data.getValue().getPreEditList())));
        newDataColumn.setCellValueFactory(data -> new SimpleStringProperty(String.join("\n", data.getValue().getAfterEditList())));


        userColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getUsername()));
        roleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getRole()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTime().format(DateTimeFormatter.ofPattern("HH:mm"))));

        if(editInfoList.size() > 0) {
           editHistoryTableView.setItems(FXCollections.observableList(editInfoList));
        }
    }
}
