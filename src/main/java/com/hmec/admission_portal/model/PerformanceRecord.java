package com.hmec.admission_portal.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable // This means it can be embedded in another entity
public class PerformanceRecord {
    private Integer ixEng;
    private Integer ixLang;
    private Integer ixMath;
    private Integer ixSci;
    private Integer ixSoc;

    private Integer xEng;
    private Integer xLang;
    private Integer xMath;
    private Integer xSci;
    private Integer xSoc;
}