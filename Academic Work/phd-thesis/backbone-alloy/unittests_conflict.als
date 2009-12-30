module unittests_redefinition

open bb

run conflict for 3 but exactly 4 Stratum, 5 Element, exactly 4 Component, exactly 1 Interface, 4 bb/full/Ports/Deltas -- , exactly 1 Index
// generates a conflict that isn't resolved: replaced parts will be the same.  runs in about 197395ms
pred conflict()
{
	no parent
	no links
	Model::errorsAllowedInTopOnly[]	
	no isTop.True.ownedElements
	some isInvalid_e
	one c : Component | no c.isComposite
}


////open conflictandresolution
run conflictAndResolution for 3 but
	exactly 5 Stratum,
	exactly 6 Element,
	exactly 5 Component,
	exactly 1 Interface,
	exactly 5 bb/full/Ports/Deltas,
	exactly 4 bb/full/Parts/Deltas,
	exactly 4 bb/full/Connectors/Deltas,
	exactly 0 bb/full/Operations/Deltas,
	exactly 4 Part,
	5 LinkEnd
// generates a conflict & resolves it: types of replaced parts will be the same.
// takes 9737s on core2duo machine
pred conflictAndResolution
{
	no parent
	no links
	some isInvalid_e
	some disj s1, s2, s3, s4, s5: Stratum
	{
		s1.dependsOn = s2
		s2.dependsOn = s3 + s4
		s3.dependsOn = s5
		s4.dependsOn = s5
		no s2.ownedElements
		Model::errorsOnlyAllowedIn[s2]
	}
}
////close conflictandresolution

run conflictAndResolutionX for 3 but
	exactly 5 Stratum,
	exactly 6 Element,
	exactly 5 Component,
	exactly 1 Interface,
	exactly 5 bb/full/Ports/Deltas,
	exactly 4 bb/full/Parts/Deltas,
	exactly 4 bb/full/Connectors/Deltas,
	exactly 0 bb/full/Operations/Deltas,
	exactly 4 Part,
	5 LinkEnd
// generates a conflict and resolves it: replaced parts will be the same.  runs in 9737863ms
pred conflictAndResolutionX
{
	no parent
	no links
	some isInvalid_e
	some disj s1, s2, s3, s4, s5: Stratum
	{
		s1.dependsOn = s2
		s2.dependsOn = s3 + s4
		s3.dependsOn = s5
		s4.dependsOn = s5
		no s2.ownedElements
		Model::errorsOnlyAllowedIn[s2]
		s5 not in (s5.ownedElements & Component).isComposite
	}
}