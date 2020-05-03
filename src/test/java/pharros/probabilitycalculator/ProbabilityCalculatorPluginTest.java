package pharros.probabilitycalculator;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ProbabilityCalculatorPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(ProbabilityCalculatorPlugin.class);
		RuneLite.main(args);
	}
}