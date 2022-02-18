package com.jselva.nisum.evaluationtest.data.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorApi {
    private int code;
    private String messageError;

    public ErrorApi() {
    }

    public ErrorApi(int code, String messageError) {
        this.code = code;
        this.messageError = messageError;
    }

    public int getCode() {
        return code;
    }

    public ErrorApi setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

}
