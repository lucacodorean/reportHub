package com.reporthub.controller.api.v1;

import com.reporthub.dto.ReportDTO;
import com.reporthub.request.api.v1.ReportStoreRequest;
import com.reporthub.request.api.v1.ReportUpdateRequest;
import com.reporthub.service.IReportService;
import com.reporthub.exception.NotFoundException;
import com.reporthub.service.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired private IReportService reportService;

    @GetMapping("/")
    public ResponseEntity<List<ReportDTO>> index() {
        return ResponseEntity.status(HttpStatus.OK).body(reportService.all());
    }

    @GetMapping("/{postKey}")
    public ResponseEntity<?> get(@PathVariable String postKey) {
        Response<ReportDTO> myResponse = reportService.retrieveDTO(postKey);
        if(myResponse.getEntityDTO() != null) return ResponseEntity.ok(myResponse.getEntityDTO());
        else return ResponseEntity.badRequest().body(myResponse.retrieveMessages());
    }

    @PostMapping("/")
    @PreAuthorize("@authorizationService.isConnected(authentication.principal.id)")
    public ResponseEntity<?> create(@RequestBody ReportStoreRequest request) {
        Response<ReportDTO> myResponse = reportService.create(request);
        if(myResponse.getEntityDTO() != null) return ResponseEntity.ok(myResponse.getEntityDTO());
        else return ResponseEntity.badRequest().body(myResponse.retrieveMessages());
    }

    @PostMapping("/{key}/attachment")
    @PreAuthorize("@authorizationService.canOperateReport(authentication.principal.id, #key)")
    public ResponseEntity<?> attach(@PathVariable String key, @RequestPart("file") MultipartFile file) {
        Response<ReportDTO> myResponse = reportService.attach(key, file);
        if(myResponse.getEntityDTO() != null) return ResponseEntity.ok(myResponse.getEntityDTO());
        else return ResponseEntity.badRequest().body(myResponse.retrieveMessages());
    }

    @PatchMapping("/{key}")
    @PreAuthorize("@authorizationService.canOperateReport(authentication.principal.id, #key)")
    public ResponseEntity<?> update(@PathVariable String key, @RequestBody ReportUpdateRequest request) {
        Response<ReportDTO> myResponse = reportService.update(key,request);
        if(myResponse.getEntityDTO() != null) return ResponseEntity.ok(myResponse.getEntityDTO());
        else return ResponseEntity.badRequest().body(myResponse.retrieveMessages());
    }

    @DeleteMapping("/{key}")
    @PreAuthorize("@authorizationService.canOperateReport(authentication.principal.id, #key)")
    public ResponseEntity<ReportDTO> delete(@PathVariable String key) {
        try {
            reportService.delete(reportService.findByKey(key));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotFoundException ex) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); }
    }
}
