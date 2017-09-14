package com.topcom.cms.exception;

import com.topcom.cms.perm.exception.AuthenticationException;
import com.topcom.cms.perm.exception.LoginExceptionHandler;
import com.topcom.cms.perm.exception.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 系统异常处理，比如：404,500
     *
     * @param request
     * @param response
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseData defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        /**
         * 为了ajax能error 能捕获异常，需要跨域
         */
        response.addHeader("Access-Control-Allow-Origin", "*");
        ResponseData r = new ResponseData();
        int code = HttpServletResponse.SC_OK;
        if (e instanceof NoHandlerFoundException) {
            code = HttpServletResponse.SC_NOT_FOUND;
            r.setMessage("资源不存在");
        } else if (e instanceof BusinessException) {
            code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            r.setMessage(e.getMessage());
        } else if (e instanceof UnauthorizedException) {
            code = HttpServletResponse.SC_FORBIDDEN;
            r.setMessage("权限不足");
        } else if (e instanceof AuthenticationException) {
            code = HttpServletResponse.SC_UNAUTHORIZED;
            if (e.getMessage() != null) {
                r.setMessage(e.getMessage());
            } else {
                String message = LoginExceptionHandler.message((AuthenticationException) e);
                r.setMessage(message);

            }
        } else {
            code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            r.setMessage("出错了");
            r.setMessage(e.getMessage());
        }
        r.setData(null);
        r.setStatus(false);
        r.setCode(code);
        response.setStatus(code);
        return r;
    }
}