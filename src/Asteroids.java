import javafx.scene.paint.Color;

public class Asteroids extends Game
{
    public Sprite spaceship;
    public Group rockGroup;
    public Texture rockTex;
    public Group laserGroup;
    public Texture laserTex;
    public Animation explosionAnim;
    public Sprite shields;
    public Label timelabel;
    public Label scorelabel;
    public Label winLabel;
    public Label loselabel;
    public Label totalscore;
    double time = 60.0;
    int score = 0;

    public void initialize()
    {
        setTitle("Asteroids");
        setWindowSize(800,600);

        Sprite background = new Sprite();
        Texture bgTex = new Texture("space.png");
        background.setTexture( bgTex );
        background.setPosition(400,300);
        group.add( background );

        spaceship = new Sprite();
        Texture ssTex = new Texture("spaceship.png");
        spaceship.setTexture(ssTex);
        spaceship.setPosition(400,300);
        spaceship.setPhysics( new Physics(200, 200, 20) );
        spaceship.addAction( Action.wrapToScreen(800,600) );
        group.add( spaceship );

        timelabel = new Label("Comic Sans MS Bold", 30);
        String text = "Time: " + time;
        timelabel.setText( text );
        timelabel.setPosition(780, 580);
        timelabel.alignment = "RIGHT";
        timelabel.fontColor = Color.WHITE;
        timelabel.setBorder(2, Color.BLACK);
        group.add(timelabel);

        scorelabel = new Label("Comic Sans MS Bold", 30);
        String stext = "Score: " + score;
        scorelabel.setText( stext );
        scorelabel.setPosition(780, 30);
        scorelabel.alignment = "RIGHT";
        scorelabel.fontColor = Color.WHITE;
        scorelabel.setBorder(2, Color.BLACK);
        group.add(scorelabel);

        rockGroup = new Group();
        group.add( rockGroup );
        int rockCount = 8;
        rockTex = new Texture("asteroid.png");
        for (int i = 0; i < rockCount; i++)
        {
            Sprite rock = new Sprite();
            rock.setTexture( rockTex );
            rock.setSize(100,100);

            double angle = 2 * Math.PI * Math.random();
            double x = spaceship.position.x
                    + 250 * Math.cos(angle);
            double y = spaceship.position.y
                    + 250 * Math.sin(angle);
            rock.setPosition(x,y);

            rock.setPhysics( new Physics(0, 100, 0) );

            double moveSpeed = 30 * Math.random() + 90;
            rock.physics.setSpeed(moveSpeed);
            rock.physics.setMotionAngle(
                    Math.toDegrees(angle) );

            double rotateSpeed = 2 * Math.random() + 1;
            rock.addAction(
                    Action.forever(Action.rotateBy(360, rotateSpeed) )
            );

            rock.addAction( Action.wrapToScreen(800,600) );
            rockGroup.add(rock);
        }

        laserGroup = new Group();
        group.add( laserGroup );
        laserTex = new Texture("laser.png");

        explosionAnim = new Animation(
                "explosion.png", 5,8, 0.02, false);

        shields = new Sprite();
        Texture shieldTex = new Texture("shields.png");
        shields.setTexture( shieldTex );
        shields.setSize(120,120);
        group.add(shields);

        winLabel = new Label("Comic Sans MS Bold", 60);
        winLabel.setText("You Win!");
        winLabel.fontColor = Color.LIGHTGREEN;
        winLabel.setBorder(2, Color.DARKGREEN);
        winLabel.setPosition(400, 100);
        winLabel.alignment = "CENTER";
        winLabel.visible = false;
        group.add(winLabel);

        loselabel = new Label("Comic Sans MS Bold", 80);
        loselabel.setText("Game Over!");
        loselabel.fontColor = Color.LIGHTGREEN;
        loselabel.setBorder(2, Color.DARKGREEN);
        loselabel.setPosition(400, 300);
        loselabel.alignment = "CENTER";
        loselabel.visible = false;
        group.add(loselabel);

    }

