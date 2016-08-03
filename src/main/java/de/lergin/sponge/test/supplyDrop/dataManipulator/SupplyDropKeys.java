package de.lergin.sponge.test.supplyDrop.dataManipulator;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.item.inventory.Inventory;

import static org.spongepowered.api.data.DataQuery.of;
import static org.spongepowered.api.data.key.KeyFactory.makeSingleKey;

public class SupplyDropKeys {
    public static final Key<Value<Inventory>> SUPPLY_DROP_INVENTORY = makeSingleKey(Inventory.class, Value.class, of("SupplyDropInventory"));

}
