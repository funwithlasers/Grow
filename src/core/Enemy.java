package core;

import common.D2.Vector2D;
import core.GameWorld;
import core.Sprite;

import java.util.Random;

import static common.D2.Vector2D.Vec2DNormalize;
import static common.D2.Vector2D.div;
import static common.D2.Vector2D.mul;

public class Enemy extends Sprite {
    public Enemy(GameWorld world, Vector2D pos, double scale, Vector2D velocity) {

      super(world,
      pos,
      0,
      new Vector2D(0,0),
      1,            //GARBAGE
      20,       //GARBAGE
      200,       //GARBAGE
    5,          //GARBAGE
        scale );

      Steering().WallAvoidanceOn();

    }

    public void eats(Enemy s2) {

        if (s2.isEdibleBy(this)) {
            resize(5);    //CHANGE CONSTANTS
            m_pWorld.Agents().remove(s2);
        }
        else {
            s2.resize(5);
            m_pWorld.Agents().remove(this);
        }
        m_pWorld.respawn();
    }

    public boolean isEdibleBy(Sprite s){
        return(this.Scale().x < s.Scale().x);
    }

    public void Update(double time_elapsed) {
        super.Update(time_elapsed);

        if(this.Scale().x < GameWorld.pHero.Scale().x) {
            //System.out.println("EVADE");
            Steering().EvadeOn(GameWorld.pHero); }
        else {
            Steering().PursuitOn(GameWorld.pHero);
            //System.out.println("PURSUIT");
        }

        if(m_vPos.x < this.Scale().x || m_vPos.x > constants.constWindowWidth - Scale().x)
            m_vVelocity.x = -m_vVelocity.x;
        if(m_vPos.y < this.Scale().y || m_vPos.y > constants.constWindowHeight - Scale().y)
            m_vVelocity.y = -m_vVelocity.y;

        //EnforceNonPenetrationConstraint(this, World()->Agents())
    }

}
