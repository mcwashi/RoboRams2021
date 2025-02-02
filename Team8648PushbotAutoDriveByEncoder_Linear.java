package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;







@Autonomous(name="Pushbot: 8648 Auto Drive By Encoder", group="Pushbot")
//@Disabled
public class Team8648PushbotAutoDriveByEncoder_Linear extends LinearOpMode {
    /* Declare OpMode members. */
    Team8648HardwarePushbot robot   = new Team8648HardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1120;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */

        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();



        /*flMotor  = hwMap.get(DcMotor.class, "FLmotor");
        rlMotor = hwMap.get(DcMotor.class, "RLmotor");
        frMotor  = hwMap.get(DcMotor.class, "FRmotor");
        rrMotor = hwMap.get(DcMotor.class, "RRmotor");*/


        //robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.flMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rlMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rrMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);




        //robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.flMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rlMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rrMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                robot.flMotor.getCurrentPosition(),
                robot.rlMotor.getCurrentPosition(),
                robot.frMotor.getCurrentPosition(),
                robot.rrMotor.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        //encoderDrive(DRIVE_SPEED,  48,  48, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
        encoderDrive(TURN_SPEED,   12, -12, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
        //encoderDrive(DRIVE_SPEED, -24, -24, 4.0);  // S3: Reverse 24 Inches with 4 Sec timeout

        //robot.leftClaw.setPosition(1.0);            // S4: Stop and close the claw.
        //robot.rightClaw.setPosition(0.0);
        //sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        int newLeftTargetRear;
        int newRightTargetRear;


        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            //newLeftTarget = robot.leftDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            //newRightTarget = robot.rightDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

            newLeftTarget = robot.flMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newLeftTargetRear = robot.rlMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.frMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newRightTargetRear = robot.rrMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);





            robot.flMotor.setTargetPosition(newLeftTarget);
            robot.rlMotor.setTargetPosition(newLeftTargetRear);
            robot.frMotor.setTargetPosition(newRightTarget);
            robot.rrMotor.setTargetPosition(newRightTargetRear);

            // Turn On RUN_TO_POSITION
            robot.flMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rlMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.frMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rrMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.flMotor.setPower(Math.abs(speed));
            robot.rlMotor.setPower(Math.abs(speed));
            robot.frMotor.setPower(Math.abs(speed));
            robot.rrMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.flMotor.isBusy() && robot.frMotor.isBusy() && robot.rlMotor.isBusy() && robot.rrMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.flMotor.getCurrentPosition(),
                        robot.rlMotor.getCurrentPosition(),
                        robot.frMotor.getCurrentPosition(),
                        robot.rrMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.flMotor.setPower(0);
            robot.rlMotor.setPower(0);
            robot.frMotor.setPower(0);
            robot.rrMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.flMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rlMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.frMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rrMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
}
