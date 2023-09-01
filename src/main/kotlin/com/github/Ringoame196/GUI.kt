package com.github.Ringoame196

import com.github.Ringoame196.Entity.Wolf
import com.github.Ringoame196.Game.GameSystem
import com.github.Ringoame196.Game.Scoreboard
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class GUI {
    fun selectGUI(player: Player) {
        val gui = Bukkit.createInventory(null, 27, "${ChatColor.DARK_GREEN}選択画面[BATTLEGUI]")
        guiItem(gui, 10, Material.BEACON, "強化", "", true)
        guiItem(gui, 12, Material.ANVIL, "金床", "", true)
        guiItem(gui, 14, Material.CHEST, "買い物", "", true)
        guiItem(gui, 16, Material.ENDER_CHEST, "チームチェスト", "", true)
        player.openInventory(gui)
    }
    fun guiItem(gui: Inventory, number: Int, setItem: Material, displayName: String, lore: String, unbreakable: Boolean) {
        // GUIにアイテムを楽にセットする
        val item = lore(setItem, displayName, lore, "")
        val itemMeta: ItemMeta? = item.itemMeta
        if (unbreakable) { itemMeta?.isUnbreakable = true } // 不破壊
        item.setItemMeta(itemMeta)
        gui.setItem(number, item)
    }
    fun zombieGUIitem(gui: Inventory, number: Int, setItem: Material, zombieName: String, price: String, id: String) {
        val displayName = "${ChatColor.YELLOW}[ゾンビ召喚]$zombieName"
        val item = lore(setItem, displayName, price, id)
        val itemMeta: ItemMeta? = item.itemMeta
        itemMeta?.isUnbreakable = true
        item.setItemMeta(itemMeta)
        gui.setItem(number, item)
    }
    @Suppress("DEPRECATION")
    fun playerHead(gui: Inventory, number: Int, name: String, displayName: String, lore: String) {
        // GUIにアイテムを楽にセットする
        val item = lore(Material.PLAYER_HEAD, displayName, lore, "")
        val itemMeta = item.itemMeta as SkullMeta
        itemMeta.owningPlayer = Bukkit.getOfflinePlayer(name) // プレイヤー名で設定
        itemMeta.isUnbreakable = true // 不破壊
        item.setItemMeta(itemMeta)
        gui.setItem(number, item)
    }
    fun potionGUIitem(gui: Inventory, number: Int, item: Material, lore: String, typePotion: PotionEffectType, level: Int, time: Int) {
        // GUIにポーションを楽にセットする
        val itemStack = lore(item, "", lore, "")
        val potionMeta = itemStack.itemMeta as PotionMeta

        val regenerationEffect = PotionEffect(typePotion, time * 20, level)
        potionMeta.addCustomEffect(regenerationEffect, true)
        potionMeta.setDisplayName("スプラッシュポーション")
        itemStack.setItemMeta(potionMeta)
        gui.setItem(number, itemStack)
    }
    fun enchantGUIitem(gui: Inventory, number: Int, lore: String, enchant: Enchantment, level: Int) {
        // GUIにエンチャント本を楽にセットする
        val item = ItemStack(Material.ENCHANTED_BOOK)
        val itemMeta: ItemMeta = item.itemMeta!!
        itemMeta.setLore(listOf(lore))
        if (itemMeta is EnchantmentStorageMeta) {
            itemMeta.addStoredEnchant(enchant, level, true)
        }
        item.setItemMeta(itemMeta)
        gui.setItem(number, item)
    }

    fun potionArrow(gui: Inventory, number: Int, lore: String, typePotion: PotionEffectType, level: Int, time: Int) {
        val itemStack = lore(Material.TIPPED_ARROW, "効果付きの矢", lore, "")
        val potionMeta = itemStack.itemMeta as PotionMeta

        val regenerationEffect = PotionEffect(typePotion, time * 20, level)
        potionMeta.addCustomEffect(regenerationEffect, true)
        itemStack.setItemMeta(potionMeta)
        gui.setItem(number, itemStack)
    }
    fun lore(material: Material, name: String, price: String, subLore: String?): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta
        meta?.setDisplayName(name)
        val loreList: MutableList<String> = mutableListOf(price)
        if (subLore != null) {
            loreList.add(subLore)
        } else if (meta?.displayName?.contains("★") == true) {
            loreList.addAll(listOf("", "クリックで発動"))
        }
        meta?.lore = loreList
        item.setItemMeta(meta)
        return item
    }

    fun noSet(gui: Inventory, number: Int) {
        guiItem(gui, number, Material.OAK_SIGN, "${ChatColor.YELLOW}近日公開", "", true)
    }

    fun strengthen(player: Player) {
        val gui = player.openInventory.topInventory
        gui.clear()
        guiItem(gui, 11, Material.PLAYER_HEAD, "チーム強化", "", true)
        guiItem(gui, 15, Material.VILLAGER_SPAWN_EGG, "村人強化", "", true)
    }

    fun pickaxeshop(gui: Inventory, player: Player) {
        dividingLine(gui, 9)
        guiItem(gui, 0, Material.WOODEN_PICKAXE, "[ツール]木のピッケル", "1p", true)
        if (player.inventory.contains(Material.WOODEN_PICKAXE)) {
            guiItem(gui, 0, Material.STONE_PICKAXE, "[ツール]石ピッケル", "5p", true)
        } else if (player.inventory.contains(Material.STONE_PICKAXE)) {
            guiItem(gui, 0, Material.IRON_PICKAXE, "[ツール]鉄ピッケル", "40p", true)
        } else if (player.inventory.contains(Material.IRON_PICKAXE)) {
            guiItem(gui, 0, Material.DIAMOND_PICKAXE, "[ツール]ダイヤモンドピッケル", "300p", true)
        } else if (player.inventory.contains(Material.DIAMOND_PICKAXE)) {
            guiItem(gui, 0, Material.NETHERITE_PICKAXE, "[ツール]ネザライトピッケル", "5000p", true)
        } else if (player.inventory.contains(Material.NETHERITE_PICKAXE)) {
            guiItem(gui, 0, Material.BARRIER, "${ChatColor.RED}選択不可", "", true)
        }

        guiItem(gui, 2, Material.STONE_AXE, "[斧]石斧", "5p", true)
        if (player.inventory.contains(Material.STONE_AXE)) {
            guiItem(gui, 2, Material.IRON_AXE, "[斧]鉄斧", "40p", true)
        } else if (player.inventory.contains(Material.IRON_AXE)) {
            guiItem(gui, 2, Material.DIAMOND_AXE, "[斧]ダイヤモンド斧", "300p", true)
        } else if (player.inventory.contains(Material.DIAMOND_AXE)) {
            guiItem(gui, 2, Material.NETHERITE_AXE, "[斧]ネザライト斧", "5000p", true)
        } else if (player.inventory.contains(Material.NETHERITE_AXE)) {
            guiItem(gui, 2, Material.BARRIER, "${ChatColor.RED}選択不可", "", true)
        }
        enchantGUIitem(gui, 18, "5p", Enchantment.DIG_SPEED, 1)
        enchantGUIitem(gui, 19, "20p", Enchantment.DIG_SPEED, 2)
        enchantGUIitem(gui, 20, "300p", Enchantment.DIG_SPEED, 3)
        enchantGUIitem(gui, 21, "500p", Enchantment.DIG_SPEED, 4)
        enchantGUIitem(gui, 22, "5000p", Enchantment.DIG_SPEED, 5)
    }
    fun weaponshop(gui: Inventory, player: Player) {
        dividingLine(gui, 9)
        guiItem(gui, 0, Material.WOODEN_SWORD, "[武器]木の剣", "1p", true)
        if (player.inventory.contains(Material.WOODEN_SWORD)) {
            guiItem(gui, 0, Material.STONE_SWORD, "[武器]石の剣", "5p", true)
        } else if (player.inventory.contains(Material.STONE_SWORD)) {
            guiItem(gui, 0, Material.IRON_SWORD, "[武器]鉄の剣", "20p", true)
        } else if (player.inventory.contains(Material.IRON_SWORD)) {
            guiItem(gui, 0, Material.DIAMOND_SWORD, "[武器]ダイヤモンドの剣", "100p", true)
        } else if (player.inventory.contains(Material.DIAMOND_SWORD)) {
            guiItem(gui, 0, Material.NETHERITE_SWORD, "[武器]ネザーライトの剣", "1500p", true)
        } else if (player.inventory.contains(Material.NETHERITE_SWORD)) {
            guiItem(gui, 0, Material.BARRIER, "${ChatColor.RED}選択不可", "", true)
        }
        guiItem(gui, 1, Material.STONE_HOE, "${ChatColor.YELLOW}[召喚の杖]", "300p", false)
        guiItem(gui, 2, Material.BOW, "弓", "100p", true)
        guiItem(gui, 3, Material.CROSSBOW, "クロスボー", "300p", true)
        guiItem(gui, 5, Material.ARROW, "矢", "1p", true)
        potionArrow(gui, 7, "30p", PotionEffectType.HEAL, 0, 1)
        enchantGUIitem(gui, 18, "60p", Enchantment.DAMAGE_ALL, 1)
        enchantGUIitem(gui, 19, "350p", Enchantment.DAMAGE_ALL, 2)
        enchantGUIitem(gui, 20, "650p", Enchantment.DAMAGE_ALL, 3)
        enchantGUIitem(gui, 27, "60p", Enchantment.DAMAGE_UNDEAD, 1)
        enchantGUIitem(gui, 28, "350p", Enchantment.DAMAGE_UNDEAD, 2)
        enchantGUIitem(gui, 29, "650p", Enchantment.DAMAGE_UNDEAD, 3)
        enchantGUIitem(gui, 30, "1200p", Enchantment.DAMAGE_UNDEAD, 4)

        potionGUIitem(gui, 33, Material.SPLASH_POTION, "15p", PotionEffectType.HEAL, 0, 1)
        potionGUIitem(gui, 34, Material.SPLASH_POTION, "50p", PotionEffectType.HEAL, 1, 1)
        potionGUIitem(gui, 35, Material.LINGERING_POTION, "300p", PotionEffectType.HEAL, 0, 1)
    }
    fun equipmentshop(gui: Inventory) {
        dividingLine(gui, 18)
        guiItem(gui, 0, Material.GOLDEN_CHESTPLATE, "[装備]金のチェストプレート", "100p", true)
        guiItem(gui, 1, Material.GOLDEN_LEGGINGS, "[装備]金のレギンス", "100p", true)
        guiItem(gui, 2, Material.GOLDEN_BOOTS, "[装備]金のブーツ", "100p", true)
        guiItem(gui, 4, Material.DIAMOND_CHESTPLATE, "[装備]ダイヤモンドのチェストプレート", "1000p", true)
        guiItem(gui, 5, Material.DIAMOND_LEGGINGS, "[装備]ダイヤモンドのレギンス", "1000p", true)
        guiItem(gui, 6, Material.DIAMOND_BOOTS, "[装備]ダイヤモンドのブーツ", "1000p", true)
        guiItem(gui, 9, Material.IRON_CHESTPLATE, "[装備]鉄のチェストプレート", "300p", true)
        guiItem(gui, 10, Material.IRON_LEGGINGS, "[装備]鉄のレギンス", "300p", true)
        guiItem(gui, 11, Material.IRON_BOOTS, "[装備]鉄のブーツ", "300p", true)
        guiItem(gui, 13, Material.NETHERITE_CHESTPLATE, "[装備]ネザーライトのチェストプレート", "10000p", true)
        guiItem(gui, 14, Material.NETHERITE_LEGGINGS, "[装備]ネザーライトのレギンス", "8000p", true)
        guiItem(gui, 15, Material.NETHERITE_BOOTS, "[装備]ネザーライトのブーツ", "5000p", true)
        enchantGUIitem(gui, 27, "10p", Enchantment.PROTECTION_ENVIRONMENTAL, 1)
        enchantGUIitem(gui, 28, "50p", Enchantment.PROTECTION_ENVIRONMENTAL, 2)
        enchantGUIitem(gui, 29, "150p", Enchantment.PROTECTION_ENVIRONMENTAL, 3)
        enchantGUIitem(gui, 30, "250p", Enchantment.PROTECTION_ENVIRONMENTAL, 4)
    }
    fun potionshop(gui: Inventory, player: Player) {
        dividingLine(gui, 9)
        guiItem(gui, 0, Material.RED_DYE, "${ChatColor.YELLOW}★チーム全員に攻撃力UP(3分)", "300p", true)
        guiItem(gui, 1, Material.MAGENTA_DYE, "${ChatColor.YELLOW}★チーム全員に再生UP(3分)", "300p", true)
        guiItem(gui, 2, Material.ORANGE_DYE, "${ChatColor.YELLOW}★チーム全員に採掘速度UP(5分)", "300p", true)
        guiItem(gui, 3, Material.GRAY_DYE, "${ChatColor.YELLOW}★チーム全員に耐性(3分)", "300p", true)
        guiItem(gui, 4, Material.LIGHT_BLUE_DYE, "${ChatColor.YELLOW}★チーム全員に移動速度UP(3分)", "300p", true)
        guiItem(gui, 5, Material.NETHER_STAR, "${ChatColor.YELLOW}★チーム全員に攻撃力UP&再生(3分)", "500p", true)
        player.openInventory(gui)
        val team = GET().teamName(player)
        if (Scoreboard().getValue(GET().getTeamScoreName(team), "ゾンビ通知") != 1) {
            guiItem(gui, 18, Material.OAK_SIGN, "${ChatColor.YELLOW}★ゾンビ襲来警報(ゾンビが召喚時にゾンビの声が聞こえる)", "500p", true)
        } else {
            guiItem(gui, 18, Material.BARRIER, "${ChatColor.RED}選択不可", "", true)
        }
    }
    fun zombieshop(player: Player) {
        val gui: Inventory = Bukkit.createInventory(null, 54, "${ChatColor.DARK_GREEN}ショップ[BATTLEGUI]")
        for (i in 0 until gui.size / 9) {
            guiItem(gui, i * 9 + 1, Material.RED_STAINED_GLASS_PANE, "${ChatColor.RED}選択禁止", "", true)
        }

        guiItem(gui, 0, Material.IRON_SWORD, "攻撃", "", true)
        zombieGUIitem(gui, 2, Material.SLIME_BALL, "ノーマルゾンビ", "30p", "normal")
        zombieGUIitem(gui, 3, Material.IRON_NUGGET, "チビゾンビ", "40p", "chibi")
        zombieGUIitem(gui, 4, Material.RAW_COPPER, "ゾンビソルジャー", "40p", "soldier")
        zombieGUIitem(gui, 5, Material.FEATHER, "ダッシュマン", "250p", "dashman")
        zombieGUIitem(gui, 6, Material.BLAZE_POWDER, "バトルロード", "850p", "battleLord")
        zombieGUIitem(gui, 7, Material.BONE_MEAL, "カスタムロード", "1000p", "customLoad")

        guiItem(gui, 9, Material.IRON_CHESTPLATE, "耐休", "", true)
        zombieGUIitem(gui, 11, Material.RAW_IRON, "シールドゾンビ", "40p", "shield")
        zombieGUIitem(gui, 12, Material.IRON_INGOT, "タンクマン", "300p", "tankman")

        guiItem(gui, 18, Material.END_CRYSTAL, "特殊", "", true)
        zombieGUIitem(gui, 20, Material.STRING, "スケルトンマン", "100p", "skeletonman")
        zombieGUIitem(gui, 21, Material.LIME_CANDLE, "泥棒", "777p", "thief")

        guiItem(gui, 27, Material.SOUL_CAMPFIRE, "範囲", "", true)
        zombieGUIitem(gui, 29, Material.STICK, "シャーマン", "500p", "shaman")
        zombieGUIitem(gui, 30, Material.ICE, "フロストメイジ", "700p", "Frostmage")

        guiItem(gui, 36, Material.HORN_CORAL, "召喚", "", true)
        zombieGUIitem(gui, 38, Material.BOOK, "ネクロマンサー", "400p", "necromancer")
        zombieGUIitem(gui, 39, Material.HONEYCOMB, "エンペラー", "400p", "emperor")

        guiItem(gui, 45, Material.WITHER_SKELETON_SKULL, "破壊", "", true)
        zombieGUIitem(gui, 47, Material.NETHERITE_SCRAP, "デスクイーン", "3000p", "deathqueen")

        player.openInventory(gui)
    }
    fun petShop(gui: Inventory) {
        guiItem(gui, 0, Material.BONE, "${ChatColor.YELLOW}★狼召喚", "100p", true)
        dividingLine(gui, 9)
        guiItem(gui, 18, Material.IRON_BLOCK, "${ChatColor.YELLOW}アイアンゴーレム", "1000p", true)
        guiItem(gui, 19, Material.GOLD_BLOCK, "${ChatColor.YELLOW}ゴールデンゴーレム", "1500p", true)
        guiItem(gui, 20, Material.BLAZE_ROD, "${ChatColor.RED}ブレイズ", "400p", true)
        guiItem(gui, 21, Material.SHULKER_SHELL, "${ChatColor.YELLOW}シュルカー", "800p", true)
    }
    fun generalMerchandiseshop(gui: Inventory, player: Player) {
        player.openInventory(gui)
        guiItem(gui, 0, Material.EMERALD, "${ChatColor.GREEN}10p", "10p", true)
        guiItem(gui, 1, Material.EMERALD, "${ChatColor.GREEN}100p", "100p", true)
        guiItem(gui, 2, Material.EMERALD, "${ChatColor.GREEN}1000p", "1000p", true)
        dividingLine(gui, 9)
        guiItem(gui, 18, Material.GOLDEN_APPLE, "金リンゴ", "300p", true)
        guiItem(gui, 19, Material.CHEST, "${ChatColor.GREEN}リモートショップ", "300p", true)
    }
    fun villagerlevelup(gui: Inventory, player: Player) {
        val teamName = GET().teamName(player) ?: return
        val level = 6 - GET().getTeamRevivalTime(GET().teamName(player)!!)
        val shop = GET().getTeamshop(teamName)
        if (shop == null) {
            Player().errormessage("ショップが見つかりませんでした", player)
            return
        }
        shop.let {
            val maxHealthAttribute = shop?.getAttribute(Attribute.GENERIC_MAX_HEALTH)
            val maxHealth = maxHealthAttribute?.value?.toInt() ?: 0
            guiItem(gui, 1, Material.RED_DYE, "${ChatColor.YELLOW}★村人体力増加", maxHealth.toString() + "p", true)
        }
        val price = level * 200
        if (level < 5) {
            guiItem(gui, 0, Material.GOLDEN_PICKAXE, "${ChatColor.YELLOW}★鉱石復活速度UP", price.toString() + "p", true)
        } else {
            guiItem(gui, 0, Material.AIR, "", "", true)
        }
        guiItem(gui, 2, Material.GRAY_DYE, "${ChatColor.YELLOW}★村人耐久1(3分)", "300p", true)
        guiItem(gui, 3, Material.PINK_DYE, "${ChatColor.YELLOW}★村人再生1(3分)", "300p", true)
        dividingLine(gui, 9)
        player.openInventory(gui)
    }
    fun disturbshop(gui: Inventory) {
        guiItem(gui, 0, Material.BLACK_CANDLE, "${ChatColor.YELLOW}★盲目(10秒)[妨害]", "300p", true)
        guiItem(gui, 1, Material.LIGHT_GRAY_CANDLE, "${ChatColor.YELLOW}★弱体化(10秒)[妨害]", "300p", true)
        guiItem(gui, 2, Material.BROWN_CANDLE, "${ChatColor.YELLOW}★採掘速度低下(10秒)[妨害]", "300p", true)
    }

    fun dividingLine(gui: Inventory, beginning: Int) {
        for (i in beginning..beginning + 8) {
            guiItem(gui, i, Material.RED_STAINED_GLASS_PANE, "", "", true)
        }
    }
    fun gamesettingGUI(player: Player) {
        val gui = Bukkit.createInventory(null, 18, "${ChatColor.DARK_GREEN}設定画面[BATTLEGUI]")
        if (GET().status()) {
            guiItem(gui, 0, Material.BARRIER, "${ChatColor.RED}終了", "", true)
        } else {
            guiItem(gui, 0, Material.EMERALD, "${ChatColor.AQUA}ゲームスタート", "", true)
        }
        guiItem(gui, 1, Material.VILLAGER_SPAWN_EGG, "${ChatColor.YELLOW}ショップ召喚", "", true)
        guiItem(gui, 2, Material.DIAMOND, "${ChatColor.GREEN}参加", "", true)
        guiItem(gui, 3, Material.PLAYER_HEAD, "${ChatColor.BLUE}プレイヤー", "", true)
        guiItem(gui, 4, Material.COMMAND_BLOCK, "${ChatColor.YELLOW}実験所へ", "", true)
        guiItem(gui, 5, Material.COMMAND_BLOCK, "${ChatColor.YELLOW}ロビーへ", "", true)
        guiItem(gui, 6, Material.COMMAND_BLOCK, "${ChatColor.YELLOW}バトルへ", "", true)
        guiItem(gui, 7, Material.COMMAND_BLOCK, "${ChatColor.YELLOW}トレジャーバトル", "", true)
        guiItem(gui, 8, Material.REDSTONE, "${ChatColor.GREEN}${Data.DataManager.gameData.playMap}", "", true)
        guiItem(gui, 9, Material.MAP, "${ChatColor.GREEN}座標設定", "", true)
        player.openInventory(gui)
    }
    fun selectworld(player: Player) {
        val gui = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}設定画面[BATTLEGUI]")
        guiItem(gui, 0, Material.CHEST, "マップ1", "", true)
        guiItem(gui, 1, Material.CHEST, "マップ2", "", true)
        player.openInventory(gui)
    }
    fun locationWorld1(player: Player) {
        val gui = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}設定画面[BATTLEGUI]")
        val location = Data.DataManager.LocationData
        guiItem(gui, 0, Material.ENDER_EYE, "${ChatColor.RED}shop", GET().locationTitle(location.redshop), true)
        guiItem(gui, 1, Material.ENDER_EYE, "${ChatColor.BLUE}shop", GET().locationTitle(location.blueshop), true)
        guiItem(gui, 2, Material.ENDER_EYE, "${ChatColor.RED}spawn", GET().locationTitle(location.redspawn), true)
        guiItem(gui, 3, Material.ENDER_EYE, "${ChatColor.BLUE}spawn", GET().locationTitle(location.bluespawn), true)
        guiItem(gui, 4, Material.ENDER_EYE, "${ChatColor.YELLOW}ランダムチェスト", GET().locationTitle(location.randomChest), true)
        player.openInventory(gui)
    }
    fun locationWorld2(player: Player) {
        val gui = Bukkit.createInventory(null, 18, "${ChatColor.DARK_GREEN}設定画面[BATTLEGUI]")
        val location = Data.DataManager.LocationData
        guiItem(gui, 0, Material.ENDER_EYE, "${ChatColor.RED}mshop", GET().locationTitle(location.mredshop), true)
        guiItem(gui, 1, Material.ENDER_EYE, "${ChatColor.BLUE}mshop", GET().locationTitle(location.mblueshop), true)
        guiItem(gui, 2, Material.ENDER_EYE, "${ChatColor.RED}mspawn", GET().locationTitle(location.mredspawn), true)
        guiItem(gui, 3, Material.ENDER_EYE, "${ChatColor.BLUE}mspawn", GET().locationTitle(location.mbluespawn), true)
        guiItem(gui, 4, Material.ENDER_EYE, "${ChatColor.YELLOW}mランダムチェスト1", GET().locationTitle(location.mrandomChest1), true)
        guiItem(gui, 5, Material.ENDER_EYE, "${ChatColor.YELLOW}mランダムチェスト2", GET().locationTitle(location.mrandomChest2), true)
        guiItem(gui, 9, Material.ENDER_EYE, "${ChatColor.RED}mspawnZombie1", GET().locationTitle(location.mredZombiespawn1), true)
        guiItem(gui, 10, Material.ENDER_EYE, "${ChatColor.RED}mspawnZombie2", GET().locationTitle(location.mredZombiespawn2), true)
        guiItem(gui, 11, Material.ENDER_EYE, "${ChatColor.RED}mspawnZombie3", GET().locationTitle(location.mredZombiespawn3), true)
        guiItem(gui, 12, Material.ENDER_EYE, "${ChatColor.BLUE}mspawnZombie1", GET().locationTitle(location.mblueZombiespawn1), true)
        guiItem(gui, 13, Material.ENDER_EYE, "${ChatColor.BLUE}mspawnZombie2", GET().locationTitle(location.mblueZombiespawn2), true)
        guiItem(gui, 14, Material.ENDER_EYE, "${ChatColor.BLUE}mspawnZombie3", GET().locationTitle(location.mblueZombiespawn3), true)

        player.openInventory(gui)
    }
    fun joinPlayers(player: Player) {
        val gui = Bukkit.createInventory(null, 18, "${ChatColor.DARK_GREEN}参加プレイヤー[BATTLEGUI]")
        var i = 0
        for (loopPlayer in Bukkit.getOnlinePlayers()) {
            when (Scoreboard().getValue("participatingPlayer", loopPlayer.name) ?: 0) {
                0 -> playerHead(gui, i, loopPlayer.name, loopPlayer.name, "${ChatColor.DARK_RED}未参加")
                1 -> playerHead(gui, i, loopPlayer.name, loopPlayer.name, "${ChatColor.GREEN}参加済み(ランダム)")
                2 -> playerHead(gui, i, loopPlayer.name, loopPlayer.name, "${ChatColor.RED}参加済み(赤チーム)")
                3 -> playerHead(gui, i, loopPlayer.name, loopPlayer.name, "${ChatColor.BLUE}参加済み(青チーム)")
            }
            i++
            if (i == 18) { return }
        }
        player.openInventory(gui)
    }
    fun messageBook(player: Player) {
        val gui = Bukkit.createInventory(null, 18, "${ChatColor.DARK_BLUE}チームメッセージ[BATTLEGUI]")
        guiItem(gui, 0, Material.PAPER, "${ChatColor.GREEN}よろしくお願いします", "", true)
        guiItem(gui, 1, Material.PAPER, "${ChatColor.GREEN}ありがとう", "", true)
        guiItem(gui, 2, Material.PAPER, "${ChatColor.GREEN}今 ${Scoreboard().getValue("point",player.name)}ポイント持っています", "", true)
        guiItem(gui, 3, Material.PAPER, "${ChatColor.RED}ゾンビが攻めてきてます", "", true)
        guiItem(gui, 4, Material.PAPER, "${ChatColor.RED}助けてください", "", true)
        guiItem(gui, 5, Material.PAPER, "${ChatColor.GREEN}攻めましょう", "", true)
        guiItem(gui, 6, Material.PAPER, "${ChatColor.GREEN}中央行きましょう", "", true)
        guiItem(gui, 7, Material.PAPER, "${ChatColor.GREEN}中央行ってきます", "", true)
        guiItem(gui, 8, Material.PAPER, "${ChatColor.GREEN}自陣に戻ります", "", true)
        guiItem(gui, 9, Material.PAPER, "${ChatColor.GREEN}チーム強化してください", "", true)
        guiItem(gui, 10, Material.PAPER, "${ChatColor.GREEN}ポイントください", "", true)
        player.openInventory(gui)
    }
    fun selectGUI(player: Player, item: ItemStack) {
        val gui = Bukkit.createInventory(null, 27, "${ChatColor.DARK_GREEN}ショップ[BATTLEGUI]")
        when (item.itemMeta?.displayName) {
            "金床" -> Anvil().set(player)
            "買い物" -> Shop().gui(player)
            "チームチェスト" -> Team().openChest(player, GET().teamName(player).toString())
            "強化" -> GUI().strengthen(player)
            "チーム強化" -> GUI().potionshop(gui, player)
            "村人強化" -> GUI().villagerlevelup(gui, player)
        }
    }
    fun clickSystem(plugin: Plugin, e: InventoryClickEvent, player: Player, guiName: String, item: ItemStack) {
        if (guiName == "${ChatColor.DARK_GREEN}金床") {
            Anvil().click(e)
            return
        }
        if (!guiName.contains("[BATTLEGUI]")) { return }
        e.isCancelled = true
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        when (guiName.replace("[BATTLEGUI]", "")) {
            "${ChatColor.BLUE}攻防戦ショップ" -> homeShop(player, item)
            "${ChatColor.DARK_GREEN}ショップ" -> Shop().clickEvent(item, player)
            "${ChatColor.DARK_GREEN}設定画面" -> GameSystem().gameSettingGUIClick(plugin, player, item, e)
            "${ChatColor.DARK_GREEN}参加プレイヤー" -> item.itemMeta?.let { GameSystem().playersJoin(it.displayName, player) }
            "${ChatColor.DARK_GREEN}選択画面" -> GUI().selectGUI(player, item)
            "${ChatColor.DARK_BLUE}チームメッセージ" -> {
                MessageBook().send(player, item.itemMeta?.displayName!!)
            }
        }
    }

    fun homeShop(player: Player, item: ItemStack) {
        val teamName = GET().teamName(player) ?: return
        val itemName = item.itemMeta?.displayName ?: return
        val shop: Inventory = Bukkit.createInventory(null, 36, "${ChatColor.DARK_GREEN}ショップ[BATTLEGUI]")
        when (itemName) {
            "${ChatColor.YELLOW}ツール" -> GUI().pickaxeshop(shop, player)
            "${ChatColor.YELLOW}武器" -> GUI().weaponshop(shop, player)
            "${ChatColor.YELLOW}防具" -> GUI().equipmentshop(shop)
            "${ChatColor.YELLOW}その他" -> GUI().generalMerchandiseshop(shop, player)
            "${ChatColor.YELLOW}お邪魔アイテム" -> GUI().disturbshop(shop)
            "${ChatColor.YELLOW}ゾンビ" -> {
                GUI().zombieshop(player)
                return
            }
            "${ChatColor.YELLOW}ペット" -> GUI().petShop(shop)
            else -> return
        }
        shop.getItem(0) ?: return
        player.openInventory(shop)
    }

    fun clickInvocation(player: Player, itemName: String, teamName: String) {
        val checkName = itemName
            .replace("${ChatColor.YELLOW}★", "")
            .replace("チーム全員に", "")
        when (checkName) {
            "ショップ解放" -> Shop().release(player, teamName)
            "攻撃力UP(3分)" -> Team().GiveEffect(player, itemName, PotionEffectType.INCREASE_DAMAGE, null, 0, 180)
            "再生UP(3分)" -> Team().GiveEffect(player, itemName, PotionEffectType.REGENERATION, null, 2, 180)
            "採掘速度UP(5分)" -> Team().GiveEffect(player, itemName, PotionEffectType.FAST_DIGGING, null, 2, 300)
            "耐性(3分)" -> Team().GiveEffect(player, itemName, PotionEffectType.DAMAGE_RESISTANCE, null, 1, 180)
            "移動速度UP(3分)" -> Team().GiveEffect(player, itemName, PotionEffectType.SPEED, null, 1, 180)
            "攻撃力UP&再生(3分)" -> Team().GiveEffect(player, itemName, PotionEffectType.REGENERATION, PotionEffectType.INCREASE_DAMAGE, 0, 180)
            "鉱石復活速度UP" -> Team().fastbreaklevel(teamName, player, itemName)
            "村人体力増加" -> Shop().teamMaxHPadd(teamName, player, itemName, 10)
            "盲目(10秒)[妨害]" -> Team().GiveEffect(player, itemName, PotionEffectType.BLINDNESS, null, 255, 10)
            "弱体化(10秒)[妨害]" -> Team().GiveEffect(player, itemName, PotionEffectType.WEAKNESS, null, 255, 10)
            "採掘速度低下(10秒)[妨害]" -> Team().GiveEffect(player, itemName, PotionEffectType.SLOW_DIGGING, null, 255, 10)
            "狼召喚" -> Wolf().summon(player)
            "村人耐久1(3分)" -> Shop().effect(player, itemName, PotionEffectType.DAMAGE_RESISTANCE, 180, 1)
            "村人再生1(3分)" -> Shop().effect(player, itemName, PotionEffectType.REGENERATION, 180, 1)
            "ゾンビ襲来警報(ゾンビが召喚時にゾンビの声が聞こえる)" -> {
                Scoreboard().set(GET().getTeamScoreName(teamName), "ゾンビ通知", 1)
                GUI().potionshop(player.openInventory.topInventory, player)
                Team().sendMessage("${player.name}さんが「ゾンビ襲撃警報」を発動しました ※ゾンビが召喚されたときに ゾンビの声が聞こえるようになりました", GET().teamName(player).toString())
            }
        }
    }
}
