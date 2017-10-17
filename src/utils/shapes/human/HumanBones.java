package utils.shapes.human;

public class HumanBones 
{
	public static int[] bones=
	{
		21,18,
		//18,21,
		18,19,
		19,20,
		20,2,
		2,1,
		1,0,
		21,100,
		100,101,
		101,102,
		102,103,
		103,104,
		21,76,
		76,77,
		77,78,
		78,79,
		79,80,
		0,4,
		0,11,
		0,15,
		2,22,
		2,49,
		4,3,
		11,12,
		15,16,
		11,9,
		11,10,
		15,13,
		15,14,
		49,51,
		51,52,
		52,53,
		53,68,
		53,55,
		53,54,
		53,56,
		55,60,
		55,68,
		54,64,
		54,72,
		22,24,
		24,25,
		25,26,
		26,41,
		26,28,
		26,27,
		26,29,
		28,33,
		28,41,
		27,37,
		27,45,
		56,57,
		57,58,
		58,59,
		60,61,
		61,62,
		62,63,
		68,69,
		69,70,
		70,71,
		64,65,
		65,66,
		66,67,
		72,73,
		73,74,
		74,75,
		29,30,
		30,31,
		31,32,
		33,34,
		34,35,
		35,36,
		41,42,
		42,43,
		43,44,
		37,38,
		38,39,
		39,40,
		45,46,
		46,47,
		47,48,
		3,5,
		5,6,
		6,7,
		7,8,
	};

	public static final float DEG2RAD = ((float)Math.PI)/180.0f;
	
