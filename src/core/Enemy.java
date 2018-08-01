package core;

import common.D2.Vector2D;
import core.GameWorld;
import core.Sprite;

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


    public void Update(double time_elapsed) {
        super.Update(time_elapsed);

        /**
         * TODO:Find a less expensive way than setting a steering behavior every time each Enemy updates
         *
         */

        if(this.m_vScale.x < GameWorld.pHero.m_vScale.x){
            //System.out.println("EVADE");
            Steering().EvadeOn(GameWorld.pHero); }
        else {
            Steering().PursuitOn(GameWorld.pHero);
           // System.out.println("PURSUIT");
        }

        //EnforceNonPenetrationConstraint(this, World()->Agents());

    }

}
