package com.mindskip.xzs.service.enums;

import java.util.HashMap;
import java.util.Map;

public enum CourseStatusEnum {

    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    int code;
    String name;

    CourseStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, CourseStatusEnum> keyMap = new HashMap<>();

    static {
        for (CourseStatusEnum item : CourseStatusEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    public static CourseStatusEnum fromCode(Integer code) {
        return keyMap.get(code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
} 