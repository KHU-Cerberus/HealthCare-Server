package cerberus.HealthCare.global.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    GOOD, CAUTION, DANGER;

    @JsonValue
    public String toLowerCase() {
        return this.name().toLowerCase();
    }
}
