package com.lzq.myblog.entity;

/**
 * 博文分类枚举
 */
public enum Category {
    CTF("CTF"),
    LEARN("Learn"),
    SOMETHING("Something");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Category fromValue(String value) {
        for (Category category : Category.values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + value);
    }
}
