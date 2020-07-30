package com.study.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ResponseUtils {
        public static void write(HttpServletResponse response, Object o)throws Exception{
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out=response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            out.println(mapper.writeValueAsString(o));
            out.flush();
            out.close();
        }
}
