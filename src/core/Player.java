package core;

import common.D2.Vector2D;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;

import static common.D2.Vector2D.Vec2DNormalize;
import static common.D2.Vector2D.div;
import static common.D2.Vector2D.mul;
import static common.misc.Cgdi.gdi;

public class Player extends Sprite {

    private static int score = 0;
    private int score_rate = 5;

    public Player(GameWorld world) {
        super(
                world,          //gameworld
                new Vector2D(0, 0),  //position
                0,  //rotation
                new Vector2D(0, 0),  //initial velocity
                10, //mass
                10,  //max speed
                100,
                10,
                20);

    }

    public void bumpScoreRate(){
        score_rate++;
    }

    public void interact(Enemy m){
        System.out.println("player scale: " + Scale().x + "    ||    " + "Enemy scale: " + m.Scale().x);
        if(m.isEdibleBy(this)) {
            resize(5);    //CHANGE CONSTANTS
            score += m.Scale().x * 10;
            m_pWorld.Agents().remove(m);
            Enemy.numEnemies--;
            m_pWorld.respawn();
        }
        else {
            m_pWorld.Agents().removeAll(m_pWorld.Agents());
            Sprite gameOver = new Sprite(m_pWorld,
                    new Vector2D(constants.constWindowWidth/2, constants.constWindowHeight/2 - 50),
                    0,
                    new Vector2D(0,0),
                    1,            //GARBAGE
                    20,       //GARBAGE
                    200,       //GARBAGE
                    20,          //GARBAGE
                    75 ){

                @Override
                public void Update(double time_elapsed) {
                    super.Update(time_elapsed);
                }

                public void Render(boolean pr) {
                    super.Render(pr);
                    gdi.ThickRedPen();
                    gdi.TextAtPos(constants.constWindowWidth/2 - 200,constants.constWindowHeight/2 - Scale().y/2, "Game Over \n Final Score: " + score);
                }
            };
            //gameOver.SetEntityType(-1);     //
            m_pWorld.Agents().add(gameOver);

            System.out.println("Game Over");
            System.out.println("Score: " + score);
          /*  try {
                HighScore hs = new HighScore();
                hs.manageEndScore(score);
                hs.displayScores();

            } catch (FileNotFoundException e) { System.out.print("FILE NOT FOUND");}
            */
        }
    }

    public static int getScore(){
        return score;
    }
   /*
    public void interact(PowerUp P){
        resize(e.Scale().x /5 );    //CHANGE CONSTANTS
        m_pWorld.Agents().remove(e);
    }
*/
    public void Update(double time_elapsed) {
        //update the time elapsed
        m_dTimeElapsed = time_elapsed;
        Vector2D OldPos = Pos();
        double x, y;
        x = MouseInfo.getPointerInfo().getLocation().getX();
        if (x < Scale().x)
            x = Scale().x;
        if (x > constants.constWindowWidth - Scale().x)
            x = constants.constWindowWidth - Scale().x;
        y = MouseInfo.getPointerInfo().getLocation().getY();
        if (y < Scale().y)
            y = Scale().y;
        if (y > constants.constWindowHeight - Scale().y)
            y = constants.constWindowHeight - Scale().y;

        m_vPos = new Vector2D(x, y);
        score += score_rate;


        List<Sprite> heroCollisions = EntityFunctionTemplates.GetCollisions(m_pWorld.Agents(), (Sprite) this);

        if (!heroCollisions.isEmpty()) {
            for (int i = 0; i < heroCollisions.size(); i++) {
                if (heroCollisions.get(i) instanceof Enemy) interact((Enemy) heroCollisions.get(i));
            }
        }
    }

    @Override
    public void Render(boolean pr) {
        super.Render(pr);
        gdi.Circle(Pos(), m_dBoundingRadius);
    }
}
