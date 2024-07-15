package org.example.securiteback.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permissions {
    USER_READ("USER_READ"),
    USER_CREATE("USER_CREATE"),
    USER_UPDATE("USER_UPDATE"),
    USER_DELETE("USER_DELETE");

    private final String name;
}