public class TestGame extends Game
{
    Sprite shark;
    Sprite starfish;
    Sprite turtle;

    public void initialize()
    {
        setTitle("Test Game");
        setWindowSize(800, 600);
        System.out.println("Hello, world!");

        shark = new Sprite();
        shark.setPosition(200, 200);
        shark.setTexture( Texture.load("shark.png") );
        shark.setSize(50, 50);

        group.add(shark);

        starfish = new Sprite();
        starfish.setPosition(50, 50);
        starfish.setTexture( Texture.load("starfish.png") );
        starfish.setSize(50, 50);

        group.add(starfish);

        turtle = new Sprite();
        turtle.setPosition(100, 100);
        turtle.setTexture( Texture.load("turtle.png") );
        turtle.setSize(50, 50);

        group.add(turtle);
    }

    public void update()
    {
    }
}