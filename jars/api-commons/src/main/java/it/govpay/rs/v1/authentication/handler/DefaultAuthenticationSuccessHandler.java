package it.govpay.rs.v1.authentication.handler;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.govpay.rs.v1.converter.JacksonJsonProvider;

public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
            ByteArrayInputStream bais = null;
            ServletOutputStream outputStream = null;
            try{
                    Response response = getPayload(request, res, authentication);
                    res.setStatus(response.getStatus());

//                    MultivaluedMap<String, Object> headers = response.getHeaders();
//                    if(!headers.isEmpty()) {
//                            Set<String> keySet = headers.keySet();
//
//                            for (String headerKey : keySet) {
//                                    List<Object> list = headers.get(headerKey);
//                                    if(!list.isEmpty()) {
//                                            StringBuilder sb = new StringBuilder();
//                                            for (Object object : list) {
//                                                    if(sb.length() > 0)
//                                                            sb.append(", ");
//
//                                                    sb.append(object);
//                                            }
//                                            res.setHeader(headerKey, sb.toString());
//                                    }
//                            }
//                    }
                    
                    ObjectMapper mapper = JacksonJsonProvider.getObjectMapper();
                    String operatoreJson = mapper.writeValueAsString(response.getEntity());
                    bais = new ByteArrayInputStream(operatoreJson.getBytes());

                    outputStream = res.getOutputStream();

                    IOUtils.copy(bais, outputStream);

                    outputStream.flush();
            }catch(Exception e) {

            } finally {
                    if(bais!= null) {
                            try {
                                    bais.close();
                            } catch (IOException e) {
                            }
                    }
            }
    }

    public Response getPayload(HttpServletRequest request, HttpServletResponse res, Authentication authentication) {
            return Response.status(HttpServletResponse.SC_OK).build();
    }
}
