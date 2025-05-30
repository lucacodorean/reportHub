    package com.reporthub;
    
    import com.reporthub.entity.Report;
    import com.reporthub.entity.User;
    import com.reporthub.repository.IReportRepository;
    import com.reporthub.repository.IUserRepository;
    import org.junit.jupiter.api.*;
    import org.junit.jupiter.api.extension.ExtendWith;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.springframework.test.context.TestPropertySource;
    import org.springframework.test.context.junit.jupiter.SpringExtension;
    
    import java.util.List;
    import java.util.Optional;
    
    import static org.junit.jupiter.api.Assertions.*;
    
    @DataJpaTest
    @ExtendWith(SpringExtension.class)
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @TestPropertySource(properties = {
            "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS REPORTHUB",
            "spring.datasource.driver-class-name=org.h2.Driver",
            "spring.datasource.username=sa",
            "spring.datasource.password=",
            "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
            "spring.jpa.hibernate.ddl-auto=create-drop"
    })
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    public class ReportRepositoryTests {
    
        @Autowired
        private IReportRepository reportRepository;
    
        @Autowired
        private IUserRepository userRepository;
    
        private User testUser;

        @BeforeEach
        void setUp() {
            testUser = userRepository.save(new User("testUser", "test@example.com", "password123", "1234567890"));
        }
    
        @AfterEach
        void tearDown() {
            reportRepository.deleteAll();
        }
    
        private Report createReport(String title, String content) {
            return new Report(content, testUser, title, null);
        }
    
        @Test
        public void testSaveReport() {
            Report report = createReport("Test Report", "Some content");
            Report saved = reportRepository.save(report);
            assertNotNull(saved);
            assertNotNull(saved.getId());
            assertEquals(report.getTitle(), saved.getTitle());
        }
    
        @Test
        public void testFindById() {
            Report report = createReport("Find Me", "Some content");
            Report saved = reportRepository.save(report);
            Optional<Report> found = reportRepository.findById(saved.getId());
            assertTrue(found.isPresent());
            assertEquals(saved.getTitle(), found.get().getTitle());
        }
    
        @Test
        public void testFindByKey() {
            Report report = createReport("Key Report", "Key Content");
            reportRepository.save(report);
            Optional<Report> found = reportRepository.findByPostKey(report.getPost_key());
            assertTrue(found.isPresent());
            assertEquals(report.getPost_key(), found.get().getPost_key());
        }
    
        @Test
        public void testFindAllReports() {
            reportRepository.saveAll(List.of(
                    createReport("Report 1", "Content 1"),
                    createReport("Report 2", "Content 2"),
                    createReport("Report 3", "Content 3")
            ));
            List<Report> reports = reportRepository.findAll();
            assertEquals(3, reports.size());
        }
    
        @Test
        public void testDeleteReport() {
            Report report = createReport("To Be Deleted", "Delete Content");
            Report saved = reportRepository.save(report);
            reportRepository.delete(saved);
            Optional<Report> deleted = reportRepository.findById(saved.getId());
            assertTrue(deleted.isEmpty());
        }
    }
    
