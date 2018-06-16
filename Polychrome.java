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
    private static final double AXIS_LENGTH = 250.0;
    private static final double BASE_ANGLE = 90;
    private static final double CONTROL_MULTIPLIER = 0.3;
    private static final double SHIFT_MULTIPLIER = 3.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.5;

    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

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
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = mousePosX - mouseOldX;
                mouseDeltaY = mousePosY - mouseOldY;

                double modifier = 1.0;

                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                }
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }
                if (me.isPrimaryButtonDown()) {
                    camGroup1.ry.setAngle(camGroup1.ry.getAngle() - mouseDeltaX*MOUSE_SPEED*modifier*ROTATION_SPEED);
                    camGroup1.rx.setAngle(camGroup1.rx.getAngle() + mouseDeltaY*MOUSE_SPEED*modifier*ROTATION_SPEED);
                }
                // TODO - modifies camera position. Set to be used when button
                // camera button movement selected in UI

                // else if (me.isAltDown() && me.isSecondaryButtonDown()) {
                //     camGroup2.t.setX(camGroup2.t.getX() + mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);
                //     camGroup2.t.setY(camGroup2.t.getY() + mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);
                // }
            }
        });

        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public  void handle(ScrollEvent se) {
                if (!se.isInertia()) {
                    cam.setTranslateZ(cam.getTranslateZ() + se.getDeltaY());
                }
                // mouseOldY = mousePosY;
                // mousePosY = se.getSceneY();
                // mouseDeltaY = mousePosY - mouseOldY;

                // double modifier = 1.0;

                // if (se.isControlDown()) {
                //     modifier = CONTROL_MULTIPLIER;
                // }
                // if (se.isShiftDown()) {
                //     modifier = SHIFT_MULTIPLIER;
                // }
                // double z = cam.getTranslateZ();
                // double newZ = z + -1*mouseDeltaY*MOUSE_SPEED*modifier;
                // cam.setTranslateZ(newZ);
            }
        });
    }

    private void handleKeyboard(Scene scene, final Node root) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case Z:
                        camGroup2.t.setX(0.0);
                        camGroup2.t.setY(0.0);
                        cam.setTranslateZ(CAM_INIT_DISTANCE);
                        camGroup1.ry.setAngle(CAM_INIT_Y_ANGLE);
                        camGroup1.rx.setAngle(CAM_INIT_X_ANGLE);
                        break;
                    case X:
                        axisGroup.setVisible(!axisGroup.isVisible());
                        break;
                    case V:
                        geneSequence.setVisible(!geneSequence.isVisible());
                        break;
                }
            }
        });
    }

    private void buildMolecule() {

        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);

        final PhongMaterial whiteMaterial = new PhongMaterial();
        whiteMaterial.setDiffuseColor(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

        final PhongMaterial greyMaterial = new PhongMaterial();
        greyMaterial.setDiffuseColor(Color.DARKGREY);
        greyMaterial.setSpecularColor(Color.GREY);

        pGroup moleculePGroup = new pGroup();
        pGroup oxygenPGroup = new pGroup();
        pGroup hydrogen1SidePGroup = new pGroup();
        pGroup hydrogen1PGroup = new pGroup();
        pGroup hydrogen2SidePGroup = new pGroup();
        pGroup hydrogen2PGroup = new pGroup();

        Sphere oxygenSphere = new Sphere(5);
        oxygenSphere.setMaterial(redMaterial);

        Sphere hydrogen1Sphere = new Sphere(5);
        hydrogen1Sphere.setMaterial(whiteMaterial);
        hydrogen1Sphere.setTranslateX(0.0);

        Sphere hydrogen2Sphere = new Sphere(5);
        hydrogen2Sphere.setMaterial(whiteMaterial);
        hydrogen2Sphere.setTranslateZ(0.0);

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

        moleculePGroup.getChildren().add(oxygenPGroup);
        moleculePGroup.getChildren().add(hydrogen1SidePGroup);
        moleculePGroup.getChildren().add(hydrogen2SidePGroup);
        oxygenPGroup.getChildren().add(oxygenSphere);
        hydrogen1SidePGroup.getChildren().add(hydrogen1PGroup);
        hydrogen2SidePGroup.getChildren().add(hydrogen2PGroup);
        hydrogen1PGroup.getChildren().add(hydrogen1Sphere);
        hydrogen2PGroup.getChildren().add(hydrogen2Sphere);
        hydrogen1SidePGroup.getChildren().add(bond1Cylinder);
        hydrogen2SidePGroup.getChildren().add(bond2Cylinder);

        hydrogen1PGroup.setTx(20.0);
        hydrogen2PGroup.setTx(20.0);
        hydrogen2SidePGroup.setRotateY(BASE_ANGLE);

        geneSequence.getChildren().add(moleculePGroup);

        world.getChildren().addAll(geneSequence);
    }

    @Override
    public void start(Stage primaryStage) {
        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);

        buildCam();
        buildAxes();
        buildMolecule();

        Scene scene = new Scene(root, 1024, 768, true);
        scene.setFill(Color.GREY);
        handleKeyboard(scene, world);
        handleMouse(scene, world);

        primaryStage.setTitle("Molecule Sample Application");
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setCamera(cam);
    }

    // Ignored in javafx applications
    public static void main(String[] args) {
        launch(args);
    }

}
