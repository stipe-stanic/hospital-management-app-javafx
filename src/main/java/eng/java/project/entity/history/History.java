package eng.java.project.entity.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class History<T extends Record> implements Serializable {
    private List<T> historyList;

    public History() {
        this.historyList = new ArrayList<>();
    }

    public void addHistoryObject(T historyObject) {
        historyList.add(historyObject);
    }

    public T getHistoryObject(Integer index) {
        return historyList.get(index);
    }

    public List<T> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<T> historyList) {
        this.historyList = historyList;
    }
}
