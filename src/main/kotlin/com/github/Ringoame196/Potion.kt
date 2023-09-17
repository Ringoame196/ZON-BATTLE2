package com.github.Ringoame196

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ThrownPotion
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Potion {
    fun summon(location: Location, potionType: PotionEffectType) {
        val item = ItemStack(Material.LINGERING_POTION)
        val meta = item.itemMeta as PotionMeta
        val regenerationEffect = PotionEffect(potionType, 1 * 20, 1)
        meta.addCustomEffect(regenerationEffect, true)
        // アイテムにメタデータを設定
        item.itemMeta = meta
        // スプラッシュポーションを生成し、指定したロケーションにスポーンさせる
        val splashPotion = location.world?.spawn(location, ThrownPotion::class.java)
        // スプラッシュポーションにアイテムをセット
        splashPotion?.setItem(item)
    }
}
