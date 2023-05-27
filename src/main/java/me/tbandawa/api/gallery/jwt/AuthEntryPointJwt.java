package me.tbandawa.api.gallery.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.tbandawa.api.gallery.responses.ErrorResponse;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    logger.error("Unauthorized error: {}", authException.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    
    ErrorResponse errorResponse = new ErrorResponse.ErrorResponseBuilder()
            .withStatus(HttpServletResponse.SC_UNAUTHORIZED)
            .withError("Unauthorized")
            .withMessages((List<String>) Arrays.asList(new String[] {authException.getMessage()}))
            .build();

    final ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), errorResponse);
  }

}
