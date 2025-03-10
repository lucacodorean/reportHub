package com.reporthub.service;

import com.reporthub.entity.Postable;
import com.reporthub.entity.Report;

import java.util.List;

public interface IReportService extends IEntityService<Report> {
    Report save(Report entity);
    Report findById(Long id);
    Report findByKey(String key);
    List<Report> findAll();
    boolean delete(Report post);
}
