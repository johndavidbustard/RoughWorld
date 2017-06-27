package ai.sensing.sensed_estimate;

public class SensedEstimate
{
	//Like a story instance except with extra information about probabilities
	
	//depending on how the sensing system works this may take different forms approximating a probability distribution of states and actions
	//common methods include simple confidence values for the presence of objects
	//or particle filtering where there are a fixed number of hypothetical models with confidences at different instances in time
	
	//Something like yolo will just give you a set of concept names and where they are in an image
	//SMPlify will give you pose information for detected people
	//Video sources may provide more data about tracked state over time
}
