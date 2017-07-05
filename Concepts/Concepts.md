# Concepts

Concepts serve two purposes: 

1. To enable an AI to infer and plan
2. To enable communication and learning between humans and AI

A system that is purely for AI inference may be incomprehensible to humans e.g. a deep learning model. Likewise a system purely for expressing concepts that are meaningful for humans may lack sufficient detail to enable simulation based inference and planning. The objective of this project is to combine the best qualities of both using a game-like representation for commonsense knowledge.

As with other commonsense knowledge datasets, the concepts are stored without a hierarchy. Multiple different hierarchies of the data are available in the Taxonomies section to support different perspectives on the data. 

In order to turn concepts into a game-like representation they are given types. These types are similar to those that might be used within a computer game to represent an interactive simulation of the world. Rather than a purely abstract triple store graph representation, the type system aims to impose more structure on the knowledge to make common cases simple to describe and simulate. As needed, it may be necesary to create exceptions for aspects of the world that don't fit this structure. While this is potentially less elegant in an abstract sense it is more practical for a game-like simulation.

Each instance of a concept can have a language representation (as might be used for a text adventure game) and a physical representation (as might be used for realistic 3d computer game).

Types include:

* Object - a thing (currently no distinction between living things and non-living things)
* Room - an area where something is
* Portal - a normal method through which things travel between rooms
* Map - a collection of rooms joined with portals that define a meaningful area e.g. a floorplan of a building

* Sound - Like a smash when a glass breaks
* Medium - The material that objects are within in a room e.g. smokey air, chlorinated swimming pool water etc.
* Smell - Like an atmospheric object
* Statement - A gesture or spoken action for the purpose of communicating
* Material - Something a thing is made of

* Pose - A recognisable positioning of adjustable parts of an object e.g. bones/facial muscles/hair/cloth of an object (can refer to only part of an object)
* Action - A change to the state of the world. Can create or destroy statements, sounds, objects etc. Actions are parameterisable e.g. action_pickup_from_ground_with_left_hand would be parameterisable with the object that is being picked up.
* Story - An initial state and a set of actions that occur to one or more of the objects. 

* Digital/Written - There are digital or written versions of the core types so that activities involving computers/books can be understood e.g. DigitalObject such as a jpeg image, DigitalRoom such as a folder, DigitalAction such as create a new file, WrittenRoom such as a page, WrittenMap such as a book


Each concept requires a unique name which is human and machine readable. Another objective is that it is relatively easy to find concepts using existing file system tools of operating systems. Current proposed format for concept names:

type_cn_conceptnetname_wn31_wordnetid

e.g.

room_cn_bedroom_wn31_n104112987

As wordnet has very different ids for different versions the wordnet version needs to be included with the id

If wordnet or conceptnet examples don't exist then leave them out. If there is no conceptnet equivalent then create a new name so that concepts always have a human readable name. Ultimately we should aim to contribute back to conceptnet where this makes sense and is possible.

pose_my_strange_pose

