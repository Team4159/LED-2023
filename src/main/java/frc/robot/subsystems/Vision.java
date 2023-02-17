package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class Vision extends SubsystemBase {
    private NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight"); // Fetches the limelight section of the networktables

    public void setPipeline(int num) {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(num);
    }

    public long getPipeline() {
        return NetworkTableInstance.getDefault().getTable("limelight").getEntry("getpipe").getInteger(-1);
    }

    @Override
    public void periodic() {
        double[] poseData = limelight.getEntry("botpose").getValue().getDoubleArray(); // gets the pose portion as an array of doubles [x, y, z, rotX, rotY, rotZ]
        double latency = limelight.getEntry("tl").getDouble(0);
        if (poseData.length != 6 || latency == 0) return;
        var pose = new Pose2d(poseData[0] + Constants.VisionConstants.fieldWidth/2, poseData[1] + Constants.VisionConstants.fieldHeight/2, Rotation2d.fromDegrees(poseData[5]));
        RobotContainer.s_Swerve.updatePoseEstimator(pose,latency+0.011); // sends new data to swerve TODO: Latency Constant
        RobotContainer.dataBoard.setVisionPose(pose);
    }
}