package com;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
	name = "Probability Calculator"
)
public class ProbabilityCalculatorPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ProbabilityCalculatorConfig config;

	@Inject
    private ClientToolbar clientToolbar;

	private ProbabilityCalculatorPanel panel;
	private NavigationButton navButton;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Prob calc started!");
		panel = new ProbabilityCalculatorPanel(new ProbabilityCalculatorInputArea(),
				new ProbabilityCalculatorOutputArea(0.002, 10000, 20));
		panel.init(config);

		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(ProbabilityCalculatorPlugin.class, "probabilitycalculator_icon.png");

		navButton = NavigationButton.builder()
				.tooltip("Probability Calculator")
				.icon(icon)
				.priority(7)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Prob calc stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Provides
	ProbabilityCalculatorConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ProbabilityCalculatorConfig.class);
	}
}
