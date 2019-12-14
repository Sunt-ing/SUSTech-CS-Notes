package observer3;

import java.awt.*;

public class BlueBall extends Ball {
    public BlueBall(Color color, int xSpeed, int ySpeed, int ballSize) {
        super(color, xSpeed, ySpeed, ballSize);
    }

    @Override
    public void update(char kb) {
        this.setXSpeed(-1 * this.getXSpeed());
        this.setYSpeed(-1 * this.getYSpeed());
    }

    public void update(int x, int y) {
        if (  (x-this.getX())*(x-this.getX()) + (y-this.getY())*(y-this.getY()  )<14400){
            int d_x=this.getX()-x;
            int d_y=this.getY()-y;
            if (d_x<0) d_x=-1;
            else if (d_x>0) d_x=1;

            if (d_y<0) d_y=-1;
            else if (d_y>0) d_y=1;

            this.setX(this.getX()+30*d_x);
            this.setY(this.getY()+30*d_y);
        }
    }
}
