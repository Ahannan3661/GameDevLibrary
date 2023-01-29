import javafx.scene.paint.Color;

import java.awt.*;
import java.util.Random;

public class StarfishCollector extends Game
{
    Sprite water;
    Group starfishGroup;
    Sprite turtle;
    Group rockGroup;
    Group fishGroup;
    Sprite shark;
    Label starfishLabel;
    Label winLabel;
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

        shark = new Sprite();
        shark.setPosition(400,300);
        shark.setTexture( new Texture("shark.png") );
        group.add( shark );

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
            starfish.setSize( (int)(Math.random() * 20 + 40), (int)(Math.random() * 20 + 40));

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

        fishGroup = new Group();
        Animation fishAnim = new Animation("fish.png", 8, 1, 0.5, true);
        for (int i = 0; i < 6; i++)
        {
            Sprite fish = new Sprite();
            double x = Math.random() * 600 + 100;
            double y = Math.random() * 400 + 100;
            Random r = new Random();
            int low = 50;
            int high = 100;
            int a = r.nextInt(high-low) + low;
            fish.setPosition(x, y);
            fish.setAnimation(fishAnim);
            fish.setPhysics( new Physics(0, a, 0) );
            r = new Random();
            low = 0;
            high = 360;
            int b = r.nextInt(high-low) + low;
            fish.setAngle(b);



            fishGroup.add(fish);

        }
        group.add(fishGroup);

        turtle = new Sprite();
        turtle.setPosition(90, 90);
        turtle.setTexture( new Texture("turtle.png") );

        turtle.setPhysics( new Physics(400, 200, 400) );
        turtle.setAngle(45);
        group.add(turtle);


        Sprite water2 = new Sprite();
        water2.setTexture( new Texture("water.png") );
        water2.setPosition(400,300);
        water2.opacity = 0.30;
        group.add( water2 );




        winLabel = new Label("Comic Sans MS Bold", 80);
        winLabel.setText("You Win!");
        winLabel.fontColor = Color.LIGHTGREEN;
        winLabel.setBorder(2, Color.DARKGREEN);
        winLabel.setPosition(400, 300);
        winLabel.alignment = "CENTER";
        winLabel.visible = false;
        group.add(winLabel);

        starfishLabel = new Label("Comic Sans MS Bold", 48);
        String text = "Starfish Left: " + starfishGroup.size();
        starfishLabel.setText( text );
        starfishLabel.setPosition(780, 580);
        starfishLabel.alignment = "RIGHT";
        starfishLabel.fontColor = Color.BLACK;
        starfishLabel.setBorder(2, Color.BLACK);
        group.add(starfishLabel);

        loseMessage = new Sprite();
        loseMessage.setPosition(400, 300);
        loseMessage.setTexture( new Texture("youLose.png") );
        loseMessage.visible = false;
        group.add(loseMessage);

        for(int i=0 ; i<20 ; i++)
        {
            turtle.addAction(Action.rotateBy(360, 10));
        }
    }

    public void update()
    {

        //turtle.addAction(Action.moveBy(2,2,0));

        if (winLabel.visible)
            return;
        if (loseMessage.visible)
            return;
        faceTurtle();
        fishCheck();

        if (input.isKeyPressed("RIGHT"))
            turtle.physics.accelerateAtAngle(0);

        if (input.isKeyPressed("LEFT"))
            turtle.physics.accelerateAtAngle(180);

        if (input.isKeyPressed("UP"))
            turtle.physics.accelerateAtAngle(270);

        if (input.isKeyPressed("DOWN"))
            turtle.physics.accelerateAtAngle(90);

        if ( turtle.physics.getSpeed() > 0 )
            turtle.setAngle(turtle.physics.getMotionAngle());



        if ( turtle.position.x < shark.position.x )
            shark.flipped = true;

        if ( turtle.position.x > shark.position.x )
            shark.flipped = false;

        for ( Entity entity : fishGroup.getList() )
        {
            Sprite fish = (Sprite)entity;
            fish.physics.accelerateBy(fish.physics.maximumSpeed,fish.angle);
        }

        for ( Entity entity : starfishGroup.getList() )
        {
            Random r = new Random();
            int low = -2;
            int high = 2;
            int a = r.nextInt(high-low) + low;
            Sprite starfish = (Sprite)entity;
            starfish.rotateBy(a);

            if ( turtle.overlaps(starfish) )
            {
                starfishGroup.remove(starfish);
                String text = "Starfish Left: " + starfishGroup.size();
                starfishLabel.setText( text );
            }
        }

        for ( Entity entity : rockGroup.getList() )
        {
            Sprite rock = (Sprite)entity;
            turtle.preventOverlap(rock);
        }

        if(turtle.overlaps(shark))
        {
            loseMessage.visible = true;
            group.remove(turtle);
        }

        if (starfishGroup.size() == 0)
            winLabel.visible = true;

        turtle.boundToScreen(800, 600);
    }
    public void faceTurtle()
    {
        double x = turtle.position.x - shark.position.x;
        double y = turtle.position.y - shark.position.y;
        var.setValues(x,y);
        shark.setAngle(var.getAngle());
    }
    public void fishCheck()
    {
        for ( Entity entity : fishGroup.getList() )
        {
            Sprite fish = (Sprite)entity;
            double w = fish.width;
            double h = fish.height;
            Vector pos = fish.position;
            if(pos.x<(-w/2))
                pos.x = 800 + w/2;
            if(pos.x>(800+(w/2)))
                pos.x = -w/2;
            if(pos.y>(600+(h/2)))
                pos.y = -h/2;
            if(pos.y<(-h/2))
                pos.y = 600 + h/2;
        }
    }
}