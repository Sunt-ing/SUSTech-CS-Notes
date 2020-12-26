package observer3;
import java.util.ArrayList;

import java.awt.*;
import java.util.List;

public class GreenBall extends Ball implements subject2 {
    private List<Ball> obs = new ArrayList<>();

    public GreenBall(Color color, int xSpeed, int ySpeed, int ballSize) {

        super(color, xSpeed, ySpeed, ballSize);
    }

    @Override
    public void update(int x, int y) {
    }

    @Override
    public void update(char keyChar) {
        switch (keyChar) {
            case 'a':
                this.setXSpeed(Math.abs(this.getXSpeed()) * -1);
                break;
            case 'd':
                this.setXSpeed(Math.abs(this.getXSpeed()));
                break;
            case 'w':
                this.setYSpeed(Math.abs(this.getYSpeed()) * -1);
                break;
            case 's':
                this.setYSpeed(Math.abs(this.getYSpeed()));
                break;
        }
    }

    @Override
    public void registerObserver(Ball o) {
        obs.add(o);
    }

    @Override
    public void notifyObservers() {
        int x=this.getX();
        int y = this.getY();
        obs.forEach(o -> o.update(x,y));
    }

    @Override
    public void move() {
        super.move();
        notifyObservers();
    }
}
