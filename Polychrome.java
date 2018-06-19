import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Polychrome extends Application {

    final Group root = new Group();
    final pGroup axisGroup = new pGroup();
    final pGroup geneSequence = new pGroup();
    final pGroup world = new pGroup();

    final PerspectiveCamera cam = new PerspectiveCamera(true);
    final pGroup camGroup1 = new pGroup();
    final pGroup camGroup2 = new pGroup();
    final pGroup camGroup3 = new pGroup();
    private static final double CAM_INIT_DISTANCE = -200;
    private static final double CAM_INIT_X_ANGLE = 30.0;
    private static final double CAM_INIT_Y_ANGLE = 315.0;
    private static final double CAM_NEAR_CLIP = 0.1;
    private static final double CAM_FAR_CLIP = 10000.0;
    private static final double CAM_MIN_ZOOM = -100;
    private static final double CAM_MAX_ZOOM = -500;
    private static final double AXIS_LENGTH = 250.0;
    private static final double BASE_ANGLE = 90;
    private static final double CONTROL_MODIFIER = 0.3;
    private static final double SHIFT_MODIFIER = 3.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.5;

    double mouseXf;
    double mouseYf;
    double mouseX0;
    double mouseY0;
    double mouseDeltaX;
    double mouseDeltaY;

    boolean wPressed;
    boolean sPressed;
    boolean aPressed;
    boolean dPressed;
    boolean qPressed;
    boolean ePressed;

    private void buildCam() {
        root.getChildren().add(camGroup1);
        camGroup1.getChildren().add(camGroup2);
        camGroup2.getChildren().add(camGroup3);
        camGroup3.getChildren().add(cam);
        camGroup3.setRotateZ(180.0);

        cam.setNearClip(CAM_NEAR_CLIP);
        cam.setFarClip(CAM_FAR_CLIP);
        cam.setTranslateZ(CAM_INIT_DISTANCE);
        camGroup1.ry.setAngle(CAM_INIT_Y_ANGLE);
        camGroup1.rx.setAngle(CAM_INIT_X_ANGLE);
    }

    private void buildAxes() {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);

        final Box xAxis = new Box(AXIS_LENGTH, 1, 1);
        final Box yAxis = new Box(1, AXIS_LENGTH, 1);
        final Box zAxis = new Box(1, 1, AXIS_LENGTH);

        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);

        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.setVisible(false);
        world.getChildren().addAll(axisGroup);
    }

    private void handleMouse(Scene scene, final Node root) {
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mouseXf = me.getSceneX();
                mouseYf = me.getSceneY();
                // TODO - may not be necessary to set old x and y here
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                // new x,y naught is previous x,y final
                mouseX0 = mouseXf;
                mouseY0 = mouseYf;
                mouseXf = me.getSceneX();
                mouseYf = me.getSceneY();
                mouseDeltaX = mouseXf - mouseX0;
                mouseDeltaY = mouseYf - mouseY0;

                double mod = 1.0;

                if (me.isControlDown()) {
                    mod = CONTROL_MODIFIER;
                }
                if (me.isShiftDown()) {
                    mod = SHIFT_MODIFIER;
                }
                if (me.isPrimaryButtonDown()) {
                    camGroup1.ry.setAngle(camGroup1.ry.getAngle() - mouseDeltaX*MOUSE_SPEED*mod*ROTATION_SPEED);
                    camGroup1.rx.setAngle(camGroup1.rx.getAngle() + mouseDeltaY*MOUSE_SPEED*mod*ROTATION_SPEED);
                }
                // TODO - modifies camera position. Set to be used when button
                // camera button movement selected in UI
                else if (me.isAltDown() && me.isSecondaryButtonDown()) {
                    camGroup2.t.setX(camGroup2.t.getX() + mouseDeltaX*MOUSE_SPEED*mod*TRACK_SPEED);
                    camGroup2.t.setY(camGroup2.t.getY() + mouseDeltaY*MOUSE_SPEED*mod*TRACK_SPEED);
                }


            }
        });

        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent se) {
                double zoom = cam.getTranslateZ() + se.getDeltaY();

                if (!se.isInertia()) {
                    if (zoom >= CAM_MIN_ZOOM) {
                        cam.setTranslateZ(CAM_MIN_ZOOM);
                    }
                    else if (zoom <= CAM_MAX_ZOOM) {
                        cam.setTranslateZ(CAM_MAX_ZOOM);
                    }
                    else {
                        cam.setTranslateZ(cam.getTranslateZ() + se.getDeltaY());
                    }
                }
            }
        });
    }

    private void handleKeyboard(Scene scene, final Node root) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case V:
                        geneSequence.setVisible(!geneSequence.isVisible());
                        break;
                    case X:
                        axisGroup.setVisible(!axisGroup.isVisible());
                        break;
                    case Z:
                        camGroup2.t.setX(0.0);
                        camGroup2.t.setY(0.0);
                        cam.setTranslateZ(CAM_INIT_DISTANCE);
                        camGroup1.ry.setAngle(CAM_INIT_Y_ANGLE);
                        camGroup1.rx.setAngle(CAM_INIT_X_ANGLE);
                        break;
                }
            }
        });

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:
                        wPressed = true;
                        break;
                    case S:
                        sPressed = true;
                        break;
                    case A:
                        aPressed = true;
                        break;
                    case D:
                        dPressed = true;
                        break;
                    case Q:
                        qPressed = true;
                        break;
                    case E:
                        ePressed = true;
                        break;
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:
                        wPressed = false;
                        break;
                    case S:
                        sPressed = false;
                        break;
                    case A:
                        aPressed = false;
                        break;
                    case D:
                        dPressed = false;
                        break;
                    case Q:
                        qPressed = false;
                        break;
                    case E:
                        ePressed = false;
                        break;
                }
            }
        });
    }

    private void keyboardRotate(int dx, int dy, int dz) {
        camGroup1.rx.setAngle(camGroup1.rx.getAngle() + dx);
        camGroup1.ry.setAngle(camGroup1.ry.getAngle() + dy);
        camGroup1.rz.setAngle(camGroup1.rz.getAngle() + dz);

    }

    private void buildSequence() {

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

        final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.DARKGREY);
        greyMaterial.setSpecularColor(Color.GREY);

        pGroup sequencePGroup = new pGroup();
        pGroup sphere1PGroup = new pGroup();
        pGroup sphere2SidePGroup = new pGroup();
        pGroup sphere2PGroup = new pGroup();
        pGroup sphere3SidePGroup = new pGroup();
        pGroup sphere3PGroup = new pGroup();

        Sphere sphere1 = new Sphere(5);
        sphere1.setMaterial(redMaterial);

        Sphere sphere2 = new Sphere(5);
        sphere2.setMaterial(whiteMaterial);
        sphere2.setTranslateX(0.0);

        Sphere sphere3 = new Sphere(5);
        sphere3.setMaterial(whiteMaterial);
        sphere3.setTranslateZ(0.0);

        Cylinder bond1Cylinder = new Cylinder(1, 20);
        bond1Cylinder.setMaterial(greyMaterial);
        bond1Cylinder.setTranslateX(10.0);
        bond1Cylinder.setRotationAxis(Rotate.Z_AXIS);
        bond1Cylinder.setRotate(90.0);

        Cylinder bond2Cylinder = new Cylinder(1, 20);
        bond2Cylinder.setMaterial(greyMaterial);
        bond2Cylinder.setTranslateX(10.0);
        bond2Cylinder.setRotationAxis(Rotate.Z_AXIS);
        bond2Cylinder.setRotate(90.0);

        sequencePGroup.getChildren().add(sphere1PGroup);
        sequencePGroup.getChildren().add(sphere2SidePGroup);
        sequencePGroup.getChildren().add(sphere3SidePGroup);
        sphere1PGroup.getChildren().add(sphere1);
        sphere2SidePGroup.getChildren().add(sphere2PGroup);
        sphere3SidePGroup.getChildren().add(sphere3PGroup);
        sphere2PGroup.getChildren().add(sphere2);
        sphere3PGroup.getChildren().add(sphere3);
        sphere2SidePGroup.getChildren().add(bond1Cylinder);
        sphere3SidePGroup.getChildren().add(bond2Cylinder);

        sphere2PGroup.setTranslateX(20.0);
        sphere3PGroup.setTranslateX(20.0);
        sphere3SidePGroup.setRotateY(BASE_ANGLE);

        geneSequence.getChildren().add(sequencePGroup);

        world.getChildren().addAll(geneSequence);
    }

    @Override
    public void start(Stage primaryStage) {
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        buildCam();
        buildAxes();
        buildSequence();

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.GREY);
        handleKeyboard(scene, world);
        handleMouse(scene, world);

        primaryStage.setTitle("Polychrome (Beta Version)");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setCamera(cam);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                int rx = 0;
                int ry = 0;
                int rz = 0;

                if (wPressed) rx -= 1;
                if (sPressed) rx += 1;
                if (aPressed) ry += 1;
                if (dPressed) ry -= 1;
                if (qPressed) rz += 1;
                if (ePressed) rz -= 1;

                keyboardRotate(rx, ry, rz);
            }
        };
        timer.start();
    }

    // Ignored in javafx applications
    public static void main(String[] args) {
        launch(args);
    }

}
