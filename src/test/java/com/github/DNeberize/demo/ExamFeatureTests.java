package com.github.DNeberize.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:worde-exam;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class ExamFeatureTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthEndpointIsAccessible() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    void adminEndpointRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/admin/status"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void localeCanBeChangedViaQueryParameter() throws Exception {
        mockMvc.perform(get("/?lang=ka"))
                .andExpect(status().isOk());
    }
}
