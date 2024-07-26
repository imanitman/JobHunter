package vn.hoidanit.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.response.RestResponse;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    // dùng để handle những trường hợp nào sẽ cần format data trả về
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
        // tất cả các trường hợp
    }

    @Override
    public Object beforeBodyWrite(
        Object body, 
        MethodParameter returnType, 
        MediaType selectedContentType,
        Class selectedConverterType, 
        ServerHttpRequest request, 
        ServerHttpResponse response) {
        HttpServletResponse responseFormat = ((ServletServerHttpResponse) response).getServletResponse();
        int status = responseFormat.getStatus();
        
        RestResponse<Object> restResponse = new RestResponse<Object>();
        if (body instanceof String || body instanceof Resource){
            return body;
        }
        if(status >= 400){
            return(body);
        }
        else{
            ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
            restResponse.setMessage(message != null ? message.value() : "Call Api Successfully");
            restResponse.setData(body);
        }
        return restResponse;
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'beforeBodyWrite'");
    }
    
}
