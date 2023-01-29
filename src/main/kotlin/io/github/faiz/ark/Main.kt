package io.github.faiz.ark

import dev.sergiferry.playernpc.api.NPC
import dev.sergiferry.playernpc.api.NPC.Global
import dev.sergiferry.playernpc.api.NPCLib
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin


class Main:JavaPlugin(), Listener {

    var NList: HashMap<String,Global> = HashMap()

    override fun onEnable() {
        NPCLib.getInstance().registerPlugin(this)
        logger.info("Ark has been enabled!")
    }

    override fun onDisable() {
        logger.info("Ark has been disabled!")
    }

    @EventHandler
    fun onJoin(e:PlayerQuitEvent) {
        var npc:Global = NPCLib.getInstance().generateGlobalNPC(this,e.player.name,e.player.location)
        npc.setSkin(e.player.name)
        npc.isShowOnTabList = false
        npc.setText(e.player.name)
        npc.pose = NPC.Pose.SLEEPING
        npc.teleport(e.player.location)
        var hp = e.player.health
        NList.set(e.player.name,npc)
        npc.addCustomClickAction(NPC.Interact.ClickType.LEFT_CLICK) { enpc, pl ->
            var item = pl.player!!.inventory.itemInMainHand.type
            when (item){
                Material.WOODEN_PICKAXE,Material.GOLDEN_PICKAXE -> hp -= 2*pl.player!!.attackCooldown
                Material.STONE_PICKAXE -> hp -= 3*pl.player!!.attackCooldown
                Material.WOODEN_SWORD,Material.GOLDEN_SWORD,Material.IRON_PICKAXE -> hp -= 4*pl.player!!.attackCooldown
                Material.STONE_SWORD,Material.DIAMOND_PICKAXE -> hp -= 5*pl.player!!.attackCooldown
                Material.IRON_SWORD,Material.NETHERITE_PICKAXE -> hp -= 6*pl.player!!.attackCooldown
                Material.DIAMOND_SWORD,Material.WOODEN_AXE,Material.GOLDEN_AXE-> hp -= 7*pl.player!!.attackCooldown
                Material.NETHERITE_SWORD -> hp -= 8*pl.player!!.attackCooldown
                Material.STONE_AXE,Material.IRON_AXE,Material.DIAMOND_AXE -> hp -= 9*pl.player!!.attackCooldown
                Material.NETHERITE_AXE -> hp -= 10*pl.player!!.attackCooldown
                else -> {hp -= 1}
            }
            if(hp <= 0){
                npc.destroy()
                NList.remove(e.player.name)
                e.player.inventory.contents.forEach {
                    if(it == null) return@forEach
                    e.player.world.dropItemNaturally(enpc.location,it)
                    e.player.inventory.remove(it)
                }
            }
        }

    }

    @EventHandler
    fun onQuit(e:PlayerJoinEvent){
        if(!NList.containsKey(e.player.name)){
            e.player.health = 0.0
        }
    }

}