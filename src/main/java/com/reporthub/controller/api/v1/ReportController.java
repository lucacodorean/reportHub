package com.reporthub.controller.api.v1;

import com.reporthub.dto.ReportDTO;
import com.reporthub.entity.Report;
import com.reporthub.entity.auth.Authenticated;
import com.reporthub.request.api.v1.ReportStoreRequest;
import com.reporthub.request.api.v1.ReportUpdateRequest;
import com.reporthub.service.IReportService;
import com.reporthub.service.ITagService;
import com.reporthub.service.IUserService;
import com.reporthub.singleton.ServiceSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    private final IReportService reportService = ServiceSingleton.getReportService();

    @Autowired
    private final ITagService tagService = ServiceSingleton.getTagService();

    @Autowired
    private final IUserService userService = ServiceSingleton.getUserService();

    // Get all
    @GetMapping("/")
    public ResponseEntity<List<ReportDTO>> index() {
        return ResponseEntity.status(HttpStatus.OK).body(
                reportService.findAll().stream().map(ReportDTO::new).toList()
        );
    }

    // Get by key
    @GetMapping("/{postKey}")
    public ResponseEntity<ReportDTO> get(@PathVariable String postKey) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ReportDTO(reportService.findByKey(postKey))
        );
    }

    @PostMapping("/")
    @PreAuthorize("@authorizationService.isConnected(authentication.principal.id)")
    public ResponseEntity<ReportDTO> create(@RequestBody ReportStoreRequest request) {
        Report report = new Report();
        report.setStatus(request.getStatus());
        report.setTitle(request.getTitle());
        report.setContent(request.getContent());
        report.setDislike_count(0L);
        report.setLike_count(0L);

        report.setUser(userService.findById(
                ((Authenticated)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
        ));

        report.setTags(request.getTags().stream().map(tagService::findByKey).toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReportDTO(reportService.save(report)));
    }

    @PatchMapping("/{key}")
    @PreAuthorize("@authorizationService.canOperateReport(authentication.principal.id, #key)")
    public ResponseEntity<ReportDTO> update(@PathVariable String key, @RequestBody ReportUpdateRequest request) {
        Report report = reportService.findByKey(key);
        if(report == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if(request.getStatus() != null)         report.setStatus(request.getStatus());
        if(request.getTitle() != null)          report.setTitle(request.getTitle());
        if(request.getContent() != null)        report.setContent(request.getContent());
        if(request.getDislikeCount() != null)   report.setDislike_count(request.getDislikeCount());
        if(request.getLikeCount() != null)      report.setLike_count(request.getLikeCount());
        if(request.getTags() != null)
            report.setTags(request.getTags().stream().map(tagService::findByKey).toList());

        report.setUpdated_at(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReportDTO(reportService.save(report)));
    }

    @DeleteMapping("/{key}")
    @PreAuthorize("@authorizationService.canOperateReport(authentication.principal.id, #key)")
    public ResponseEntity<ReportDTO> delete(@PathVariable String key) {
        reportService.delete(reportService.findByKey(key));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
