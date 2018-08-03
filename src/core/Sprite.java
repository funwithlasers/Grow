/** 
 *  Desc:   Definition of a simple sprite that uses steering behaviors
 * 
 * @author Petr (http://www.sallyx.org/)
 */
package core;

import java.awt.*;
import java.util.ArrayList;

import common.misc.Cgdi;
import common.misc.SmootherV2;
import common.D2.Vector2D;
import javafx.scene.shape.Circle;

import java.util.List;
import static common.D2.Vector2D.*;
import static common.misc.Cgdi.gdi;
import static core.ParamLoader.Prm;
import static common.D2.Transformation.*;

public class Sprite extends MovingEntity {

    //a pointer to the world data. So a sprite can access any obstacle,
    //path, wall or agent data
    protected GameWorld m_pWorld;
    //the steering behavior class
    protected SteeringBehavior m_pSteering;
    //some steering behaviors give jerky looking movement. The
    //following members are used to smooth the sprite's heading
    protected SmootherV2<Vector2D> m_pHeadingSmoother;
    //this vector represents the average of the sprite's heading
    //vector smoothed over the last few frames
    protected Vector2D m_vSmoothedHeading;
    //when true, smoothing is active
    protected boolean m_bSmoothingOn;
    //keeps a track of the most recent update time. (some of the
    //steering behaviors make use of this - see Wander)
    protected double m_dTimeElapsed;
    //buffer for the sprite shape
    private List<Vector2D> m_vecspriteVB = new ArrayList<Vector2D>();
    //radius of circular sprites

    /**
     *  fills the sprite's shape buffer with its vertices
     */
    protected void InitializeBuffer() {

        final int NumspriteVerts = 3;

        Vector2D sprite[] = {new Vector2D(-1.0f, 0.6f),
            new Vector2D(1.0f, 0.0f),
            new Vector2D(-1.0f, -0.6f)};

        //setup the vertex buffers and calculate the bounding radius
        for (int vtx = 0; vtx < NumspriteVerts; ++vtx) {
            m_vecspriteVB.add(sprite[vtx]);
        }
    }

    //disallow the copying of Sprite types
    //private Sprite(Sprite v) { }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cloning not allowed");
    }

    public Sprite(GameWorld world,
                  Vector2D position,
                  double rotation,
                  Vector2D velocity,
                  double mass,
                  double max_force,
                  double max_speed,
                  double max_turn_rate,
                  double scale) {
        super(position,
                scale,
                velocity,
                max_speed,
                new Vector2D(Math.sin(rotation), -Math.cos(rotation)),
                mass,
                new Vector2D(scale, scale),
                max_turn_rate,
                max_force);

        m_pWorld = world;
        m_vSmoothedHeading = new Vector2D(0, 0);
        m_bSmoothingOn = false;
        m_dTimeElapsed = 0.0;
        //InitializeBuffer();

        //set up the steering behavior class
        m_pSteering = new SteeringBehavior(this);

        //set up the smoother
        m_pHeadingSmoother = new SmootherV2<Vector2D>(Prm.NumSamplesForSmoothing, new Vector2D(0.0, 0.0));

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        m_pSteering = null;
        m_pHeadingSmoother = null;
    }

    /**
    *  Updates the sprite's position and orientation from a series of steering behaviors
    */
    public void Update(double time_elapsed) {
        //update the time elapsed
        m_dTimeElapsed = time_elapsed;

        //keep a record of its old position so we can update its cell later
        //in this method
        Vector2D OldPos = Pos();

        Vector2D SteeringForce;

        //calculate the combined force from each steering behavior in the 
        //sprite's list
        SteeringForce = m_pSteering.Calculate();

        //Acceleration = Force/Mass
        Vector2D acceleration = div(SteeringForce, m_dMass);

        //update velocity
        m_vVelocity.add(mul(acceleration, time_elapsed));

        //make sure sprite does not exceed maximum velocity
        m_vVelocity.Truncate(m_dMaxSpeed);

        //update the position
        m_vPos.add(mul(m_vVelocity, time_elapsed));

        //update the heading if the sprite has a non zero velocity
        if (m_vVelocity.LengthSq() > 0.00000001) {
            m_vHeading = Vec2DNormalize(m_vVelocity);

            m_vSide = m_vHeading.Perp();
        }

        //EnforceNonPenetrationConstraint(this, World()->Agents());

        //treat the screen as a toroid
        //WrapAround(m_vPos, m_pWorld.cxClient(), m_pWorld.cyClient());

        //update the sprite's current cell if space partitioning is turned on
        if (Steering().isSpacePartitioningOn()) {
            World().CellSpace().UpdateEntity(this, OldPos);
        }

        if (isSmoothingOn()) {
            m_vSmoothedHeading = m_pHeadingSmoother.Update(Heading());
        }
    }

    public void Devour(Sprite s2) {
        if (m_dBoundingRadius >= s2.m_dBoundingRadius) {
            resize(Scale().x / 5);    //CHANGE CONSTANTS
            m_pWorld.Agents().remove(s2);
        }
        if(s2.m_dBoundingRadius > m_dBoundingRadius) {
            resize(s2.Scale().x / 5);
            m_pWorld.Agents().remove(this);
        }

    }
    /**
     *Used to INCREASE the size of Sprite by growth
     */
    //TODO: look for better way to grow with a Vector2D parameter so it doesn't depend on Scale().x == Scale().y
    protected void resize(double growth){
        SetScale(Scale().x + growth);
    }
