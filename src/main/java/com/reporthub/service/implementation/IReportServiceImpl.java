package com.reporthub.service.implementation;

import com.reporthub.entity.Report;
import com.reporthub.repository.IReportRepository;
import com.reporthub.service.IReportService;
import exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IReportServiceImpl implements IReportService {

    @Autowired
    private IReportRepository reportRepository;

    public Report save(Report entity)  {
        return reportRepository.save(entity);
    }

    public Report findById(Long id) throws NotFoundException {
        Optional<Report> report = reportRepository.findById(id);
        if(report.isPresent()) return report.get();
        else throw new NotFoundException("Report not found");
    }

    public Report findByKey(String key) throws NotFoundException {
        Optional<Report> report = reportRepository.findByPostKey(key);
        if(report.isPresent()) return report.get();
        else throw new NotFoundException("Report not found");
    }

    public List<Report> findAll() { return reportRepository.findAll(); }

    public boolean delete(Report entity) throws NotFoundException {
        if(!reportRepository.existsById(entity.getId())) throw new NotFoundException("Report not found");
        reportRepository.delete(entity);
        return true;
    }
}
