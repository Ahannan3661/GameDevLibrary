import javafx.scene.paint.Color;

public class SubSurvival extends Game
{
    public Sprite water;
    public Sprite sub;
    public Sprite core;
    public Group enemyGroup;
    public Label scoreLabel;
    public Label hpLabel;
    public Animation explosionAnim;
    public Texture enemyTex;
    public int score = 0;
    public int HP = 100;
    public double enemyTimer;
    public double enemySpeed;
    public Label loseLabel;
    public Texture bulletTex;
    public Group bulletGroup;


    public void initialize()
    {
        setTitle("Sub Survival");
        setWindowSize(640,480);

        water = new Sprite();
        Texture wTex = new Texture("water.png");
        water.setTexture( wTex );
        water.setPosition(320,240);
        group.add( water );

        bulletGroup = new Group();
        group.add(bulletGroup);

        sub = new Sprite();
        Texture sTex = new Texture("player.png");
        sub.setTexture(sTex);
        sub.setPosition(200,240);
        sub.setPhysics( new Physics(200, 200, 20) );
        sub.addAction( Action.boundToScreen(640,480) );
        group.add( sub );

        core = new Sprite();
        Texture cTex = new Texture("core.png");
        core.setTexture(cTex);
        core.setPosition(70,240);
        group.add( core );

        explosionAnim = new Animation(
                "explosion.png", 5,8, 0.02, false);

        scoreLabel = new Label("Comic Sans MS Bold", 25);
        String text = "Score: " + score;
        scoreLabel.setText( text );
        scoreLabel.setPosition(600, 30);
        scoreLabel.alignment = "RIGHT";
        scoreLabel.fontColor = Color.WHITE;
        scoreLabel.setBorder(2, Color.BLACK);
        group.add(scoreLabel);

        hpLabel = new Label("Comic Sans MS Bold", 25);
         text = "HP: " + HP;
        hpLabel.setText( text );
        hpLabel.setPosition(600, 460);
        hpLabel.alignment = "RIGHT";
        hpLabel.fontColor = Color.WHITE;
        hpLabel.setBorder(2, Color.BLACK);
        group.add(hpLabel);

        enemyGroup = new Group();
        group.add(enemyGroup);
        enemyTex = new Texture("enemy.png");
        enemyTimer = 2.0;
        enemySpeed = 150;

        loseLabel = new Label("Comic Sans MS Bold", 60);
        loseLabel.setText("Game Over!");
        loseLabel.fontColor = Color.LIGHTGREEN;
        loseLabel.setBorder(2, Color.DARKGREEN);
        loseLabel.setPosition(320, 240);
        loseLabel.alignment = "CENTER";
        loseLabel.visible = false;
        group.add(loseLabel);

        bulletTex = new Texture("bullet.png");

    }


    public void update()
    {
        if (loseLabel.visible)
        {
            return;
        }

        sub.preventOverlap(core);

        if (input.isKeyPressed("RIGHT"))
            sub.physics.accelerateAtAngle(0);

        if (input.isKeyPressed("LEFT"))
            sub.physics.accelerateAtAngle(180);

        if (input.isKeyPressed("UP"))
            sub.physics.accelerateAtAngle(270);

        if (input.isKeyPressed("DOWN"))
            sub.physics.accelerateAtAngle(90);

        enemyTimer -= 1.0/60.0;
        if (enemyTimer < 0)
        {
            Sprite enemy = new Sprite();
            enemy.setTexture(enemyTex);
            double enemyY = Math.random() * 320 + 100;
            enemy.setPosition(700,enemyY);
            enemy.setPhysics(new Physics(0,600,0));
            enemy.physics.setSpeed(enemySpeed);
            enemy.physics.setMotionAngle(180);
            enemyGroup.add(enemy);
            enemyTimer = 2.0;
            enemySpeed += 10;
        }
        for (Sprite enemy : enemyGroup.getSpriteList())
        {
            if(enemy.position.x < -40)
            {
                enemyGroup.remove(enemy);
            }

            if(sub.overlaps(enemy))
            {
                Sprite explosion = new Sprite();
                explosion.setAnimation(
                        explosionAnim.clone() );
                explosion.alignToSprite(sub);
                explosion.addAction( Action.animateThenRemove() );
                group.add( explosion );
                group.remove(sub);
                loseLabel.visible = true;
            }
            if(core.overlaps(enemy))
            {
                Sprite explosion = new Sprite();
                explosion.setAnimation(
                        explosionAnim.clone() );
                explosion.alignToSprite(enemy);
                explosion.addAction( Action.animateThenRemove() );
                group.add( explosion );
                enemyGroup.remove(enemy);
                HP-=25;
                hpLabel.setText( "HP: " + HP);
                core.opacity-=0.25;
                if(HP<=0)
                {
                    loseLabel.visible = true;
                }
            }
        }

        for (Sprite bullet : bulletGroup.getSpriteList())
        {
            if(bullet.position.x > 680)
            {
                bulletGroup.remove(bullet);
            }
            for (Sprite enemy : enemyGroup.getSpriteList())
            {
                if(bullet.overlaps(enemy))
                {
                    Sprite explosion = new Sprite();
                    explosion.setAnimation(
                            explosionAnim.clone() );
                    explosion.alignToSprite(enemy);
                    explosion.addAction( Action.animateThenRemove() );
                    group.add( explosion );
                    enemyGroup.remove(enemy);
                    score+=100;
                    scoreLabel.setText( "Score: " + score );
                }
            }
        }

        if ( input.isKeyJustPressed("SPACE") )
        {
            if(bulletGroup.size()<1)
            {
                Sprite bullet = new Sprite();
                bullet.setTexture(bulletTex);
                bullet.alignToSprite(sub);
                bullet.setPhysics(new Physics(0, 400, 0));
                bullet.physics.setSpeed(400);
                bullet.physics.setMotionAngle(0);
                bulletGroup.add(bullet);
            }

        }
    }
}
