package com.ranjan.somiq.core.consts

enum class ValidationError {
    // Field-specific validation errors
    NAME_REQUIRED,
    NAME_TOO_SHORT,
    EMAIL_REQUIRED,
    INVALID_EMAIL,

    // General or API-related errors that might be mapped
    // from backend error codes or unexpected client-side issues.
    // These correspond to "something_went_wrong" and "email_already_in_use"
    SOMETHING_WENT_WRONG,
    EMAIL_ALREADY_IN_USE,

    // You might add more specific API errors here later if needed
    // NETWORK_ERROR,
    // SERVER_UNAVAILABLE,
    // UNAUTHORIZED,
    // ...
}