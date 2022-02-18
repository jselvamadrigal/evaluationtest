package com.jselva.nisum.evaluationtest.data.models;

public enum ErrorApiCodes {
    ERROR_INTERNAL_SERVER("El servicio no responde correctamente, comuniquese con soporte.");

    final String message;

    ErrorApiCodes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
