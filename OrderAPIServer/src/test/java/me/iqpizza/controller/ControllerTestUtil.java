package me.iqpizza.controller;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class ControllerTestUtil {

    private static MockHttpServletRequestBuilder method(HttpMethod method, String uri) {
        switch (method) {
            case POST: return post(uri);
            case GET: return get(uri);
            case PATCH: return patch(uri);
            case DELETE: return delete(uri);
            case PUT: return put(uri);
            case HEAD: return head(uri);
            default: return options(uri);
        }
    }

    public static ResultActions resultActions(MockMvc mockMvc, String uri,
                                              String content, HttpMethod httpMethod,
                                              String token) throws Exception {
        return mockMvc.perform(
                method(httpMethod, uri)
                        .content((content == null) ? "" : content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );
    }
}
