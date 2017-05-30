package ai.knowledge_representation.state_instance;

import java.io.Serializable;

public class SoundPhysicalInstance implements Serializable
{
	//Could be a recorded sound
	//or could be synthesised using midi or other procedural techniques
	
	//An mp3, ogg or wav file
	public String soundFile;

	public double timeThroughFile = 0.0;
	
	//where in space the sound is originating from

}
