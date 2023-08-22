package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.TeamData
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class GUI {
    fun selectGUI(player: Player) {
        val gui = Bukkit.createInventory(null, 27, "${ChatColor.DARK_GREEN}選択画面[BATTLEGUI]")
        setGUIitem(gui, 10, Material.BEACON, "強化", "", true)
        setGUIitem(gui, 12, Material.ANVIL, "金床", "", true)
        setGUIitem(gui, 14, Material.CHEST, "買い物", "", true)
        setGUIitem(gui, 16, Material.ENDER_CHEST, "チームチェスト", "", true)
        player.openInventory(gui)
    }
    fun setGUIitem(GUI: Inventory, number: Int, set_item: Material, displayname: String, lore: String, unbreakable: Boolean) {
        // GUIにアイテムを楽にセットする
        val item = lore(set_item, displayname, lore)
        val itemMeta: ItemMeta? = item.itemMeta
        if (unbreakable) { itemMeta?.isUnbreakable = true } // 不破壊
        item.setItemMeta(itemMeta)
        GUI.setItem(number, item)
    }
    @Suppress("DEPRECATION")
    fun set_playerHead(GUI: Inventory, number: Int, name: String, displayname: String, lore: String) {
        // GUIにアイテムを楽にセットする
        val item = lore(Material.PLAYER_HEAD, displayname, lore)
        val itemMeta = item.itemMeta as SkullMeta
        itemMeta.owningPlayer = Bukkit.getOfflinePlayer(name) // プレイヤー名で設定
        itemMeta.isUnbreakable = true // 不破壊
        item.setItemMeta(itemMeta)
        GUI.setItem(number, item)
    }
    fun set_potionGUIitem(GUI: Inventory, number: Int, item: Material, lore: String, typePotion: PotionEffectType, level: Int, time: Int) {
        // GUIにポーションを楽にセットする
        val itemStack = lore(item, "", lore)
        val potionMeta = itemStack.itemMeta as PotionMeta

        val regenerationEffect = PotionEffect(typePotion, time * 20, level)
        potionMeta.addCustomEffect(regenerationEffect, true)
        potionMeta.setDisplayName("スプラッシュポーション")
        itemStack.setItemMeta(potionMeta)
        GUI.setItem(number, itemStack)
    }
    fun set_enchant_GUIitem(GUI: Inventory, number: Int, lore: String, enchant: Enchantment, level: Int) {
        // GUIにエンチャント本を楽にセットする
        val item = ItemStack(Material.ENCHANTED_BOOK)
        val itemMeta: ItemMeta = item.itemMeta!!
        itemMeta.setLore(listOf(lore))
        if (itemMeta is EnchantmentStorageMeta) {
            itemMeta.addStoredEnchant(enchant, level, true)
        }
        item.setItemMeta(itemMeta)
        GUI.setItem(number, item)
    }

    fun setPotionArrow(GUI: Inventory, number: Int, lore: String, typePotion: PotionEffectType, level: Int, time: Int) {
        val itemStack = lore(Material.TIPPED_ARROW, "効果付きの矢", lore)
        val potionMeta = itemStack.itemMeta as PotionMeta

        val regenerationEffect = PotionEffect(typePotion, time * 20, level)
        potionMeta.addCustomEffect(regenerationEffect, true)
        itemStack.setItemMeta(potionMeta)
        GUI.setItem(number, itemStack)
    }

    fun lore(material: Material, name: String, lore: String): ItemStack {
        val item = ItemStack(material)
        val loreList: MutableList<String> = mutableListOf(lore)
        val meta = item.itemMeta
        meta?.setDisplayName(name)
        if (meta?.displayName?.contains("★") == true) loreList.addAll(listOf("", "クリックで発動"))
        meta?.lore = loreList
        item.setItemMeta(meta)
        return item
    }

    fun no_set(GUI: Inventory, number: Int) {
        setGUIitem(GUI, number, Material.OAK_SIGN, "${ChatColor.YELLOW}近日公開", "", true)
    }

    fun strengthen(player: Player) {
        val gui = player.openInventory.topInventory
        gui.clear()
        setGUIitem(gui, 11, Material.PLAYER_HEAD, "チーム強化", "", true)
        setGUIitem(gui, 15, Material.VILLAGER_SPAWN_EGG, "村人強化", "", true)
    }

    fun pickaxeshop(GUI: Inventory, player: Player) {
        dividing_line(GUI, 9)
        setGUIitem(GUI, 0, Material.WOODEN_PICKAXE, "[ツール]木のピッケル", "1p", true)
        if (player.inventory.contains(Material.WOODEN_PICKAXE)) {
            setGUIitem(GUI, 0, Material.STONE_PICKAXE, "[ツール]石ピッケル", "5p", true)
        } else if (player.inventory.contains(Material.STONE_PICKAXE)) {
            setGUIitem(GUI, 0, Material.IRON_PICKAXE, "[ツール]鉄ピッケル", "40p", true)
        } else if (player.inventory.contains(Material.IRON_PICKAXE)) {
            setGUIitem(GUI, 0, Material.DIAMOND_PICKAXE, "[ツール]ダイヤモンドピッケル", "300p", true)
        } else if (player.inventory.contains(Material.DIAMOND_PICKAXE)) {
            setGUIitem(GUI, 0, Material.NETHERITE_PICKAXE, "[ツール]ネザライトピッケル", "5000p", true)
        } else if (player.inventory.contains(Material.NETHERITE_PICKAXE)) {
            setGUIitem(GUI, 0, Material.BARRIER, "${ChatColor.RED}選択不可", "", true)
        }

        setGUIitem(GUI, 2, Material.STONE_AXE, "[斧]石斧", "5p", true)
        if (player.inventory.contains(Material.STONE_AXE)) {
            setGUIitem(GUI, 2, Material.IRON_AXE, "[斧]鉄斧", "40p", true)
        } else if (player.inventory.contains(Material.IRON_AXE)) {
            setGUIitem(GUI, 2, Material.DIAMOND_AXE, "[斧]ダイヤモンド斧", "300p", true)
        } else if (player.inventory.contains(Material.DIAMOND_AXE)) {
            setGUIitem(GUI, 2, Material.NETHERITE_AXE, "[斧]ネザライト斧", "5000p", true)
        } else if (player.inventory.contains(Material.NETHERITE_AXE)) {
            setGUIitem(GUI, 2, Material.BARRIER, "${ChatColor.RED}選択不可", "", true)
        }
        set_enchant_GUIitem(GUI, 18, "5p", Enchantment.DIG_SPEED, 1)
        set_enchant_GUIitem(GUI, 19, "20p", Enchantment.DIG_SPEED, 2)
        set_enchant_GUIitem(GUI, 20, "300p", Enchantment.DIG_SPEED, 3)
        set_enchant_GUIitem(GUI, 21, "500p", Enchantment.DIG_SPEED, 4)
        set_enchant_GUIitem(GUI, 22, "5000p", Enchantment.DIG_SPEED, 5)
    }
    fun weaponshop(GUI: Inventory, player: Player) {
        dividing_line(GUI, 9)
        setGUIitem(GUI, 0, Material.WOODEN_SWORD, "[武器]木の剣", "1p", true)
        if (player.inventory.contains(Material.WOODEN_SWORD)) {
            setGUIitem(GUI, 0, Material.STONE_SWORD, "[武器]石の剣", "5p", true)
        } else if (player.inventory.contains(Material.STONE_SWORD)) {
            setGUIitem(GUI, 0, Material.IRON_SWORD, "[武器]鉄の剣", "20p", true)
        } else if (player.inventory.contains(Material.IRON_SWORD)) {
            setGUIitem(GUI, 0, Material.DIAMOND_SWORD, "[武器]ダイヤモンドの剣", "100p", true)
        } else if (player.inventory.contains(Material.DIAMOND_SWORD)) {
            setGUIitem(GUI, 0, Material.NETHERITE_SWORD, "[武器]ネザーライトの剣", "1500p", true)
        } else if (player.inventory.contains(Material.NETHERITE_SWORD)) {
            setGUIitem(GUI, 0, Material.BARRIER, "${ChatColor.RED}選択不可", "", true)
        }
        setGUIitem(GUI, 1, Material.STONE_HOE, "${ChatColor.YELLOW}[召喚の杖]", "300p", false)
        setGUIitem(GUI, 2, Material.BOW, "弓", "100p", true)
        setGUIitem(GUI, 3, Material.CROSSBOW, "クロスボー", "300p", true)
        setGUIitem(GUI, 5, Material.ARROW, "矢", "1p", true)
        setPotionArrow(GUI, 7, "30p", PotionEffectType.HEAL, 0, 1)
        set_enchant_GUIitem(GUI, 18, "60p", Enchantment.DAMAGE_ALL, 1)
        set_enchant_GUIitem(GUI, 19, "350p", Enchantment.DAMAGE_ALL, 2)
        set_enchant_GUIitem(GUI, 20, "650p", Enchantment.DAMAGE_ALL, 3)
        set_enchant_GUIitem(GUI, 27, "60p", Enchantment.DAMAGE_UNDEAD, 1)
        set_enchant_GUIitem(GUI, 28, "350p", Enchantment.DAMAGE_UNDEAD, 2)
        set_enchant_GUIitem(GUI, 29, "650p", Enchantment.DAMAGE_UNDEAD, 3)

        set_potionGUIitem(GUI, 33, Material.SPLASH_POTION, "15p", PotionEffectType.HEAL, 0, 1)
        set_potionGUIitem(GUI, 34, Material.SPLASH_POTION, "50p", PotionEffectType.HEAL, 1, 1)
        set_potionGUIitem(GUI, 35, Material.LINGERING_POTION, "300p", PotionEffectType.HEAL, 0, 1)
    }
    fun equipmentshop(GUI: Inventory) {
        dividing_line(GUI, 18)
        setGUIitem(GUI, 0, Material.GOLDEN_CHESTPLATE, "[装備]金のチェストプレート", "100p", true)
        setGUIitem(GUI, 1, Material.GOLDEN_LEGGINGS, "[装備]金のレギンス", "100p", true)
        setGUIitem(GUI, 2, Material.GOLDEN_BOOTS, "[装備]金のブーツ", "100p", true)
        setGUIitem(GUI, 4, Material.DIAMOND_CHESTPLATE, "[装備]ダイヤモンドのチェストプレート", "1000p", true)
        setGUIitem(GUI, 5, Material.DIAMOND_LEGGINGS, "[装備]ダイヤモンドのレギンス", "1000p", true)
        setGUIitem(GUI, 6, Material.DIAMOND_BOOTS, "[装備]ダイヤモンドのブーツ", "1000p", true)
        setGUIitem(GUI, 9, Material.IRON_CHESTPLATE, "[装備]鉄のチェストプレート", "300p", true)
        setGUIitem(GUI, 10, Material.IRON_LEGGINGS, "[装備]鉄のレギンス", "300p", true)
        setGUIitem(GUI, 11, Material.IRON_BOOTS, "[装備]鉄のブーツ", "300p", true)
        setGUIitem(GUI, 13, Material.NETHERITE_CHESTPLATE, "[装備]ネザーライトのチェストプレート", "10000p", true)
        setGUIitem(GUI, 14, Material.NETHERITE_LEGGINGS, "[装備]ネザーライトのレギンス", "8000p", true)
        setGUIitem(GUI, 15, Material.NETHERITE_BOOTS, "[装備]ネザーライトのブーツ", "5000p", true)
        set_enchant_GUIitem(GUI, 27, "10p", Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        set_enchant_GUIitem(GUI, 28, "50p", Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        set_enchant_GUIitem(GUI, 29, "150p", Enchantment.PROTECTION_ENVIRONMENTAL, 3)
        set_enchant_GUIitem(GUI, 30, "250p", Enchantment.PROTECTION_ENVIRONMENTAL, 4)
    }
    fun potionshop(GUI: Inventory, player: Player) {
        dividing_line(GUI, 9)
        setGUIitem(GUI, 0, Material.RED_DYE, "${ChatColor.YELLOW}★チーム全員に攻撃力UP(3分)", "300p", true)
        setGUIitem(GUI, 1, Material.MAGENTA_DYE, "${ChatColor.YELLOW}★チーム全員に再生UP(3分)", "300p", true)
        setGUIitem(GUI, 2, Material.ORANGE_DYE, "${ChatColor.YELLOW}★チーム全員に採掘速度UP(5分)", "300p", true)
        setGUIitem(GUI, 3, Material.GRAY_DYE, "${ChatColor.YELLOW}★チーム全員に耐性(3分)", "300p", true)
        setGUIitem(GUI, 4, Material.LIGHT_BLUE_DYE, "${ChatColor.YELLOW}★チーム全員に移動速度UP(3分)", "300p", true)
        setGUIitem(GUI, 5, Material.NETHER_STAR, "${ChatColor.YELLOW}★チーム全員に攻撃力UP&再生(3分)", "500p", true)
        player.openInventory(GUI)
    }
    fun zombieshop(player: Player) {
        val GUI: Inventory = Bukkit.createInventory(null, 54, "${ChatColor.DARK_GREEN}ショップ[BATTLEGUI]")
        setGUIitem(GUI, 0, Material.IRON_SWORD, "攻撃", "", true)
        setGUIitem(GUI, 9, Material.IRON_CHESTPLATE, "耐休", "", true)
        setGUIitem(GUI, 18, Material.END_CRYSTAL, "特殊", "", true)
        setGUIitem(GUI, 27, Material.SOUL_CAMPFIRE, "範囲", "", true)
        setGUIitem(GUI, 36, Material.HORN_CORAL, "召喚", "", true)
        setGUIitem(GUI, 45, Material.WITHER_SKELETON_SKULL, "破壊", "", true)
        for (i in 0..GUI.size / 9 - 1) {
            setGUIitem(GUI, i * 9 + 1, Material.RED_STAINED_GLASS_PANE, "${ChatColor.RED}選択禁止", "", true)
        }

        setGUIitem(GUI, 2, Material.SLIME_BALL, "${ChatColor.YELLOW}[ゾンビ召喚]ノーマルゾンビ", "30p", true)
        setGUIitem(GUI, 3, Material.IRON_NUGGET, "${ChatColor.YELLOW}[ゾンビ召喚]チビゾンビ", "40p", true)
        setGUIitem(GUI, 4, Material.RAW_COPPER, "${ChatColor.YELLOW}[ゾンビ召喚]ゾンビソルジャー", "40p", true)
        setGUIitem(GUI, 5, Material.FEATHER, "${ChatColor.YELLOW}[ゾンビ召喚]ダッシュマン", "250p", true)

        setGUIitem(GUI, 11, Material.RAW_IRON, "${ChatColor.YELLOW}[ゾンビ召喚]シールドゾンビ", "40p", true)
        setGUIitem(GUI, 12, Material.IRON_INGOT, "${ChatColor.YELLOW}[ゾンビ召喚]タンクマン", "300p", true)

        setGUIitem(GUI, 20, Material.STRING, "${ChatColor.YELLOW}[ゾンビ召喚]スケルトンマン", "100p", true)
        setGUIitem(GUI, 21, Material.LIME_CANDLE, "${ChatColor.YELLOW}[ゾンビ召喚]泥棒", "777p", true)

        setGUIitem(GUI, 29, Material.STICK, "${ChatColor.YELLOW}[ゾンビ召喚]シャーマン", "500p", true)

        setGUIitem(GUI, 38, Material.BOOK, "${ChatColor.YELLOW}[ゾンビ召喚]ネクロマンサー", "400p", true)
        setGUIitem(GUI, 39, Material.HONEYCOMB, "${ChatColor.YELLOW}[ゾンビ召喚]エンペラー", "400p", true)

        setGUIitem(GUI, 47, Material.NETHERITE_SCRAP, "${ChatColor.YELLOW}[ゾンビ召喚]デスクイーン", "3000p", true)

        player.openInventory(GUI)
    }
    fun petshop(GUI: Inventory) {
        setGUIitem(GUI, 0, Material.BONE, "${ChatColor.YELLOW}★狼召喚", "100p", true)
        dividing_line(GUI, 9)
        setGUIitem(GUI, 18, Material.IRON_BLOCK, "${ChatColor.YELLOW}アイアンゴーレム", "500p", true)
        setGUIitem(GUI, 19, Material.GOLD_BLOCK, "${ChatColor.YELLOW}ゴールデンゴーレム", "1500p", true)
        setGUIitem(GUI, 20, Material.DIAMOND_BLOCK, "${ChatColor.YELLOW}ダイヤモンドゴーレム", "8000p", true)
    }
    fun general_merchandiseshop(GUI: Inventory, player: Player) {
        player.openInventory(GUI)
        setGUIitem(GUI, 0, Material.EMERALD, "${ChatColor.GREEN}10p", "10p", true)
        setGUIitem(GUI, 1, Material.EMERALD, "${ChatColor.GREEN}100p", "100p", true)
        setGUIitem(GUI, 2, Material.EMERALD, "${ChatColor.GREEN}1000p", "1000p", true)
        dividing_line(GUI, 9)
        setGUIitem(GUI, 18, Material.ZOMBIE_HEAD, "${ChatColor.GREEN}敵対されない帽子", "8000p", true)
        setGUIitem(GUI, 19, Material.GOLDEN_APPLE, "金リンゴ", "300p", true)
        setGUIitem(GUI, 21, Material.CHEST, "${ChatColor.GREEN}リモートショップ", "300p", true)
    }
    fun villagerlevelup(GUI: Inventory, player: Player) {
        val team_name = GET().TeamName(player) ?: return
        val level = 6 - Data.DataManager.teamDataMap.getOrPut(team_name) { TeamData() }.blockTime
        val shop = Data.DataManager.teamDataMap[team_name]?.entities?.lastOrNull()
        shop.let { entity ->
            val maxHealthAttribute = shop?.getAttribute(Attribute.GENERIC_MAX_HEALTH)
            val maxHealth = maxHealthAttribute?.value?.toInt() ?: 0
            setGUIitem(GUI, 1, Material.RED_DYE, "${ChatColor.YELLOW}★村人体力増加", maxHealth.toString() + "p", true)
        }
        val price = level * 200
        if (level < 5) {
            setGUIitem(GUI, 0, Material.GOLDEN_PICKAXE, "${ChatColor.YELLOW}★鉱石復活速度UP", price.toString() + "p", true)
        } else {
            setGUIitem(GUI, 0, Material.AIR, "", "", true)
        }
        setGUIitem(GUI, 2, Material.GRAY_DYE, "${ChatColor.YELLOW}★村人耐久1(3分)", "300p", true)
        setGUIitem(GUI, 3, Material.PINK_DYE, "${ChatColor.YELLOW}★村人再生1(3分)", "300p", true)
        dividing_line(GUI, 9)
        player.openInventory(GUI)
    }
    fun disturbshop(GUI: Inventory) {
        setGUIitem(GUI, 0, Material.BLACK_CANDLE, "${ChatColor.YELLOW}★盲目(10秒)[妨害]", "300p", true)
        setGUIitem(GUI, 1, Material.LIGHT_GRAY_CANDLE, "${ChatColor.YELLOW}★弱体化(10秒)[妨害]", "300p", true)
        setGUIitem(GUI, 2, Material.BROWN_CANDLE, "${ChatColor.YELLOW}★採掘速度低下(10秒)[妨害]", "300p", true)
    }

    fun dividing_line(GUI: Inventory, beginning: Int) {
        for (i in beginning..beginning + 8) {
            setGUIitem(GUI, i, Material.RED_STAINED_GLASS_PANE, "", "", true)
        }
    }
    fun gamesettingGUI(player: Player) {
        val GUI = Bukkit.createInventory(null, 18, "${ChatColor.DARK_GREEN}設定画面[BATTLEGUI]")
        if (GET().status()) {
            setGUIitem(GUI, 0, Material.BARRIER, "${ChatColor.RED}終了", "", true)
        } else {
            setGUIitem(GUI, 0, Material.EMERALD, "${ChatColor.AQUA}ゲームスタート", "", true)
        }
        setGUIitem(GUI, 1, Material.VILLAGER_SPAWN_EGG, "${ChatColor.YELLOW}ショップ召喚", "", true)
        setGUIitem(GUI, 2, Material.DIAMOND, "${ChatColor.GREEN}参加", "", true)
        setGUIitem(GUI, 3, Material.REDSTONE_BLOCK, "${ChatColor.RED}プラグインリロード", "", true)
        setGUIitem(GUI, 4, Material.PLAYER_HEAD, "${ChatColor.BLUE}プレイヤー", "", true)
        setGUIitem(GUI, 5, Material.COMMAND_BLOCK, "${ChatColor.YELLOW}実験所へ", "", true)
        setGUIitem(GUI, 6, Material.COMMAND_BLOCK, "${ChatColor.YELLOW}ロビーへ", "", true)
        setGUIitem(GUI, 7, Material.COMMAND_BLOCK, "${ChatColor.YELLOW}バトルへ", "", true)
        setGUIitem(GUI, 8, Material.LANTERN, "${ChatColor.GREEN}テレポート", "", true)
        setGUIitem(GUI, 17, Material.ENDER_CHEST, "${ChatColor.GREEN}OPチェスト", "", true)
        setGUIitem(GUI, 9, Material.MAP, "${ChatColor.GREEN}座標設定", "", true)
        player.openInventory(GUI)
    }
    fun Selectworld(player: Player) {
        val GUI = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}設定画面[BATTLEGUI]")
        setGUIitem(GUI, 0, Material.CHEST, "マップ1", "", true)
        setGUIitem(GUI, 1, Material.CHEST, "マップ2", "", true)
        player.openInventory(GUI)
    }
    fun LocationWorld1(player: Player) {
        val GUI = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}設定画面[BATTLEGUI]")
        val location = Data.DataManager.LocationData
        setGUIitem(GUI, 0, Material.ENDER_EYE, "${ChatColor.RED}shop", GET().locationTitle(location.redshop), true)
        setGUIitem(GUI, 1, Material.ENDER_EYE, "${ChatColor.BLUE}shop", GET().locationTitle(location.blueshop), true)
        setGUIitem(GUI, 2, Material.ENDER_EYE, "${ChatColor.RED}spawn", GET().locationTitle(location.redspawn), true)
        setGUIitem(GUI, 3, Material.ENDER_EYE, "${ChatColor.BLUE}spawn", GET().locationTitle(location.bluespawn), true)
        setGUIitem(GUI, 4, Material.ENDER_EYE, "${ChatColor.YELLOW}ランダムチェスト", GET().locationTitle(location.randomChest), true)
        setGUIitem(GUI, 5, Material.ENDER_EYE, "${ChatColor.YELLOW}中央ショップ", GET().locationTitle(location.centerShop), true)
        player.openInventory(GUI)
    }
    fun JoinPlayers(player: Player) {
        val gui = Bukkit.createInventory(null, 18, "${ChatColor.DARK_GREEN}参加プレイヤー[BATTLEGUI]")
        var i = 0
        for (loopPlayer in Bukkit.getOnlinePlayers()) {
            if (Data.DataManager.gameData.ParticipatingPlayer.contains(loopPlayer)) {
                set_playerHead(gui, i, loopPlayer.name, loopPlayer.name, "${ChatColor.GREEN}参加済み")
            } else {
                set_playerHead(gui, i, loopPlayer.name, loopPlayer.name, "${ChatColor.RED}未参加")
            }
            i++
            if (i == 18) { return }
        }
        player.openInventory(gui)
    }
    fun close(title: String, player: Player, inventory: Inventory) {
        when (title) {
            "${ChatColor.DARK_GREEN}チームチェスト" -> player.playSound(player, Sound.BLOCK_CHEST_CLOSE, 1f, 1f)
            "${ChatColor.DARK_GREEN}金床" -> anvil().close(player, inventory)
            "${ChatColor.DARK_GREEN}召喚の杖" -> Hoe().clickGUI(player, inventory)
        }
    }
}
