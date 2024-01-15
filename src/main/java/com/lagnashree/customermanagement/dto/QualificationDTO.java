package com.lagnashree.customermanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QualificationDTO {
    private String name;
    private Qualifications qualifications;

    @Data
    @Builder
    public static class Qualifications {
        private School school;
        private College college;
    }

    @Data
    @Builder
    public static class School {
        private String name;
        private String address;
    }

    @Data
    @Builder
    public static class College {
        private String name;
        private String address;
        private String degree;
    }
}
