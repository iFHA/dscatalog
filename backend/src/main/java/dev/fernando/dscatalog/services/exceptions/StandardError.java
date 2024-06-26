package dev.fernando.dscatalog.services.exceptions;

import java.time.Instant;

public record StandardError(Integer statusCode, String message, Instant timestamp, String path) {
}
