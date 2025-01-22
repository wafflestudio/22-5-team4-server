package com.wafflestudio.interpark.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.security.access.AccessDeniedException

@Component
class RestAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        response.status = HttpServletResponse.SC_FORBIDDEN // 403
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(
            """
            {
                "error": "Access Denied",
                "message": "${accessDeniedException.message}",
                "path": "${request.requestURI}"
            }
            """.trimIndent()
        )
    }
}
