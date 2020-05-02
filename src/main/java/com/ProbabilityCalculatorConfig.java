package com;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("probability_calculator")
public interface ProbabilityCalculatorConfig extends Config
{
	@ConfigItem(
		keyName = "decimalPlaces",
		name = "Decimal Places",
		description = "The number of decimal places to round the output to"
	)
	default int greeting()
	{
		return 4;
	}
}
