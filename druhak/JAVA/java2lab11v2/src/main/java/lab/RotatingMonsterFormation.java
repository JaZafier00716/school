package lab;

import java.io.Serial;
import javafx.geometry.Point2D;

public class RotatingMonsterFormation extends Formation<Monster> {

    @Serial
    private static final long serialVersionUID = -6929516153242243375L;

    private double angle = 0;
    private double rotationSpeed = 70;
    public RotatingMonsterFormation(Level level, MyPoint position, Monster... entities) {
        super(level, position, entities);
    }

    @Override
    public void simulateElements(double delta) {
        angle = angle + rotationSpeed * delta;
        if(entitiesInFormation.isEmpty()){
            return;
        }
        Monster middle = entitiesInFormation.getFirst();
        middle.setPositionOfMiddle(position);
        int rotCount = entitiesInFormation.size()-1;
        double radius = 100;
        for (int i = 1; i < entitiesInFormation.size(); i++) {
            Monster monster = entitiesInFormation.get(i);
            double currentAngle = Math.toRadians(angle + 360/rotCount*(i-1));
            MyPoint ufoPosition = position.add(Math.cos(currentAngle)*radius, Math.sin(currentAngle)*radius);
            monster.setPositionOfMiddle(ufoPosition);
        }
    }
}
