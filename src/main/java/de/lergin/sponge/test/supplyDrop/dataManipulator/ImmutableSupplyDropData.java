package de.lergin.sponge.test.supplyDrop.dataManipulator;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.item.inventory.Inventory;

public class ImmutableSupplyDropData extends AbstractImmutableSingleData<Inventory, ImmutableSupplyDropData, SupplyDropData> {
    Inventory inventory;

    protected ImmutableSupplyDropData(Inventory value) {
        super(value, SupplyDropKeys.SUPPLY_DROP_INVENTORY);
        this.inventory = value;
    }

    @Override
    protected ImmutableValue<?> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(SupplyDropKeys.SUPPLY_DROP_INVENTORY, this.inventory).asImmutable();
    }

    @Override
    public SupplyDropData asMutable() {
        return new SupplyDropData(inventory);
    }

    @Override
    public int compareTo(ImmutableSupplyDropData o) {
        return 0;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