	public static float[] bjlimits=
	{
		//Spine
//		"joint-pelvistojoint-spine3",
		-180.0f*DEG2RAD,180.0f*DEG2RAD,-180.0f*DEG2RAD,180.0f*DEG2RAD,-180.0f*DEG2RAD,180.0f*DEG2RAD,
//		-180.0f*DEG2RAD,180.0f*DEG2RAD,-16.0f*DEG2RAD,16.0f*DEG2RAD,-180.0f*DEG2RAD,180.0f*DEG2RAD,
		//"joint-spine3tojoint-pelvis",
		//-180.0f*DEG2RAD,180.0f*DEG2RAD,-16.0f*DEG2RAD,16.0f*DEG2RAD,-180.0f*DEG2RAD,180.0f*DEG2RAD,
//		"joint-spine3tojoint-spine2",
		-40.0f*DEG2RAD,50.0f*DEG2RAD,-16.0f*DEG2RAD,16.0f*DEG2RAD,-16.0f*DEG2RAD,16.0f*DEG2RAD,
//		"joint-spine2tojoint-spine1",
		-70.0f*DEG2RAD,60.0f*DEG2RAD,-16.0f*DEG2RAD,16.0f*DEG2RAD,-50.0f*DEG2RAD,50.0f*DEG2RAD,
//		"joint-spine1tojoint-neck",
		-20.0f*DEG2RAD,20.0f*DEG2RAD,-16.0f*DEG2RAD,16.0f*DEG2RAD,-20.0f*DEG2RAD,20.0f*DEG2RAD,

		//neck
//		"joint-necktojoint-head",
		-10.0f*DEG2RAD,20.0f*DEG2RAD,-16.0f*DEG2RAD,16.0f*DEG2RAD,-40.0f*DEG2RAD,40.0f*DEG2RAD,
//		"joint-headtojoint-head2",
		-30.0f*DEG2RAD,40.0f*DEG2RAD,-74.0f*DEG2RAD,74.0f*DEG2RAD,-30.0f*DEG2RAD,30.0f*DEG2RAD,


		//right leg
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-110.0f*DEG2RAD,30.0f*DEG2RAD,-90.0f*DEG2RAD,90.0f*DEG2RAD,-90.0f*DEG2RAD,110.0f*DEG2RAD,
		-130.0f*DEG2RAD,20.0f*DEG2RAD,-25.0f*DEG2RAD,35.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-50.0f*DEG2RAD,50.0f*DEG2RAD,-30.0f*DEG2RAD,30.0f*DEG2RAD,-45.0f*DEG2RAD,45.0f*DEG2RAD,
		-35.0f*DEG2RAD,45.0f*DEG2RAD,-20.0f*DEG2RAD,20.0f*DEG2RAD,-10.0f*DEG2RAD,10.0f*DEG2RAD,

		//left leg
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-30.0f*DEG2RAD,110.0f*DEG2RAD,-90.0f*DEG2RAD,90.0f*DEG2RAD,-90.0f*DEG2RAD,110.0f*DEG2RAD,
		-130.0f*DEG2RAD,20.0f*DEG2RAD,-35.0f*DEG2RAD,25.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-50.0f*DEG2RAD,50.0f*DEG2RAD,-3.0f*DEG2RAD,30.0f*DEG2RAD,-45.0f*DEG2RAD,45.0f*DEG2RAD,
		-35.0f*DEG2RAD,45.0f*DEG2RAD,-20.0f*DEG2RAD,20.0f*DEG2RAD,-10.0f*DEG2RAD,10.0f*DEG2RAD,

		//head to mouth
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		//head to leye
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		//head to reye
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		//neck to lclavicle
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		//neck to rclavicle
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		//jaw
		-30.0f*DEG2RAD,0.0f*DEG2RAD,-1.0f*DEG2RAD,1.0f*DEG2RAD,-20.0f*DEG2RAD,20.0f*DEG2RAD,
		//leye
		-50.0f*DEG2RAD,30.0f*DEG2RAD,-30.0f*DEG2RAD,30.0f*DEG2RAD,-35.0f*DEG2RAD,30.0f*DEG2RAD,
		//reye
		-50.0f*DEG2RAD,30.0f*DEG2RAD,-30.0f*DEG2RAD,30.0f*DEG2RAD,-30.0f*DEG2RAD,35.0f*DEG2RAD,
		//llid
		-25.0f*DEG2RAD,15.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,5.0f*DEG2RAD,
		-10.0f*DEG2RAD,5.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,5.0f*DEG2RAD,
		//rlid
		-25.0f*DEG2RAD,15.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-5.0f*DEG2RAD,0.0f*DEG2RAD,
		-5.0f*DEG2RAD,10.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-5.0f*DEG2RAD,0.0f*DEG2RAD,

		//rshoulder
		-45.0f*DEG2RAD,17.0f*DEG2RAD,-3.0f*DEG2RAD,3.0f*DEG2RAD,-16.0f*DEG2RAD,30.0f*DEG2RAD,
		-90.0f*DEG2RAD,90.0f*DEG2RAD,-110.0f*DEG2RAD,110.0f*DEG2RAD,-100.0f*DEG2RAD,70.0f*DEG2RAD,
		0.0f*DEG2RAD,130.0f*DEG2RAD,-40.0f*DEG2RAD,80.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-45.0f*DEG2RAD,45.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-90.0f*DEG2RAD,90.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-5.0f*DEG2RAD,50.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-35.0f*DEG2RAD,20.0f*DEG2RAD,
		0.0f*DEG2RAD,3.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,3.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,3.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,3.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		//lshoulder
		-17.0f*DEG2RAD,45.0f*DEG2RAD,-3.0f*DEG2RAD,3.0f*DEG2RAD,-16.0f*DEG2RAD,30.0f*DEG2RAD,
		-90.0f*DEG2RAD,90.0f*DEG2RAD,-110.0f*DEG2RAD,110.0f*DEG2RAD,-100.0f*DEG2RAD,70.0f*DEG2RAD,
		0.0f*DEG2RAD,130.0f*DEG2RAD,-80.0f*DEG2RAD,40.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-45.0f*DEG2RAD,45.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-90.0f*DEG2RAD,90.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-50.0f*DEG2RAD,5.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-35.0f*DEG2RAD,20.0f*DEG2RAD,
		-3.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-3.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-3.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-3.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,

		//rthumb
		-20.0f*DEG2RAD,50.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-80.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		//
		-15.0f*DEG2RAD,15.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-75.0f*DEG2RAD,15.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-100.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-90.0f*DEG2RAD,0.0f*DEG2RAD,

		-15.0f*DEG2RAD,15.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-75.0f*DEG2RAD,15.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-100.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-90.0f*DEG2RAD,0.0f*DEG2RAD,

		-15.0f*DEG2RAD,15.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-75.0f*DEG2RAD,15.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-100.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-90.0f*DEG2RAD,0.0f*DEG2RAD,

		-15.0f*DEG2RAD,15.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-75.0f*DEG2RAD,15.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-100.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-90.0f*DEG2RAD,0.0f*DEG2RAD,

		//lthumb
		-50.0f*DEG2RAD,20.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,80.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,

		-15.0f*DEG2RAD,15.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-75.0f*DEG2RAD,15.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-100.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-90.0f*DEG2RAD,0.0f*DEG2RAD,

		-15.0f*DEG2RAD,15.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-75.0f*DEG2RAD,15.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-100.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-90.0f*DEG2RAD,0.0f*DEG2RAD,

		//73 - problem with base
		-15.0f*DEG2RAD,15.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-75.0f*DEG2RAD,15.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-100.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-90.0f*DEG2RAD,0.0f*DEG2RAD,

		-15.0f*DEG2RAD,15.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-75.0f*DEG2RAD,15.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-100.0f*DEG2RAD,0.0f*DEG2RAD,
		0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,-90.0f*DEG2RAD,0.0f*DEG2RAD,

		//tongue
		-60.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,0.0f*DEG2RAD,
		-20.0f*DEG2RAD,30.0f*DEG2RAD,-5.0f*DEG2RAD,5.0f*DEG2RAD,-20.0f*DEG2RAD,20.0f*DEG2RAD,
		-20.0f*DEG2RAD,30.0f*DEG2RAD,-5.0f*DEG2RAD,5.0f*DEG2RAD,-20.0f*DEG2RAD,20.0f*DEG2RAD,
		-20.0f*DEG2RAD,30.0f*DEG2RAD,-5.0f*DEG2RAD,5.0f*DEG2RAD,-20.0f*DEG2RAD,20.0f*DEG2RAD,
	};
	
