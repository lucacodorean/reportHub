package com.reporthub;

import com.reporthub.entity.Report;
import com.reporthub.entity.User;
import com.reporthub.repository.IReportRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReportRepositoryTests {

    @Autowired
    private IReportRepository reportRepository;

    private static final User dummyUser = new User("testUser", "test@example.com", "password123", "1234567890");

    private static Stream<Report> provideReports() {
        return Stream.of(
                new Report("Basic report content here", dummyUser, "Test Report 1", null),
                new Report("Another report for testing", dummyUser, "Test Report 2", null),
                new Report("System crash bug report", dummyUser, "Critical Bug Report", null)
        );
    }

    @BeforeEach
    void setUp() {
        reportRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        reportRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("provideReports")
    public void testSaveReport(Report report) {
        Report saved = reportRepository.save(report);
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(report.getTitle(), saved.getTitle());
    }

    @ParameterizedTest
    @MethodSource("provideReports")
    public void testFindById(Report report) {
        Report saved = reportRepository.save(report);
        Optional<Report> found = reportRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getTitle(), found.get().getTitle());
    }

    @ParameterizedTest
    @MethodSource("provideReports")
    public void testFindByKey(Report report) {
        reportRepository.save(report);
        Optional<Report> found = reportRepository.findByPostKey(report.getPost_key());
        assertTrue(found.isPresent());
        assertEquals(report.getPost_key(), found.get().getPost_key());
    }

    @Test
    public void testFindAllReports() {
        reportRepository.saveAll(provideReports().toList());
        List<Report> reports = reportRepository.findAll();
        assertEquals(3, reports.size());
    }

    @ParameterizedTest
    @MethodSource("provideReports")
    public void testDeleteReport(Report report) {
        Report saved = reportRepository.save(report);
        reportRepository.delete(saved);
        Optional<Report> deleted = reportRepository.findById(saved.getId());
        assertTrue(deleted.isEmpty());
    }
}

