package core;

import common.D2.Vector2D;
import core.GameWorld;
import core.Sprite;

import java.util.Random;

import static common.D2.Vector2D.Vec2DNormalize;
import static common.D2.Vector2D.div;
import static common.D2.Vector2D.mul;
import static common.misc.Cgdi.gdi;

public class Enemy extends Sprite {

    public static int numEnemies = 0;

    public Enemy(GameWorld world, Vector2D pos, double scale, Vector2D velocity) {

      super(world,
      pos,
      0,
      velocity,
      1,            //GARBAGE
      20,       //GARBAGE
      200,       //GARBAGE
    20,          //GARBAGE
        scale );

      numEnemies++;

    }

    public void eats(Enemy s2) {

        if (s2.isEdibleBy(this)) {
            resize(5);    //CHANGE CONSTANTS
            m_pWorld.Agents().remove(s2);
            Enemy.numEnemies--;
        }
        else {
            s2.resize(5);
            m_pWorld.Agents().remove(this);
            Enemy.numEnemies--;
        }
        m_pWorld.respawn();
    }

    public boolean isEdibleBy(Sprite s) {
        return(this.Scale().x < s.Scale().x);
    }

    public void Update(double time_elapsed) {
        super.Update(time_elapsed);
        if(Scale().x < 3){
            World().Agents().remove(this);
            Enemy.numEnemies--;
        }

        if(this.Scale().x < GameWorld.pHero.Scale().x) {
            Steering().EvadeOn(GameWorld.pHero);
        }
        else {
            Steering().PursuitOn(GameWorld.pHero);
        }

        if(m_vPos.x < this.Scale().x || m_vPos.x > constants.constWindowWidth - Scale().x)
            m_vVelocity.x = -m_vVelocity.x;
        if(m_vPos.y < this.Scale().y || m_vPos.y > constants.constWindowHeight - Scale().y)
            m_vVelocity.y = -m_vVelocity.y;

        //EnforceNonPenetrationConstraint(this, World()->Agents())
    }

    @Override
    public void Render(boolean pr) {
        super.Render(pr);
        gdi.Circle(Pos(), m_dBoundingRadius);
    }

}
