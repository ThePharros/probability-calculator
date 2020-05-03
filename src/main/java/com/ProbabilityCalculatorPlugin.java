package com;

import com.google.inject.Provides;
import javax.inject.Inject;
import javax.swing.*;

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
	private ProbabilityCalculatorInputArea input;
	private ProbabilityCalculatorOutputArea output;
	private NavigationButton navButton;
	private double dropRate = 1.0/5;
	private int killCount = 20;
	private int dropsReceived = 4;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Prob calc started!");
		input = new ProbabilityCalculatorInputArea();
		output = new ProbabilityCalculatorOutputArea(dropRate, killCount, dropsReceived);
		panel = new ProbabilityCalculatorPanel(input, output);
		panel.init(config);

		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(ProbabilityCalculatorPlugin.class, "probabilitycalculator_icon.png");

		input.getUiDropRate().addActionListener(e -> {
			onFieldDropRateUpdated();
			input.getUiKillCount().requestFocusInWindow();
		});
		input.getUiKillCount().addActionListener(e -> {
			onFieldKillCountUpdated();
			input.getUiDropsReceived().requestFocusInWindow();
		});
		input.getUiDropsReceived().addActionListener(e -> {
			onFieldDropsReceivedUpdated();
		});

		updateInputFields();

		navButton = NavigationButton.builder()
				.tooltip("Probability Calculator")
				.icon(icon)
				.priority(7)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	private void onFieldDropRateUpdated() {
		dropRate = input.getDropRateInput();
		updateInputFields();
	}

	private void onFieldKillCountUpdated() {
		killCount = (int)input.getKillCountInput();
		updateInputFields();
	}

	private void onFieldDropsReceivedUpdated() {
		dropsReceived = (int)input.getDropsReceivedInput();
		updateInputFields();
	}

	private void updateInputFields() {
		input.setDropRateInput(dropRate);
		input.setDropsReceivedInput(dropsReceived);
		input.setKillCountInput(killCount);
		output.setDropRate(dropRate);
		output.setKillCount(killCount);
		output.setDropsReceived(dropsReceived);
		output.updateTextArea();

		log.info("Input fields updated!");
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
