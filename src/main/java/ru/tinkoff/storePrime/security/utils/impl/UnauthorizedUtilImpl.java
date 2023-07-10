package ru.tinkoff.storePrime.security.utils.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.tinkoff.storePrime.dto.exception.ExceptionDto;
import ru.tinkoff.storePrime.exceptions.ExceptionMessages;
import ru.tinkoff.storePrime.security.utils.UnauthorizedUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class UnauthorizedUtilImpl implements UnauthorizedUtil {

    public void createUnauthorizedAnswer(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ExceptionDto exceptionDto = ExceptionDto.builder()
                .message("Authorization failed")
                .status(HttpStatus.UNAUTHORIZED.value())
                .serviceMessage(ExceptionMessages.UNAUTHORIZED)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String exceptionDtoJson = objectMapper.writeValueAsString(exceptionDto);

        PrintWriter writer = response.getWriter();
        writer.println(exceptionDtoJson);
    }

}
