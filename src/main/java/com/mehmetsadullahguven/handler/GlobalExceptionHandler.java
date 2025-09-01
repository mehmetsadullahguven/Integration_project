package com.mehmetsadullahguven.handler;

import com.mehmetsadullahguven.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<ApiError> handleBaseException(BaseException e, WebRequest request)
    {
        return ResponseEntity.badRequest().body(createdApiError(e.getMessage(), request));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError<Map<String, List<String>>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request)
    {
        Map<String, List<String>> map = new HashMap<>();
        for (ObjectError objectError : e.getBindingResult().getFieldErrors()) {
            String fieldName = ((FieldError) objectError).getField();
            if (map.containsKey(fieldName)) {
                map.put(fieldName, addValue(map.get(fieldName), objectError.getDefaultMessage()));
            }else {
                map.put(fieldName, addValue(new ArrayList<>(), objectError.getDefaultMessage()));
            }
        }
        return ResponseEntity.badRequest().body(createdApiError(map, request));
    }

    private List<String> addValue(List<String> list, String value)
    {
        list.add(value);
        return list;
    }

    public <E> ApiError<E> createdApiError(E message, WebRequest request)
    {
        Exception<E> exception = new Exception<>();
        exception.setCreatedTime(new Date());
        exception.setHostName(this.getHostName());
        exception.setPath(request.getDescription(false).substring(4));
        exception.setMessage(message);

        ApiError<E> apiError = new ApiError<>();
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setException(exception);

        return apiError;
    }

    private String getHostName()
    {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {}
        return "Unknown Host";
    }
}
