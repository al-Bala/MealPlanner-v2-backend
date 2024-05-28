package pl.mealplanner;

import com.mealplannerv2.MealPlannerV2Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@SpringBootTest(classes = MealPlannerV2Application.class)
@ActiveProfiles("integration")
@AutoConfigureMockMvc
@Testcontainers
public class BaseIntegrationTest {

    @Autowired
    public MockMvc mockMvc;

    @Container
    public static final GenericContainer<?> mongoDBContainer = new GenericContainer<>(DockerImageName.parse("mongo:4.0.10"))
            .withExposedPorts(27017)
            .withCopyFileToContainer(MountableFile.forClasspathResource("./test-data.js"), "/docker-entrypoint-initdb.d/init-script.js");

    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry){
        registry.add("spring.data.mongodb.uri", () -> "mongodb://" + mongoDBContainer.getHost() + ":" + mongoDBContainer.getFirstMappedPort());
        registry.add("spring.data.mongodb.database", () -> "testDatabase");
    }
}
