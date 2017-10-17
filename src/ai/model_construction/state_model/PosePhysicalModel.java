package ai.model_construction.state_model;

import java.io.Serializable;

import utils.constraints.PhysicalConstraint;

public class PosePhysicalModel implements Serializable
{
	private static final long serialVersionUID = 1L;

	public PhysicalConstraint[] constraints = new PhysicalConstraint[]{};
	
	//All models consist of a parameterised generative system
	//and a set of constraints, for poses the parameters are typically bone rotations and root translation
	//and the constraints are interpenetration, touching and facing constraints
	
	//Use to create a pose from constraints e.g. using inverse kinematics and constraints
	
	//most poses relate to interactions with one or more objects
	//e.g. sitting on a sofa using a phone

	//so a pose model can produce poses for each object
	

	
	//One way to construct a pose model is to specify constraints

	//As with shapes we will just create classes for common cases as is convenient
	
	//Hinge
	//rotating tap
	//flush toilet
	//handle
	//lock
	//push button
	//slide button
	//light/power switch
	//pull cord

	//flexible shower hose
	//mouse/power cable
	
	//human pose

	//a model defines the set of objects that are a part of it
	//e.g. holding 3 pint glasses

	//different modeled initialisation poses for 
	//different positions and orientations of wrists relative to an object

	//most poses will have support for positioning the body, feet and hands independently
	//treating the arms and legs as ik connections between the two parts

	//the other parts might be expressed explicitly or be free to be solved for given other constraints
	
	//held object can be positioned and rotated independently
	//with hands constrained to be around the objects
	
	//a potential pose can have an error cost which is how much it breaks the constraints
}
