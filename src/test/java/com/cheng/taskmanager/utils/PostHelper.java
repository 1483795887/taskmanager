package com.cheng.taskmanager.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cheng.taskmanager.bean.ResultBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class PostHelper {
    private MockMvc mockMvc;

    public PostHelper(WebApplicationContext context) {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    public JSONObject postDate(String url, Object object) throws Exception {
        String jsonStr = JSON.toJSONString(object);
        MvcResult result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStr)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String string = result.getResponse().getContentAsString();
        return JSONObject.parseObject(string);
    }

    public void checkFailed(String url, Object object) throws Exception {
        JSONObject map = postDate(url, object);
        ResultBean bean1 = map.getJSONObject("result").toJavaObject(ResultBean.class);
        assertNotNull(bean1);
        assertEquals(bean1.getCode(), ResultBean.FAILED);
    }

    public void checkSucceed(JSONObject map) {
        ResultBean bean1 = map.getJSONObject("result").toJavaObject(ResultBean.class);
        assertNotNull(bean1);
        assertEquals(bean1.getCode(), ResultBean.SUCCESS);
    }

    public void checkBadRequest(String url, Object obj) throws Exception {
        mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(obj)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
