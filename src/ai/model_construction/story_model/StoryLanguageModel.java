package ai.model_construction.story_model;

public class StoryLanguageModel 
{
	//All models consist of a parameterised generative system
	//and a set of constraints
	
	//starts with a state model to identify the start of the story
	//then something like a flow chart (or more accurately a probabilistic finite state machine)
	//each state can have sub state machines e.g. a story for a working day might include get washed
	//get washed might be it's own story model
	
	//can be used to create instances directly
	//or can create more specific models that have sub elements that are shorter parts
	//e.g. a life model may include instances modelled in detail and other parts that are hazy
}
