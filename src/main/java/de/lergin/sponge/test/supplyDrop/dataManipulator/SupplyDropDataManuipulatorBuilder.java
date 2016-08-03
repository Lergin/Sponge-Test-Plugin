package de.lergin.sponge.test.supplyDrop.dataManipulator;

import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.item.inventory.Inventory;

import java.util.*;

public class SupplyDropDataManuipulatorBuilder extends AbstractDataBuilder<SupplyDropData> {
    protected SupplyDropDataManuipulatorBuilder(Class<SupplyDropData> requiredClass, int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<SupplyDropData> buildContent(DataView dataView) throws InvalidDataException {
        if (dataView.contains(SupplyDropKeys.SUPPLY_DROP_INVENTORY.getQuery())) {

            Optional<Inventory> optional = dataView.getObject(SupplyDropKeys.SUPPLY_DROP_INVENTORY.getQuery(), Inventory.class);

            if(!optional.isPresent())
                return Optional.empty();

            return Optional.of(new SupplyDropData(optional.get()));
        }
        return Optional.empty();
    }
}
