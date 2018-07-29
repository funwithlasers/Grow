package core;

import common.D2.Vector2D;

import java.awt.*;

import static common.D2.Vector2D.Vec2DNormalize;
import static common.D2.Vector2D.div;
import static common.D2.Vector2D.mul;

public class Player extends Enemy {

    private static int score = 0;


    public Player(GameWorld g) {
        super(
                g,          //gameworld
                new Vector2D(100, 100),  //position
                0,  //rotation
                new Vector2D(0, 0),  //initial velocity
                10, //mass
                10,  //max speed
                100,
                10,
                20);

        m_pWorld = g;
        m_dTimeElapsed = 0.0;
        InitializeBuffer();
    }

    //TODO: look for better way to grow with a Vector2D parameter so it doesn't depend on Scale().x == Scale().y
    private void grow(double growth){
        SetScale(Scale().x + growth);
    }

    public void Update(double time_elapsed) {
        //update the time elapsed
        m_dTimeElapsed = time_elapsed;
        Vector2D OldPos = Pos();
        m_vPos = new Vector2D(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY() );



        //EnforceNonPenetrationConstraint(this, World()->Agents());

        //treat the screen as a toroid
        //WrapAround(m_vPos, m_pWorld.cxClient(), m_pWorld.cyClient());

        //update the enemy's current cell if space partitioning is turned on
        if (Steering().isSpacePartitioningOn()) {
            World().CellSpace().UpdateEntity(this, OldPos);
        }

    }

}
