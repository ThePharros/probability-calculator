package pharros.probabilitycalculator;

import com.google.inject.Provides;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

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
    private double dropRate = 1.0 / 100;
    private int killCount = 100;
    private int dropsReceived = 1;

    @Override
    protected void startUp()
    {
        log.info("prob-calc: Plugin started!");
        input = new ProbabilityCalculatorInputArea();
        output = new ProbabilityCalculatorOutputArea(dropRate, killCount, dropsReceived, config);
        panel = new ProbabilityCalculatorPanel(input, output);
        //panel.init(config);

        final BufferedImage icon = ImageUtil.getResourceStreamFromClass(ProbabilityCalculatorPlugin.class, "probabilitycalculator_icon.png");

        //Action listeners
        input.getUiDropRate().addActionListener(e ->
            onFieldDropRateUpdated());
        input.getUiKillCount().addActionListener(e ->
            onFieldKillCountUpdated());
        input.getUiDropsReceived().addActionListener(e ->
            onFieldDropsReceivedUpdated());

        //Focus listeners
        input.getUiDropRate().addFocusListener(buildFocusAdapter(e -> onFieldDropRateUpdated()));
        input.getUiKillCount().addFocusListener(buildFocusAdapter(e -> onFieldKillCountUpdated()));
        input.getUiDropsReceived().addFocusListener(buildFocusAdapter(e -> onFieldDropsReceivedUpdated()));

        updateInputFields();

        navButton = NavigationButton.builder()
            .tooltip("Probability Calculator")
            .icon(icon)
            .priority(7)
            .panel(panel)
            .build();

        clientToolbar.addNavigation(navButton);

    }

    private void onFieldDropRateUpdated()
    {
        if (input.getDropRateInput().contains("/"))
        {
            String num = input.getDropRateInput().split("/")[0];
            String den = input.getDropRateInput().split("/")[1];
            dropRate = Double.parseDouble(num) / Double.parseDouble(den);
        } else
        {
            dropRate = Double.parseDouble(input.getDropRateInput());
        }
        updateInputFields();
    }

    private void onFieldKillCountUpdated()
    {
        killCount = input.getKillCountInput();
        updateInputFields();
    }

    private void onFieldDropsReceivedUpdated()
    {
        dropsReceived = input.getDropsReceivedInput();
        updateInputFields();
    }

    private void updateInputFields()
    {
        input.setDropRateInput(input.getDropRateInput());
        input.setDropsReceivedInput(dropsReceived);
        input.setKillCountInput(killCount);

        output.setDropRate(dropRate);
        output.setKillCount(killCount);
        output.setDropsReceived(dropsReceived);
        output.updateTextArea();

        log.info("prob-calc: Input fields updated!");
    }

    private FocusAdapter buildFocusAdapter(Consumer<FocusEvent> focusLostConsumer)
    {
        return new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                focusLostConsumer.accept(e);
            }
        };
    }

    @Override
    protected void shutDown()
    {
        clientToolbar.removeNavigation(navButton);
        log.info("prob-calc: Plugin stopped!");
    }

    @Provides
    ProbabilityCalculatorConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ProbabilityCalculatorConfig.class);
    }
}
