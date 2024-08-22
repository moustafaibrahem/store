package com.mibr.store.service;

import com.mibr.store.data.history.History;
import com.mibr.store.data.history.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    public List<History> getHistoryByCategoryId(Long categoryId) {
        return historyRepository.findByCategoryId(categoryId);
    }

    public History addHistoryRecord(History history) {
        return historyRepository.save(history);
    }
}
