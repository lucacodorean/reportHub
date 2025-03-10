package com.reporthub.controller.api.v1;

import com.reporthub.dto.ReportDTO;
import com.reporthub.entity.Report;
import com.reporthub.entity.User;
import com.reporthub.service.IReportService;
import com.reporthub.singleton.ServiceSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    private final IReportService reportService = ServiceSingleton.getReportService();

    // Get all
    @GetMapping("/")
    public ResponseEntity<List<ReportDTO>> index() {
        System.out.println("Hello!!!!");
        return ResponseEntity.status(HttpStatus.OK).body(
                reportService.findAll().stream().map(ReportDTO::new).toList()
        );
    }

    //TODO: implement adding reports
    // @PostMapping("/")
    //public ResponseEntity<ReportDTO> addReport(@RequestBody ReportDTO reportDTO) {
    //}

    // Get by key
    @GetMapping("/{postKey}")
    public ResponseEntity<ReportDTO> get(@PathVariable String postKey) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ReportDTO(reportService.findByKey(postKey))
        );
    }

    // DeleteKey
    @DeleteMapping("delete/{postKey}")
    public ResponseEntity<Void> delete(@PathVariable String postKey) {
        reportService.delete(reportService.findByKey(postKey));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
