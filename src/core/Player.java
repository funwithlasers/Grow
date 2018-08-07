package core;

import common.D2.Vector2D;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;

import static common.D2.Vector2D.Vec2DNormalize;
import static common.D2.Vector2D.div;
import static common.D2.Vector2D.mul;

public class Player extends Sprite {

    private static int score = 0;


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

    public void interact(Enemy m){
        System.out.println("player scale: " + Scale().x + "    ||    " + "Enemy scale: " + m.Scale().x);
        if(m.isEdibleBy(this)) {
            resize(5);    //CHANGE CONSTANTS
            m_pWorld.Agents().remove(m);
            m_pWorld.respawn();
            m_pWorld.zoom();
        }
        else {
            m_pWorld.Agents().removeAll(m_pWorld.Agents());
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
        if(x < Scale().x)
            x = Scale().x;
        if(x > constants.constWindowWidth - Scale().x)
            x = constants.constWindowWidth - Scale().x;
        y = MouseInfo.getPointerInfo().getLocation().getY();
        if(y < Scale().y)
            y = Scale().y;
        if(y > constants.constWindowHeight - Scale().y)
            y = constants.constWindowHeight - Scale().y;

        m_vPos = new Vector2D(x, y);
        score++;



        List<Sprite> heroCollisions = EntityFunctionTemplates.GetCollisions(m_pWorld.Agents(), (Sprite)this);

        if(!heroCollisions.isEmpty()) {
            for (int i = 0; i < heroCollisions.size(); i++) {
                if (heroCollisions.get(i) instanceof Enemy) interact((Enemy)heroCollisions.get(i));
            }
        }

       // System.out.println(score / 100);

        //resize(.05);

        //EnforceNonPenetrationConstraint(this, World()->Agents());

        //treat the screen as a toroid
        //WrapAround(m_vPos, m_pWorld.cxClient(), m_pWorld.cyClient());

        //update the sprite's current cell if space partitioning is turned on
        if (Steering().isSpacePartitioningOn()) {
            World().CellSpace().UpdateEntity(this, OldPos);
        }

    }

}
