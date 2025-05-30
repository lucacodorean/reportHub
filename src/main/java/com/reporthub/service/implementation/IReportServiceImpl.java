package com.reporthub.service.implementation;

import com.reporthub.config.AppConfig;
import com.reporthub.dto.ReportDTO;
import com.reporthub.entity.Report;
import com.reporthub.entity.auth.Authenticated;
import com.reporthub.repository.IReportRepository;
import com.reporthub.request.api.v1.ReportStoreRequest;
import com.reporthub.request.api.v1.ReportUpdateRequest;
import com.reporthub.service.IReportService;
import com.reporthub.exception.NotFoundException;
import com.reporthub.service.ITagService;
import com.reporthub.service.IUserService;
import com.reporthub.service.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IReportServiceImpl implements IReportService {

    @Autowired
    private IReportRepository reportRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private ITagService tagService;

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

    @Override
    public Response<ReportDTO> create(ReportStoreRequest request) {
        Map<String, String> message = new HashMap<>();

        try {
            Report report = new Report();
            report.setStatus(request.getStatus());
            report.setTitle(request.getTitle());
            report.setContent(request.getContent());
            report.setDislike_count(0L);
            report.setLike_count(0L);

            report.setUser(userService.findById(
                    ((Authenticated) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
            ));

            report.setTags(
                    request.getTags().stream()
                            .map(tag -> {
                                try { return tagService.findByKey(tag); }
                                catch (NotFoundException e) { throw new RuntimeException(e); }
                            })
                            .filter(Objects::nonNull)
                            .toList()
            );

            return new Response<>(new ReportDTO(this.save(report)), null);
        } catch (NotFoundException ex) { message.put("message", ex.getMessage()); }

        return new Response<>(null, message);
    }

    @Override
    public Response<ReportDTO> update(String key, ReportUpdateRequest request) {
        Map<String, String> message = new HashMap<>();
        try {
            Report report = this.findByKey(key);
            if(report == null) throw new NotFoundException("Report not found");

            if(request.getStatus() != null)         report.setStatus(request.getStatus());
            if(request.getTitle() != null)          report.setTitle(request.getTitle());
            if(request.getContent() != null)        report.setContent(request.getContent());
            if(request.getDislikeCount() != null)   report.setDislike_count(request.getDislikeCount());
            if(request.getLikeCount() != null)      report.setLike_count(request.getLikeCount());
            if(request.getTags() != null)
                report.setTags(
                        request.getTags().stream()
                                .map(tag -> {
                                    try {return tagService.findByKey(tag); }
                                    catch (NotFoundException e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList())
                );

            report.setUpdated_at(LocalDateTime.now());
            return new Response<>(new ReportDTO(this.save(report)), message);
        } catch (NotFoundException ex) { message.put("message", ex.getMessage()); }
        return new Response<>(null, message);
    }

    private String retrieveFilePath(MultipartFile file) {
        try {
            Path uploadPath = Paths.get(AppConfig.getAttachmentPath());
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            String extension =  Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
            Path filePath = uploadPath.resolve(UUID.randomUUID() + extension);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Response<ReportDTO> attach(String key, MultipartFile file) {
        Map<String, String> message = new HashMap<>();

        try {
            Report report = this.findByKey(key);
            if(report != null) report.setAttachment(this.retrieveFilePath(file));
            return new Response<>(new ReportDTO(this.save(report)), message);
        } catch (NotFoundException ex) { message.put("message", "Report not fund"); }

        return new Response<>(null, message);
    }

    @Override
    public List<ReportDTO> all() {
        return this.findAll().stream().map(ReportDTO::new).collect(Collectors.toList());
    }

    @Override
    public Response<ReportDTO> retrieveDTO(String key)  {
        Map<String, String> message = new HashMap<>();
        try {
            Report report = this.findByKey(key);
            if (report == null) throw new NotFoundException("Report not found");
            return new Response<>(new ReportDTO(report), null);
        } catch (NotFoundException e) { message.put("message", "Report not found"); }
        return new Response<>(null, message);
    }
}
