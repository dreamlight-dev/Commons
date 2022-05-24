package com.dreamlightbuilds.commons.bukkit.item;

import com.cryptomorin.xseries.SkullUtils;
import com.dreamlightbuilds.commons.Common;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.function.Consumer;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(final Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(final ItemStack item) {
        this.item = Objects.requireNonNull(item, "item");
        this.meta = item.getItemMeta();

        if (this.meta == null) {
            throw new IllegalArgumentException("The type " + item.getType() + " doesn't support item meta");
        }
    }

    public ItemBuilder type(final Material material) {
        this.item.setType(material);
        return this;
    }

    public ItemBuilder amount(final int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(final Enchantment enchantment) {
        return enchant(enchantment, 1);
    }

    public ItemBuilder enchant(final Enchantment enchantment, final int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder removeEnchant(final Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder removeEnchants() {
        this.meta.getEnchants().keySet().forEach(this.meta::removeEnchant);
        return this;
    }

    public ItemBuilder meta(final Consumer<ItemMeta> consumer) {
        consumer.accept(this.meta);
        return this;
    }

    public <T extends ItemMeta> ItemBuilder meta(final Class<T> metaClass, final Consumer<T> metaConsumer) {
        if (metaClass.isInstance(this.meta)) {
            metaConsumer.accept(metaClass.cast(this.meta));
        }
        return this;
    }

    public ItemBuilder name(final String name) {
        this.meta.setDisplayName(Common.color(name));
        return this;
    }

    public ItemBuilder lore(final String lore) {
        return lore(Collections.singletonList(lore));
    }

    public ItemBuilder lore(final String... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder lore(final List<String> lore) {
        this.meta.setLore(Common.color(lore));
        return this;
    }

    public ItemBuilder addLore(final String line) {
        List<String> lore = this.meta.getLore();

        if (lore == null) {
            return lore(line);
        }

        lore.add(line);
        return lore(lore);
    }

    public ItemBuilder addLore(final String... lines) {
        return addLore(Arrays.asList(lines));
    }

    public ItemBuilder addLore(final List<String> lines) {
        List<String> lore = this.meta.getLore();

        if (lore == null) {
            return lore(lines);
        }

        lore.addAll(lines);
        return lore(lore);
    }

    public ItemBuilder flags(final ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder flags() {
        return flags(ItemFlag.values());
    }

    public ItemBuilder removeFlags(final ItemFlag... flags) {
        this.meta.removeItemFlags(flags);
        return this;
    }

    public ItemBuilder removeFlags() {
        return removeFlags(ItemFlag.values());
    }

    public ItemBuilder armorColor(final Color color) {
        return meta(LeatherArmorMeta.class, m -> m.setColor(color));
    }

    public ItemBuilder armorColor(final int red, final int green, final int blue) {
        return armorColor(Color.fromRGB(red, green, blue));
    }

    public ItemBuilder customModelData(int data){
        this.meta.setCustomModelData(data);
        return this;
    }

    public ItemBuilder pdc(final NamespacedKey key, final String s){
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, s);
        return this;
    }

    public ItemBuilder pdc(final NamespacedKey key, final Double d){
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, d);
        return this;
    }

    public ItemBuilder pdc(final NamespacedKey key, final Float f){
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.FLOAT, f);
        return this;
    }

    public ItemBuilder pdc(final NamespacedKey key, final Integer i){
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, i);
        return this;
    }

    public ItemBuilder pdc(final NamespacedKey key, final Long l){
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.LONG, l);
        return this;
    }

    public ItemBuilder pdc(final NamespacedKey key, final Byte b){
        this.meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, b);
        return this;
    }

    public ItemBuilder skull(final String identifier){
        SkullUtils.applySkin(this.meta, identifier);
        return this;
    }

    public ItemBuilder skull(final OfflinePlayer identifier){
        SkullUtils.applySkin(this.meta, identifier);
        return this;
    }

    public ItemBuilder skull(final UUID identifier){
        SkullUtils.applySkin(this.meta, identifier);
        return this;
    }

    public ItemStack build() {
        this.item.setItemMeta(this.meta);
        return this.item;
    }

}
