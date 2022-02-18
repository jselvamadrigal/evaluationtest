package com.jselva.nisum.evaluationtest.data.models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseGenericApi<T> {

    @JsonProperty("message")
    private T data;
    private ErrorApi error;

    public ResponseGenericApi() {
    }

    public ResponseGenericApi(T data) {
        this.data = data;
    }

    public ResponseGenericApi(ErrorApi error) {
        this.data = null;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorApi getError() {
        return error;
    }

    public void setError(ErrorApi error) {
        this.error = error;
    }

}
