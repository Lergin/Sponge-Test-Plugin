package de.lergin.sponge.test;

import com.google.inject.Inject;
import de.lergin.sponge.test.deathCanon.DeathCanonCommand;
import de.lergin.sponge.test.supplyDrop.SupplyDropLandListener;
import de.lergin.sponge.test.supplyDrop.SupplyDropCommand;
import de.lergin.sponge.test.waterLighting.WaterLightingCommand;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(
        name = "de.lergin.sponge.tests.TestPlugin",
        id = "de.lergin.sponge.test",
        version = "0.1.0",
        authors = {"Lergin"},
        description = "plugin with tests")
public class TestPlugin {
    private static TestPlugin instance;

    public static TestPlugin instance(){
        return instance;
    }

    @Inject
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }


    @Listener
    public void gameConstruct(GameConstructionEvent event) {
        instance = this;
    }


    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
        CommandSpec supplyDropCommandSpec = CommandSpec.builder()
                .executor(new SupplyDropCommand())
                .build();

        CommandSpec waterLightningCommandSpec = CommandSpec.builder()
                .executor(new WaterLightingCommand())
                .build();

        CommandSpec deathCanonCommandSpec = CommandSpec.builder()
                .executor(new DeathCanonCommand())
                .build();

        CommandSpec mainCommandSpec = CommandSpec.builder()
                .description(Text.of("Main Test Plugin command"))
                .child(supplyDropCommandSpec, "supplyDrop")
                .child(waterLightningCommandSpec, "waterLightning")
                .child(deathCanonCommandSpec, "deathCanon")
                .build();

        Sponge.getCommandManager().register(this, mainCommandSpec, "tests");


        Sponge.getEventManager().registerListeners(this, new SupplyDropLandListener());
    }
}
