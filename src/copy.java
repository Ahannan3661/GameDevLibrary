import java.util.Random;
import java.util.Random;

public class copy extends Game
{
    Sprite water;
    Group starfishGroup;
    Sprite turtle;
    Sprite winMessage;
    Group rockGroup;
    Sprite shark;
    Sprite loseMessage;
    Vector var = new Vector();

    public void initialize()
    {
        setTitle("Starfish Collector");
        setWindowSize(800, 600);

        water = new Sprite();
        water.setTexture( new Texture("water.png") );
        water.setPosition(400,300);
        group.add( water );

        shark = new Sprite();
        shark.setPosition(400,300);
        shark.setTexture( new Texture("shark.png") );
        group.add( shark );

        rockGroup = new Group();
        Texture rockTexture = new Texture("rock.png");
        int rockCount = 3;
        for (int i = 0; i < rockCount; i++)
        {
            Sprite rock = new Sprite();
            double x = Math.random() * 600 + 100;
            double y = Math.random() * 400 + 100;
            rock.setPosition(x, y);
            rock.setTexture(rockTexture);
            rockGroup.add( rock );
        }
        group.add( rockGroup );



        starfishGroup = new Group();
        Texture starfishTexture = new Texture("starfish.png");
        int starfishCount = 20;
        for (int i = 0; i < starfishCount; i++)
        {
            Sprite starfish = new Sprite();
            double x = Math.random() * 600 + 100;
            double y = Math.random() * 400 + 100;
            starfish.setPosition(x, y);
            starfish.setTexture(starfishTexture);
            Random r = new Random();
            int low = 40;
            int high = 60;
            int a = r.nextInt(high-low) + low;
            r = new Random();
            int b = r.nextInt(high-low) + low;
            starfish.setSize(a,b);

            boolean rockOverlap;
            do
            {
                rockOverlap = false;

                x = Math.random() * 600 + 100;
                y = Math.random() * 400 + 100;
                starfish.setPosition(x, y);
                for (Entity entity : rockGroup.getList())
                {
                    Sprite rock = (Sprite)entity;
                    if (rock.overlaps(starfish) || shark.overlaps(starfish))
                        rockOverlap = true;
                }
            } while( rockOverlap );

            starfishGroup.add( starfish );
        }
        group.add(starfishGroup);

        turtle = new Sprite();
        turtle.setPosition(90, 90);
        turtle.setTexture( new Texture("turtle.png") );
        turtle.setAngle(90);
        group.add(turtle);


        Sprite water2 = new Sprite();
        water2.setTexture( new Texture("water.png") );
        water2.setPosition(400,300);
        water2.opacity = 0.30;
        group.add( water2 );

        winMessage = new Sprite();
        winMessage.setPosition(400, 300);
        winMessage.setTexture( new Texture("youWin.png") );
        winMessage.visible = false;
        group.add(winMessage);

        //adding the looseMessage sprite to the group
        loseMessage = new Sprite();
        loseMessage.setPosition(400, 300);
        loseMessage.setTexture( new Texture("youLose.png") );
        loseMessage.visible = false;
        group.add(loseMessage);
        //end


    }

    public void update()
    {

        faceTurtle();
        if (winMessage.visible)
            return;
        //game end on loseMessage
        if (loseMessage.visible)
            return;
        //end

        if (input.isKeyPressed("RIGHT"))
        {
            turtle.moveBy(2, 0);
            turtle.setAngle(0);
        }
        if (input.isKeyPressed("LEFT"))
        {
            turtle.moveBy(-2, 0);
            turtle.setAngle(180);
        }
        if (input.isKeyPressed("UP"))
        {
            turtle.moveBy(0, -2);
            turtle.setAngle(270);
        }
        if (input.isKeyPressed("DOWN"))
        {
            turtle.moveBy(0, 2);
            turtle.setAngle(90);
        }

        if ( turtle.position.x < shark.position.x )
            shark.flipped = true;

        if ( turtle.position.x > shark.position.x )
            shark.flipped = false;


        for ( Entity entity : starfishGroup.getList() )
        {
            Random r = new Random();
            int low = -2;
            int high = 2;
            int a = r.nextInt(high-low) + low;
            Sprite starfish = (Sprite)entity;
            starfish.rotateBy(a);
        }

        for ( Entity entity : starfishGroup.getList() )
        {
            Sprite starfish = (Sprite)entity;
            if ( turtle.overlaps(starfish) )
                starfishGroup.remove(starfish);
        }

        for ( Entity entity : rockGroup.getList() )
        {
            Sprite rock = (Sprite)entity;
            turtle.preventOverlap(rock);
        }

        //turtle collision with shark check
        if(turtle.overlaps(shark))
        {
            loseMessage.visible = true;
            group.remove(turtle);
        }//end

        if (starfishGroup.size() == 0)
            winMessage.visible = true;

        turtle.boundToScreen(800, 600);
    }
    public void faceTurtle()
    {
        double x = turtle.position.x - shark.position.x;
        double y = turtle.position.y - shark.position.y;
        var.setValues(x,y);
        shark.setAngle(var.getAngle());
    }
}