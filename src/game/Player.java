package game;

public class Player {

    private double x;
    private final double y;
    private final double width = 40;
    private final double height = 20;

    public Player(double startX, double y) {
        this.x = startX;
        this.y = y;
    }

    public void moveLeft() {
        x -= Constants.PLAYER_SPEED;
        if (x < width / 2) x = width / 2;
    }

    public void moveRight() {
        x += Constants.PLAYER_SPEED;
        if (x > Constants.WIDTH - width / 2) x = Constants.WIDTH - width / 2;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}
