package com.jselva.nisum.evaluationtest.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneDto {

    private String number;
    private String cityCode;
    private String countryCode;

    public PhoneDto() {
    }

    public PhoneDto(String number, String cityCode, String countryCode) {
        this.number = number;
        this.cityCode = cityCode;
        this.countryCode = countryCode;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCityCode() {
        return this.cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneDto)) return false;

        var phoneDto = (PhoneDto) o;

        if (!Objects.equals(number, phoneDto.number)) return false;
        if (!Objects.equals(cityCode, phoneDto.cityCode)) return false;
        return Objects.equals(countryCode, phoneDto.countryCode);
    }

    @Override
    public int hashCode() {
        var result = number != null ? number.hashCode() : 0;
        result = 31 * result + (cityCode != null ? cityCode.hashCode() : 0);
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        return result;
    }
}