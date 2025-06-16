package com.emiteai.api.exception;

import java.util.List;

public record ErrorResponse(int status,
                            String message,
                            List<String> details) { }
