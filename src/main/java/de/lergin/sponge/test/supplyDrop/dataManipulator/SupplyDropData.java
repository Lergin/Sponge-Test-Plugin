package de.lergin.sponge.test.supplyDrop.dataManipulator;

import com.google.common.base.Preconditions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.item.inventory.Inventory;

import java.util.Optional;

public class SupplyDropData extends AbstractSingleData<Inventory, SupplyDropData, ImmutableSupplyDropData> {
    Inventory inventory;

    public SupplyDropData(Inventory value) {
        super(value, SupplyDropKeys.SUPPLY_DROP_INVENTORY);
        this.inventory = value;
    }

    @Override
    protected Value<?> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(SupplyDropKeys.SUPPLY_DROP_INVENTORY, this.inventory);
    }

    @Override
    public ImmutableSupplyDropData asImmutable() {
        return new ImmutableSupplyDropData(inventory);
    }

    @Override
    public int compareTo(SupplyDropData supplyDropData) {
        return 0;
    }

    @Override
    public Optional<SupplyDropData> fill(DataHolder dataHolder, MergeFunction mergeFunction) {
        SupplyDropData supplyDropData = Preconditions.checkNotNull(mergeFunction).merge(copy(),
                dataHolder.get(SupplyDropData.class).orElse(copy()));

        return Optional.of(supplyDropData);
    }

    @Override
    public Optional<SupplyDropData> from(DataContainer dataContainer) {
        if (!dataContainer.contains(SupplyDropKeys.SUPPLY_DROP_INVENTORY.getQuery())) {
            return Optional.empty();
        }
        this.inventory = dataContainer.getObject(SupplyDropKeys.SUPPLY_DROP_INVENTORY.getQuery(), Inventory.class).get();
        return Optional.of(this);
    }

    @Override
    public SupplyDropData copy() {
        return new SupplyDropData(inventory);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
