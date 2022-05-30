package com.walletapi.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum DirectionEnum {
    IN(1, "Credit transaction"),
    OUT(2, "Withdraw transaction"),
    OTHER(0, "Undefined");

    private Integer code;
    private String description;

    public static DirectionEnum of(Integer code) {
        return Arrays.stream(DirectionEnum.values())
                .filter(c -> Objects.equals(c.code, code))
                .findFirst()
                .orElse(DirectionEnum.OTHER);
    }
}