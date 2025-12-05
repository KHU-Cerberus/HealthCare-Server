package cerberus.HealthCare.user.dto.report;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthAnalysisResponse {

    private List<DiseaseRisk> increasedDiseases;
    private List<DiseaseRisk> decreasedDiseases;
    private NutrientDeficiency nutrientDeficiency;

    @Getter
    @Setter
    public static class DiseaseRisk {
        private String name;
        private List<String> causes;
    }

    @Getter
    @Setter
    public static class NutrientDeficiency {
        private List<String> nutrients;
        private List<String> recommendedFoods;
    }
}