    public void update()
    {

        if (winLabel.visible)
        {
            totalscore = new Label("Comic Sans MS Bold", 30);
            int timebonus = ((int)time)*5;
            int shieldbonus = (int) (1 - shields.opacity)*20;
            int total = timebonus + shieldbonus +score;
            String stext = "Total Score: " + total;
            totalscore.setText( stext );
            totalscore.setPosition(400, 300);
            totalscore.alignment = "CENTER";
            totalscore.fontColor = Color.WHITE;
            totalscore.setBorder(2, Color.BLACK);
            group.add(totalscore);
            shields.alignToSprite(spaceship);
            return;
        }

        if (loselabel.visible)
        {
            return;
        }


        shields.alignToSprite(spaceship);

        if ( input.isKeyPressed("LEFT") )
            spaceship.rotateBy(-3);

        if ( input.isKeyPressed("RIGHT") )
            spaceship.rotateBy(3);

        if ( input.isKeyPressed("UP") )
            spaceship.physics.accelerateAtAngle(spaceship.angle);

        if ( input.isKeyJustPressed("SPACE") )
        {
            if(laserGroup.size()<3) {
                Sprite laser = new Sprite();
                laser.setTexture(laserTex);
                laser.alignToSprite(spaceship);
                laser.setPhysics(new Physics(0, 400, 0));
                laser.physics.setSpeed(400);
                laser.physics.setMotionAngle(spaceship.angle);
                laser.addAction(Action.wrapToScreen(800, 600));
                laserGroup.add(laser);

                laser.addAction(Action.delayFadeRemove(1, 0.5));
            }
        }

        for (Entity rockE : rockGroup.getList())
        {
            Sprite rock = (Sprite)rockE;

            if (shields.overlaps(rock) && shields.opacity > 0)
            {
                Sprite explosion = new Sprite();
                explosion.setAnimation(
                        explosionAnim.clone() );
                explosion.alignToSprite(rock);
                explosion.addAction( Action.animateThenRemove() );

                group.add( explosion );
                rock.removeSelf();
                shields.opacity -= 0.25;


            }

            // game over
            if (rock.overlaps(spaceship))
            {
                Sprite explosion = new Sprite();
                explosion.setAnimation(
                        explosionAnim.clone() );
                explosion.alignToSprite(spaceship);
                explosion.addAction( Action.animateThenRemove() );

                group.add( explosion );
                spaceship.removeSelf();
                loselabel.visible = true;

            }

            for (Entity laserE : laserGroup.getList())
            {
                Sprite laser = (Sprite)laserE;
                if (rock.overlaps(laser))
                {
                    rockGroup.remove(rock);
                    laserGroup.remove(laser);
                    Sprite explosion = new Sprite();
                    explosion.setAnimation(
                            explosionAnim.clone() );
                    explosion.alignToSprite(rock);

                    explosion.addAction( Action.animateThenRemove() );

                    group.add( explosion );


                    if (rock.width == 100)
                    {
                        score+=10;
                        for (int i = 0; i < 2; i++)
                        {
                            Sprite rockSmall = new Sprite();
                            rockSmall.setTexture(rockTex);
                            rockSmall.setSize(50, 50);
                            rockSmall.alignToSprite(rock);
                            rockSmall.addAction(Action.wrapToScreen(800,600));
                            rockSmall.setPhysics(new Physics(0, 300, 0));
                            rockSmall.physics.setSpeed(
                                    2 * rock.physics.getSpeed());
                            rockSmall.physics.setMotionAngle(
                                    rock.physics.getMotionAngle() + 90*Math.random() - 45);
                            rockGroup.add(rockSmall);
                        }
                    }else score+=20;
                }
            }
        }
        if (rockGroup.size() == 0)
        {
            winLabel.visible = true;
        }

        String text = "Score: " +  score;
        scorelabel.setText( text );

        time -= (1.0/60.0);
         text = "Time: " + (int) time;
        timelabel.setText( text );
        if(time <=0)
        {
            loselabel.visible = true;
        }

    }
}