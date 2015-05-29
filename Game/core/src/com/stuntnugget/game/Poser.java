package com.stuntnugget.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.utils.Array;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneLoader;

public class Poser {
	public enum POSE {
		STAND, FLOP, CHEER, FLY
	}

	private POSE currentPose;
	private Array<Joint> joints;
	private Map<POSE, Array<Joint>> poseMap = new HashMap<Poser.POSE, Array<Joint>>();
	private float gain = 0.25f;

	Poser(Array<Joint> joints) {
		this.joints = new Array<Joint>();
		this.joints.addAll(joints);
		RubeSceneLoader loader = new RubeSceneLoader();
		Array<Joint> poseJoints = new Array<Joint>();

		RubeScene scene = loader.loadScene(Gdx.files
				.internal("rube/chicken.json"));
		poseJoints = new Array<Joint>();
		poseJoints.addAll(scene.getJoints());
		poseMap.put(POSE.FLOP, poseJoints);
		for (int i = 0; i < poseJoints.size; ++i) {
			String jointName = (String) scene.getCustom(poseJoints.get(i),
					"jointName");
			poseJoints.get(i).setUserData(jointName);
		}

		scene.clear();
		scene = loader.loadScene(Gdx.files.internal("rube/chicken-stand.json"));
		poseJoints = new Array<Joint>();
		poseJoints.addAll(scene.getJoints());
		poseMap.put(POSE.STAND, poseJoints);
		for (int i = 0; i < poseJoints.size; ++i) {
			String jointName = (String) scene.getCustom(poseJoints.get(i),
					"jointName");
			poseJoints.get(i).setUserData(jointName);
		}
		
		scene.clear();
		scene = loader.loadScene(Gdx.files.internal("rube/chicken-cheer.json"));
		poseJoints = new Array<Joint>();
		poseJoints.addAll(scene.getJoints());
		poseMap.put(POSE.CHEER, poseJoints);
		for (int i = 0; i < poseJoints.size; ++i) {
			String jointName = (String) scene.getCustom(poseJoints.get(i),
					"jointName");
			poseJoints.get(i).setUserData(jointName);
		}
/*
		scene = loader.loadScene(Gdx.files.internal("rube/chicken-fly.json"));
		poseJoints = scene.getJoints();
		poseMap.put(POSE.FLY, poseJoints);
		for (int i = 0; i < poseJoints.size; ++i) {
			String jointName = (String) scene.getCustom(poseJoints.get(i),
					"jointName");
			poseJoints.get(i).setUserData(jointName);
		}
*/
		Gdx.app.log("Poser", "gort");
	}

	public void changePose(Poser.POSE pose) {
		currentPose = pose;
		Array<Joint> targetJoints = poseMap.get(pose);
		for (int jointIndex = 0; jointIndex < joints.size; ++jointIndex) {
			if (joints.get(jointIndex).getUserData() == null) {
				continue;
			}
			for (int targetIndex = 0; targetIndex < targetJoints.size; ++targetIndex) {
				if (targetJoints.get(targetIndex) == null) {
					continue;
				}
				String jointName = (String) joints.get(jointIndex)
						.getUserData();
				String targetName = (String) targetJoints.get(targetIndex)
						.getUserData();
				if (jointName.equals(targetName)) {
					switch (joints.get(jointIndex).getType()) {
					case RevoluteJoint:
						RevoluteJoint joint = (RevoluteJoint) joints
								.get(jointIndex);
						boolean foo = joint.isMotorEnabled();
						RevoluteJoint targetJoint = (RevoluteJoint) targetJoints
								.get(targetIndex);
						boolean bar = targetJoint.isMotorEnabled();
						joint.enableMotor(targetJoint.isMotorEnabled());
						joint.setMaxMotorTorque(targetJoint.getMaxMotorTorque());
						break;
					case WeldJoint:
						// TODO do I need this?
						break;
					default:
						break;
					}
					break;
				}
			}
		}
	}

	public void update() {
		Gdx.app.log("Poser", "" + currentPose);
		Array<Joint> targetJoints = poseMap.get(currentPose);
		for (int jointIndex = 0; jointIndex < joints.size; ++jointIndex) {
			if (joints.get(jointIndex).getUserData() == null) {
				continue;
			}
			for (int targetIndex = 0; targetIndex < targetJoints.size; ++targetIndex) {
				if (targetJoints.get(targetIndex) == null) {
					continue;
				}
				String jointName = (String) joints.get(jointIndex)
						.getUserData();
				String targetName = (String) targetJoints.get(targetIndex)
						.getUserData();
				if (jointName.equals(targetName)) {
					switch (joints.get(jointIndex).getType()) {
					case RevoluteJoint:
						RevoluteJoint joint = (RevoluteJoint) joints
								.get(jointIndex);
						RevoluteJoint targetJoint = (RevoluteJoint) targetJoints
								.get(targetIndex);
						boolean foo = joint.isMotorEnabled();
						float bar = joint.getMaxMotorTorque();
						Gdx.app.log("Poser", "" + foo);
						float angleError = joint.getJointAngle()
								- targetJoint.getJointAngle();
						joint.setMotorSpeed(-gain * angleError);
						break;
					default:
						break;
					}
					break;
				}
			}
		}
	}
}
