package observer3;

import java.awt.*;

public class RedBall extends Ball {
    public RedBall(Color color, int xSpeed, int ySpeed, int ballSize) {
        super(color, xSpeed, ySpeed, ballSize);
    }

    @Override
    public void update(char keyChar) {
        if (keyChar == 'a' || keyChar == 'd') {
            int temp = this.getXSpeed();
            this.setXSpeed(this.getYSpeed());
            this.setYSpeed(temp);
        }
    }

    public void update(int x, int y) {
        if ( (x-this.getX())*(x-this.getX()) + (y-this.getY())*(y-this.getY())<10000 ){
            int d_x=(this.getX()-x)/(Math.abs((this.getX()-x)));
            int d_y=(this.getY()-y)/(Math.abs((this.getY()-y)));
            this.setX(this.getX()+50*d_x);
            this.setY(this.getY()+50*d_y);
        }
    }
}