	public static int[] bprnts=
	{
		-1,
		//0,
		0,
		1,
		2,
		3,
		4,
		0,
		6,
		7,
		8,
		9,
		0,
		11,
		12,
		13,
		14,
		5,
		5,
		5,
		3,
		3,
		5,
		5,
		5,
		5,
		5,
		5,
		5,
		3,
		28,
		29,
		30,
		31,
		31,
		31,
		32,
		32,
		33,
		33,
		3,
		39,
		40,
		41,
		42,
		42,
		42,
		43,
		43,
		44,
		44,
		34,
		50,
		51,
		35,
		53,
		54,
		36,
		56,
		57,
		37,
		59,
		60,
		38,
		62,
		63,
		45,
		65,
		66,
		46,
		68,
		69,
		47,
		71,
		72,
		48,
		74,
		75,
		49,
		77,
		78,
		21,
		21,
		81,
		82,
	};
	
	public static String[] snames=
	{
		"joint-pelvistojoint-spine3",
		//"joint-spine3tojoint-pelvis",
		"joint-spine3tojoint-spine2",
		"joint-spine2tojoint-spine1",
		"joint-spine1tojoint-neck",
		"joint-necktojoint-head",
		"joint-headtojoint-head2",
		"joint-pelvistojoint-r-upper-leg",
		"joint-r-upper-legtojoint-r-knee",
		"joint-r-kneetojoint-r-ankle",
		"joint-r-ankletojoint-r-foot-1",
		"joint-r-foot-1tojoint-r-foot-2",
		"joint-pelvistojoint-l-upper-leg",
		"joint-l-upper-legtojoint-l-knee",
		"joint-l-kneetojoint-l-ankle",
		"joint-l-ankletojoint-l-foot-1",
		"joint-l-foot-1tojoint-l-foot-2",
		"joint-head2tojoint-mouth",
		"joint-head2tojoint-l-eye",
		"joint-head2tojoint-r-eye",
		"joint-necktojoint-l-clavicle",
		"joint-necktojoint-r-clavicle",
		"joint-mouthtojoint-jaw",
		"joint-l-eyetojoint-l-eye-target",
		"joint-r-eyetojoint-r-eye-target",
		"joint-l-eyetojoint-l-upperlid",
		"joint-l-eyetojoint-l-lowerlid",
		"joint-r-eyetojoint-r-upperlid",
		"joint-r-eyetojoint-r-lowerlid",
		"joint-r-clavicletojoint-r-shoulder",
		"joint-r-shouldertojoint-r-elbow",
		"joint-r-elbowtojoint-r-hand",
		"joint-r-handtojoint-r-finger-3-1",
		"joint-r-handtojoint-r-hand-3",
		"joint-r-handtojoint-r-hand-2",
		"joint-r-handtojoint-r-finger-1-1",
		"joint-r-hand-3tojoint-r-finger-2-1",
		"joint-r-hand-3tojoint-r-finger-3-1",
		"joint-r-hand-2tojoint-r-finger-4-1",
		"joint-r-hand-2tojoint-r-finger-5-1",
		"joint-l-clavicletojoint-l-shoulder",
		"joint-l-shouldertojoint-l-elbow",
		"joint-l-elbowtojoint-l-hand",
		"joint-l-handtojoint-l-finger-3-1",
		"joint-l-handtojoint-l-hand-3",
		"joint-l-handtojoint-l-hand-2",
		"joint-l-handtojoint-l-finger-1-1",
		"joint-l-hand-3tojoint-l-finger-2-1",
		"joint-l-hand-3tojoint-l-finger-3-1",
		"joint-l-hand-2tojoint-l-finger-4-1",
		"joint-l-hand-2tojoint-l-finger-5-1",
		"joint-r-finger-1-1tojoint-r-finger-1-2",
		"joint-r-finger-1-2tojoint-r-finger-1-3",
		"joint-r-finger-1-3tojoint-r-finger-1-4",
		"joint-r-finger-2-1tojoint-r-finger-2-2",
		"joint-r-finger-2-2tojoint-r-finger-2-3",
		"joint-r-finger-2-3tojoint-r-finger-2-4",
		"joint-r-finger-3-1tojoint-r-finger-3-2",
		"joint-r-finger-3-2tojoint-r-finger-3-3",
		"joint-r-finger-3-3tojoint-r-finger-3-4",
		"joint-r-finger-4-1tojoint-r-finger-4-2",
		"joint-r-finger-4-2tojoint-r-finger-4-3",
		"joint-r-finger-4-3tojoint-r-finger-4-4",
		"joint-r-finger-5-1tojoint-r-finger-5-2",
		"joint-r-finger-5-2tojoint-r-finger-5-3",
		"joint-r-finger-5-3tojoint-r-finger-5-4",
		"joint-l-finger-1-1tojoint-l-finger-1-2",
		"joint-l-finger-1-2tojoint-l-finger-1-3",
		"joint-l-finger-1-3tojoint-l-finger-1-4",
		"joint-l-finger-2-1tojoint-l-finger-2-2",
		"joint-l-finger-2-2tojoint-l-finger-2-3",
		"joint-l-finger-2-3tojoint-l-finger-2-4",
		"joint-l-finger-3-1tojoint-l-finger-3-2",
		"joint-l-finger-3-2tojoint-l-finger-3-3",
		"joint-l-finger-3-3tojoint-l-finger-3-4",
		"joint-l-finger-4-1tojoint-l-finger-4-2",
		"joint-l-finger-4-2tojoint-l-finger-4-3",
		"joint-l-finger-4-3tojoint-l-finger-4-4",
		"joint-l-finger-5-1tojoint-l-finger-5-2",
		"joint-l-finger-5-2tojoint-l-finger-5-3",
		"joint-l-finger-5-3tojoint-l-finger-5-4",
		"joint-jawtojoint-tongue-1",
		"joint-tongue-1tojoint-tongue-2",
		"joint-tongue-2tojoint-tongue-3",
		"joint-tongue-3tojoint-tongue-4",
	};
	
