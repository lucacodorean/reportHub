package com.reporthub.service.implementation;

import com.reporthub.entity.Report;
import com.reporthub.repository.IReportRepository;
import com.reporthub.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IReportServiceImpl implements IReportService {

    @Autowired
    private IReportRepository reportRepository;

    public Report save(Report entity) {
        return reportRepository.save(entity);
    }

    public Report findById(Long id) { return reportRepository.findById(id).orElse(null); }

    public Report findByKey(String key) { return reportRepository.findByPostKey(key).orElse(null); }

    public List<Report> findAll() { return reportRepository.findAll(); }

    public boolean delete(Report entity) {
        if(!reportRepository.existsById(entity.getId())) return false;

        reportRepository.delete(entity);
        return true;
    }

    public List<Report> loadReportsByTagName(String tagName) {
        return reportRepository.findByTagName(tagName);
    }
}