//-------------------------------- Render -------------------------------------
//-----------------------------------------------------------------------------

    public void Render(boolean pr) {

        //render neighboring sprites in different colors if requested
        if (m_pWorld.RenderNeighbors()) {
            if (ID() == 0) {
                gdi.RedPen();
            } else if (IsTagged()) {
                gdi.GreenPen();
            } else {
                gdi.BluePen();
            }
        } else {
            gdi.BluePen();
        }

        if (Steering().isInterposeOn()) {
            gdi.RedPen();
        }

        if (Steering().isHideOn()) {
            gdi.GreenPen();
        }

        //a vector to hold the transformed vertices
        List<Vector2D> m_vecspriteVBTrans;

        if (isSmoothingOn()) {
            m_vecspriteVBTrans = WorldTransform(m_vecspriteVB,
                    Pos(),
                    SmoothedHeading(),
                    SmoothedHeading().Perp(),
                    Scale());
        } else {
            m_vecspriteVBTrans = WorldTransform(m_vecspriteVB,
                    Pos(),
                    Heading(),
                    Side(),
                    Scale());
        }
        //gdi.ClosedShape(m_vecspriteVBTrans);
        gdi.Circle(Pos(), m_dBoundingRadius);

        //render any visual aids / and or user options
        if (m_pWorld.ViewKeys()) {
            Steering().RenderAids();
        }

    }
    //-------------------------------------------accessor methods

    public SteeringBehavior Steering() {
        return m_pSteering;
    }

    public GameWorld World() {
        return m_pWorld;
    }

    public Vector2D SmoothedHeading() {
        return new Vector2D(m_vSmoothedHeading);
    }

    public boolean isSmoothingOn() {
        return m_bSmoothingOn;
    }

    public void SmoothingOn() {
        m_bSmoothingOn = true;
    }

    public void SmoothingOff() {
        m_bSmoothingOn = false;
    }

    public void ToggleSmoothing() {
        m_bSmoothingOn = !m_bSmoothingOn;
    }

    /**
     * @return time elapsed from last update
     */
    public double getTimeElapsed() {
        return m_dTimeElapsed;
    }

    /**
     * @deprecated Use {@link getTimeElapsed()} instead
     * @return time elapsed from last update
     */
    public double TimeElapsed() {
	    return getTimeElapsed();
    }
}