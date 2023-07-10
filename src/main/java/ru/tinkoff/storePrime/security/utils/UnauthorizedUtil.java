package ru.tinkoff.storePrime.security.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UnauthorizedUtil {

    void createUnauthorizedAnswer(HttpServletResponse response) throws IOException;

}