	public static final int BONE_HipstoChest0=0;
	//"Hips=;
	public static final int BONE_HipstoChest1=1;
	public static final int BONE_HipstoChest2=2;
	public static final int BONE_ChesttoNeck=3;
	public static final int BONE_NecktoHead=4;
	public static final int BONE_HeadtoHeadend=5;
	public static final int BONE_Hip_L=6;
	public static final int BONE_LeftUpLegtoLeftLowLeg=7;
	public static final int BONE_LeftLowLegtoLeftFoot=8;
	public static final int BONE_LeftFoottoLeftFootend=9;
	public static final int BONE_Toe_L=10;
	public static final int BONE_Hip_R=11;
	public static final int BONE_RightUpLegtoRightLowLeg=12;
	public static final int BONE_RightLowLegtoRightFoot=13;
	public static final int BONE_RightFoottoRightFootend=14;
	public static final int BONE_Toe_R=15;
	public static final int BONE_HeadtoMouth=16;
	public static final int BONE_HeadtoLEye=17;
	public static final int BONE_HeadtoREye=18;
	public static final int BONE_Spine3toLClavicle=19;
	public static final int BONE_Spine3toRClavicle=20;
	public static final int BONE_Jaw=21;
	public static final int BONE_Eye_R=22;
	public static final int BONE_Eye_L=23;
	public static final int BONE_UpLid_R=24;
	public static final int BONE_LoLid_R=25;
	public static final int BONE_UpLid_L=26;
	public static final int BONE_LoLid_L=27;
	public static final int BONE_LeftCollartoLeftUpArm=28;
	public static final int BONE_LeftUpArmtoLeftLowArm=29;
	public static final int BONE_LeftLowArmtoLeftHand=30;
	public static final int BONE_LeftHandtoLeftHandend=31;
	public static final int BONE_Wrist_1_L=32;
	public static final int BONE_Wrist_2_L=33;
	public static final int BONE_Palm_1_L=34;
	public static final int BONE_Palm_2_L=35;
	public static final int BONE_Palm_3_L=36;
	public static final int BONE_Palm_4_L=37;
	public static final int BONE_Palm_5_L=38;
	public static final int BONE_RightCollartoRightUpArm=39;
	public static final int BONE_RightUpArmtoRightLowArm=40;
	public static final int BONE_RightLowArmtoRightHand=41;
	public static final int BONE_RightHandtoRightHandend=42;
	public static final int BONE_Wrist_1_R=43;
	public static final int BONE_Wrist_2_R=44;
	public static final int BONE_Palm_1_R=45;
	public static final int BONE_Palm_2_R=46;
	public static final int BONE_Palm_3_R=47;
	public static final int BONE_Palm_4_R=48;
	public static final int BONE_Palm_5_R=49;
	public static final int BONE_Finger_1_1_L=50;
	public static final int BONE_Finger_1_2_L=51;
	public static final int BONE_Finger_1_3_L=52;
	public static final int BONE_Finger_2_1_L=53;
	public static final int BONE_Finger_2_2_L=54;
	public static final int BONE_Finger_2_3_L=55;
	public static final int BONE_Finger_3_1_L=56;
	public static final int BONE_Finger_3_2_L=57;
	public static final int BONE_Finger_3_3_L=58;
	public static final int BONE_Finger_4_1_L=59;
	public static final int BONE_Finger_4_2_L=60;
	public static final int BONE_Finger_4_3_L=61;
	public static final int BONE_Finger_5_1_L=62;
	public static final int BONE_Finger_5_2_L=63;
	public static final int BONE_Finger_5_3_L=64;
	public static final int BONE_Finger_1_1_R=65;
	public static final int BONE_Finger_1_2_R=66;
	public static final int BONE_Finger_1_3_R=67;
	public static final int BONE_Finger_2_1_R=68;
	public static final int BONE_Finger_2_2_R=69;
	public static final int BONE_Finger_2_3_R=70;
	public static final int BONE_Finger_3_1_R=71;
	public static final int BONE_Finger_3_2_R=72;
	public static final int BONE_Finger_3_3_R=73;
	public static final int BONE_Finger_4_1_R=74;
	public static final int BONE_Finger_4_2_R=75;
	public static final int BONE_Finger_4_3_R=76;
	public static final int BONE_Finger_5_1_R=77;
	public static final int BONE_Finger_5_2_R=78;
	public static final int BONE_Finger_5_3_R=79;
	public static final int BONE_JawtoTongue=80;
	public static final int BONE_TongueBase=81;
	public static final int BONE_TongueMid=82;
	public static final int BONE_TongueTip=83;
	
	
	public static String[] names=
	{
		"HipstoChest0",
		//"Hips",
		"HipstoChest1",
		"HipstoChest2",
		"ChesttoNeck",
		"NecktoHead",
		"HeadtoHeadend",
		"Hip_L",
		"LeftUpLegtoLeftLowLeg",
		"LeftLowLegtoLeftFoot",
		"LeftFoottoLeftFootend",
		"Toe_L",
		"Hip_R",
		"RightUpLegtoRightLowLeg",
		"RightLowLegtoRightFoot",
		"RightFoottoRightFootend",
		"Toe_R",
		"HeadtoMouth",
		"HeadtoLEye",
		"HeadtoREye",
		"Spine3toLClavicle",
		"Spine3toRClavicle",
		"Jaw",
		"Eye_R",
		"Eye_L",
		"UpLid_R",
		"LoLid_R",
		"UpLid_L",
		"LoLid_L",
		"LeftCollartoLeftUpArm",
		"LeftUpArmtoLeftLowArm",
		"LeftLowArmtoLeftHand",
		"LeftHandtoLeftHandend",
		"Wrist-1_L",
		"Wrist-2_L",
		"Palm-1_L",
		"Palm-2_L",
		"Palm-3_L",
		"Palm-4_L",
		"Palm-5_L",
		"RightCollartoRightUpArm",
		"RightUpArmtoRightLowArm",
		"RightLowArmtoRightHand",
		"RightHandtoRightHandend",
		"Wrist-1_R",
		"Wrist-2_R",
		"Palm-1_R",
		"Palm-2_R",
		"Palm-3_R",
		"Palm-4_R",
		"Palm-5_R",
		"Finger-1-1_L",
		"Finger-1-2_L",
		"Finger-1-3_L",
		"Finger-2-1_L",
		"Finger-2-2_L",
		"Finger-2-3_L",
		"Finger-3-1_L",
		"Finger-3-2_L",
		"Finger-3-3_L",
		"Finger-4-1_L",
		"Finger-4-2_L",
		"Finger-4-3_L",
		"Finger-5-1_L",
		"Finger-5-2_L",
		"Finger-5-3_L",
		"Finger-1-1_R",
		"Finger-1-2_R",
		"Finger-1-3_R",
		"Finger-2-1_R",
		"Finger-2-2_R",
		"Finger-2-3_R",
		"Finger-3-1_R",
		"Finger-3-2_R",
		"Finger-3-3_R",
		"Finger-4-1_R",
		"Finger-4-2_R",
		"Finger-4-3_R",
		"Finger-5-1_R",
		"Finger-5-2_R",
		"Finger-5-3_R",
		"JawtoTongue",
		"TongueBase",
		"TongueMid",
		"TongueTip",
	};
	
