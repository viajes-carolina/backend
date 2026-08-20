package com.viajescarolina.api.common.audit;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marca el método execute() de un caso de uso de escritura para que AuditInterceptor
 * registre automáticamente una entrada en audit_log tras una ejecución exitosa.
 * action/entityType son @Nonbinding: no participan en la resolución de tipo de CDI,
 * así que un único AuditInterceptor cubre todas las combinaciones de valores.
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Audited {

    @Nonbinding
    String action();

    @Nonbinding
    String entityType();
}
