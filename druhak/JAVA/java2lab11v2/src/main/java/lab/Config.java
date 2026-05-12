package lab;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Config {

    @Getter
    private static Config instance;

    @lombok.Builder.Default
    private final double monsterMinXPopsition = 0.5;
    @lombok.Builder.Default
    private final double monsterMinSpeed = 50;
    @lombok.Builder.Default
    private final double monsterMaxSpeed = 150;
    @lombok.Builder.Default
    private final double obstacleWidth = 30;
    @lombok.Builder.Default
    private final double obstacleHeight = 20;
    @lombok.Builder.Default
    private final double playerStartSpeed = 50;
    @lombok.Builder.Default
    private final int monsterStartCount = 5;
    @lombok.Builder.Default
    private final int obscatlesStartCount = 3;

    public static void configure(Config setting) {
        instance = setting;
    }


    public static Config newHradcoreConfig(){
        return Config.builder().monsterMaxSpeed(500).monsterStartCount(10).obscatlesStartCount(10).build();
    }

}
