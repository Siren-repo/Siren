package com.devlop.siren.global.util;

import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ResponseCode.ErrorCode;
import com.devlop.siren.global.exception.GlobalException;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class PermissionInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    HandlerMethod method = (HandlerMethod) handler;
    Permission permission = method.getMethodAnnotation(Permission.class);
    if (permission == null) return true; // Check HandlerMethod

    UserDetailsDto context =
        (UserDetailsDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    UserRole contextRole = context.getUserRole();

    Set<UserRole> annotations = Set.of(permission.role());

    if (annotations.contains(UserRole.ADMIN)) {
      if (contextRole.name().equals(UserRole.ADMIN.name())) {
        log.info("Successfully authenticated as ADMIN");
        return true; // Check @Permission
      }
    }

    if (annotations.contains(UserRole.STAFF)) {
      if (contextRole.name().equals(UserRole.STAFF.name())) {
        log.info("Successfully authenticated as STAFF");
        return true; // Check @Permission
      }
    }

    throw new GlobalException(ErrorCode.INVALID_AUTH);
  }
}