	public static int[] mirror =
	{
		-1,
		//"Hips",
		-1,
		-1,
		-1,
		-1,
		-1,
		BONE_Hip_R,
		BONE_RightUpLegtoRightLowLeg,
		BONE_RightLowLegtoRightFoot,
		BONE_RightFoottoRightFootend,
		BONE_Toe_R,
		BONE_Hip_L,
		BONE_LeftUpLegtoLeftLowLeg,
		BONE_LeftLowLegtoLeftFoot,
		BONE_LeftFoottoLeftFootend,
		BONE_Toe_L,
		-1,
		BONE_HeadtoREye,
		BONE_HeadtoLEye,
		BONE_Spine3toRClavicle,
		BONE_Spine3toLClavicle,
		-1,
		BONE_Eye_L,
		BONE_Eye_R,
		BONE_UpLid_L,
		BONE_LoLid_L,
		BONE_UpLid_R,
		BONE_LoLid_R,
		BONE_RightCollartoRightUpArm,
		BONE_RightUpArmtoRightLowArm,
		BONE_RightLowArmtoRightHand,
		BONE_RightHandtoRightHandend,
		BONE_Wrist_1_R,
		BONE_Wrist_2_R,
		BONE_Palm_1_R,
		BONE_Palm_2_R,
		BONE_Palm_3_R,
		BONE_Palm_4_R,
		BONE_Palm_5_R,
		BONE_LeftCollartoLeftUpArm,
		BONE_LeftUpArmtoLeftLowArm,
		BONE_LeftLowArmtoLeftHand,
		BONE_LeftHandtoLeftHandend,
		BONE_Wrist_1_L,
		BONE_Wrist_2_L,
		BONE_Palm_1_L,
		BONE_Palm_2_L,
		BONE_Palm_3_L,
		BONE_Palm_4_L,
		BONE_Palm_5_L,
		BONE_Finger_1_1_R,
		BONE_Finger_1_2_R,
		BONE_Finger_1_3_R,
		BONE_Finger_2_1_R,
		BONE_Finger_2_2_R,
		BONE_Finger_2_3_R,
		BONE_Finger_3_1_R,
		BONE_Finger_3_2_R,
		BONE_Finger_3_3_R,
		BONE_Finger_4_1_R,
		BONE_Finger_4_2_R,
		BONE_Finger_4_3_R,
		BONE_Finger_5_1_R,
		BONE_Finger_5_2_R,
		BONE_Finger_5_3_R,
		BONE_Finger_1_1_L,
		BONE_Finger_1_2_L,
		BONE_Finger_1_3_L,
		BONE_Finger_2_1_L,
		BONE_Finger_2_2_L,
		BONE_Finger_2_3_L,
		BONE_Finger_3_1_L,
		BONE_Finger_3_2_L,
		BONE_Finger_3_3_L,
		BONE_Finger_4_1_L,
		BONE_Finger_4_2_L,
		BONE_Finger_4_3_L,
		BONE_Finger_5_1_L,
		BONE_Finger_5_2_L,
		BONE_Finger_5_3_L,
		-1,
		-1,
		-1,
		-1,
	};
}
