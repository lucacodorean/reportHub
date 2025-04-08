package com.reporthub.service;

import com.reporthub.dto.ReportDTO;
import com.reporthub.entity.Report;
import com.reporthub.request.api.v1.ReportStoreRequest;
import com.reporthub.request.api.v1.ReportUpdateRequest;
import com.reporthub.service.util.IEntityServiceUtil;
import org.springframework.web.multipart.MultipartFile;

public interface IReportService extends
        IEntityService<Report>,
        IEntityServiceUtil<ReportDTO, ReportStoreRequest, ReportUpdateRequest> {

    Response<ReportDTO> attach(String key, MultipartFile file);
}
