package com.hopstepjump.backbone.parserbase;

public class StringMatch extends Match
{
	public StringMatch(IAction action)
	{
		super(new StringPatternMatcher(), action);
	}
}