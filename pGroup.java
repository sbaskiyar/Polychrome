import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class pGroup extends Group {

    public Translate t  = new Translate();
    public Translate p  = new Translate();
    public Translate ip = new Translate();

    public Rotate rx = new Rotate();
    {rx.setAxis(Rotate.X_AXIS);};
    public Rotate ry = new Rotate();
    {ry.setAxis(Rotate.Y_AXIS);};
    public Rotate rz = new Rotate();
    {rz.setAxis(Rotate.Z_AXIS);};

    public Scale s = new Scale();

    public pGroup() {
        super();
        getTransforms().addAll(t, rz, ry, rx, s);
    }

    public void setTranslate(double x, double y) {
        t.setX(x);
        t.setY(y);
    }

    public void setTranslate(double x, double y, double z) {
        t.setX(x);
        t.setY(y);
        t.setZ(z);
    }

    public void setRotate(double x, double y, double z) {
        rx.setAngle(x);
        ry.setAngle(y);
        rz.setAngle(z);
    }

    public void setRotateX(double x) { rx.setAngle(x); }
    public void setRotateY(double y) { ry.setAngle(y); }
    public void setRotateZ(double z) { rz.setAngle(z); }

    public void setScale(double scaleFactor) {
        s.setX(scaleFactor);
        s.setY(scaleFactor);
        s.setZ(scaleFactor);
    }

    public void setScale(double x, double y, double z) {
        s.setX(x);
        s.setY(y);
        s.setZ(z);
    }

    public void setPivot(double x, double y, double z) {
        p.setX(x);
        p.setY(y);
        p.setZ(z);
        ip.setX(-x);
        ip.setY(-y);
        ip.setZ(-z);
    }
}
